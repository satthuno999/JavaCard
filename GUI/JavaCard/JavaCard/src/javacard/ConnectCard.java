/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacard;

import com.sun.org.apache.xpath.internal.axes.HasPositionalPredChecker;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import jdk.nashorn.internal.ir.Terminal;
import java.util.List;
import javax.smartcardio.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author Spark_Mac
 */
public class ConnectCard {
    public byte [] data;
    public String message;
    public String strID;
    public String strName;
    public String strDate;
    public String strPhone;
    public String connectapplet(){
        try{
            
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            
            CardTerminal terminal = terminals.get(0);
            
            Card card = terminal.connect("*");
            
            CardChannel channel = card.getBasicChannel();
            
            byte[] aid = {(byte)0x25,(byte)0x10,(byte)0x19,(byte)0x99,(byte)0x00,(byte)0x00,(byte)0x00};
            ResponseAPDU answer = channel.transmit(new CommandAPDU(0x00,0xA4,0x04,0x00,aid));
            String kq = answer.toString();
            data = answer.getData();
            return kq;
            
        }
        catch(Exception ex){
            return "Error";
        }
    }
    
    
    public boolean verifyPin(String pin){
        
        byte[] pinbyte =  pin.getBytes();
        try{
            
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            
            CardTerminal terminal = terminals.get(0);
            
            Card card = terminal.connect("*");
            
            CardChannel channel = card.getBasicChannel();
            
            ResponseAPDU answer = channel.transmit(new CommandAPDU(0xB0,0x42,0x00,0x00,pinbyte));
            
            message = answer.toString();
            switch (((message.split("="))[1]).toUpperCase()) {
                case "9000":
                    return true;
                case "9C02":
                    JOptionPane.showMessageDialog(null, "Bạn đã nhập sai PIN");
                    return false;
                case "9C0C":
                    JOptionPane.showMessageDialog(null, "Bạn đã nhập sai quá số lần thử!Thẻ đã bị khoá");
                    return false;
                default:
                    return false;
            }
            
        }
        catch(Exception ex){
            return false;
        }
    }
    
    public boolean createPIN(String pin){
        
        byte[] pinbyte =  pin.getBytes();
        byte lengt = (byte) pinbyte.length;
        
        byte[] send = new byte[lengt+1];
        send[0] = lengt;
        for(int i =1;i<send.length;i++){
            send[i] = pinbyte[i-1];
        }
        try{
            
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            
            CardTerminal terminal = terminals.get(0);
            
            Card card = terminal.connect("*");
            
            CardChannel channel = card.getBasicChannel();
            
            ResponseAPDU answer = channel.transmit(new CommandAPDU(0xB0,0x40,0x00,0x03,send));
            
            message = answer.toString();
            if((message.split("="))[1].equals("9000")){
                
                return true;
            
            }
            else return false;
            
        }
        catch(Exception ex){
            return false;
        }
    }
    
        public boolean ChangePIN(String oldPin,String newPin){
        
        byte[] pinOldByte =  oldPin.getBytes();
        byte lengtOld = (byte) pinOldByte.length;
        
        byte[] pinNewByte =  newPin.getBytes();
        byte lengtNew = (byte) pinNewByte.length;
        
        byte[] send = new byte[lengtNew+lengtOld+2];
        int offSet = 0;
        send[offSet] = lengtOld;
        offSet+=1;
        System.arraycopy(pinOldByte, 0, send, offSet, lengtOld);
        offSet+=lengtOld;
        send[offSet] = lengtNew;
        offSet+=1;
        System.arraycopy(pinNewByte, 0, send, offSet, lengtNew);
        try{
            
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            
            CardTerminal terminal = terminals.get(0);
            
            Card card = terminal.connect("*");
            
            CardChannel channel = card.getBasicChannel();
            
            ResponseAPDU answer = channel.transmit(new CommandAPDU(0xB0,0x44,0x00,0x00,send));
            
            message = answer.toString();
            switch (((message.split("="))[1]).toUpperCase()) {
                case "9000":
                    JOptionPane.showMessageDialog(null, "Cập nhật PIN thành công!");
                    return true;
                case "9C02":
                    JOptionPane.showMessageDialog(null, "Bạn đã nhập sai PIN");
                    return false;
                case "9C0C":
                    JOptionPane.showMessageDialog(null, "Bạn đã nhập sai quá số lần thử!Thẻ đã bị khoá");
                    return false;
                default:
                    return false;
            }
            
        }
        catch(Exception ex){
            return false;
        }
    }
    
    public void setUp(){
        
        try{
            
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            
            CardTerminal terminal = terminals.get(0);
            
            Card card = terminal.connect("*");
            
            CardChannel channel = card.getBasicChannel();
            
            ResponseAPDU answer = channel.transmit(new CommandAPDU(0xB0,0x2A,0x00,0x00));
            
        }
        catch(Exception ex){
            //return "Error";
        }
    
    }
    
    public boolean EditInformation(byte [] data){
        try{
            
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            
            CardTerminal terminal = terminals.get(0);
            
            Card card = terminal.connect("*");
            
            CardChannel channel0 = card.getBasicChannel();
            ResponseAPDU resetData = channel0.transmit(new CommandAPDU(0xB0,0x52,0x00,0x00));
            
            CardChannel channel = card.getBasicChannel();
            
            ResponseAPDU answer = channel.transmit(new CommandAPDU(0xB0,0x50,0x00,0x00,data));
            
            message = answer.toString();
            switch (((message.split("="))[1]).toUpperCase()) {
                case "9000":
                    JOptionPane.showMessageDialog(null, "Cập nhật thông tin thành công!");
                    return true;
                case "9C02":
                    JOptionPane.showMessageDialog(null, "Bạn đã nhập sai PIN");
                    return false;
                case "9C0C":
                    JOptionPane.showMessageDialog(null, "Bạn đã nhập sai quá số lần thử!Thẻ đã bị khoá");
                    return false;
                default:
                    return false;
            }
            
        }
        catch(Exception ex){
            return false;
        }
    }
    public boolean ReadInformation(){
        try{
            
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            
            CardTerminal terminal = terminals.get(0);
            
            Card card = terminal.connect("*");
            
            CardChannel channel = card.getBasicChannel();
            
            ResponseAPDU answerID = channel.transmit(new CommandAPDU(0xB0,0x51,0x01,0x00));
            strID = new String(answerID.getData());
            
            CardChannel channel1 = card.getBasicChannel();
            ResponseAPDU answerName = channel1.transmit(new CommandAPDU(0xB0,0x51,0x02,0x00));
            strName = new String(answerName.getData());
            
            CardChannel channel3 = card.getBasicChannel();
            ResponseAPDU answerDate = channel3.transmit(new CommandAPDU(0xB0,0x51,0x03,0x00));
            strDate = new String(answerDate.getData());
            
            CardChannel channel4 = card.getBasicChannel();
            ResponseAPDU answerPhone = channel4.transmit(new CommandAPDU(0xB0,0x51,0x04,0x00));
            strPhone = new String(answerPhone.getData());
            
            return true;
        }
        catch(Exception ex){
            return false;
        }
    }   
    public boolean UploadImage(File file, String type){
        try{
            
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            
            CardTerminal terminal = terminals.get(0);
            
            Card card = terminal.connect("*");
            
            CardChannel channel = card.getBasicChannel();
            
            BufferedImage bImage = ImageIO.read(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, type, bos);
            
            byte[] napanh = bos.toByteArray();
            
            int soLan = napanh.length / 249;
            
            String strsend = soLan + "S" + napanh.length % 249;
            
            byte[] send = strsend.getBytes();
            
            ResponseAPDU response = channel.transmit(new CommandAPDU(0xB0,0x54,0x00,0x01,send));
            String check = Integer.toHexString(response.getSW());
            
            if(check.equals("9000")){
                for(int i = 0;i<=soLan ;i++){
                    byte p1 = (byte) i;
                    int start = 0, end = 0;
                    start = i * 249;
                    if(i != soLan){
                        end = (i+1) *249;
                    }
                    else{
                        end = napanh.length;
                    }
                    byte[] slice = Arrays.copyOfRange(napanh, start, end);
                    response = channel.transmit(new CommandAPDU(0xB0,0x53,p1,0x01,slice));
                    String checkSlide = Integer.toHexString(response.getSW());
                    if(!checkSlide.equals("9000")){
                        return false;
                    }
                }
                return true;
            }
            return true;
        }
        catch(Exception ex){
            return false;
        }
    }
    public BufferedImage DownloadImage(){
        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            
            CardTerminal terminal = terminals.get(0);
            
            Card card = terminal.connect("*");
            
            CardChannel channel = card.getBasicChannel();
            
            int size = 0;
            ResponseAPDU answer = channel.transmit(new CommandAPDU(0xB0,0x56,0x01,0x01));
            String check = Integer.toHexString(answer.getSW());
            if(check.equals("9000")){
                byte[] sizeAnh = answer.getData();
                
                if(ConvertData.isByteArrayAllZero(sizeAnh)){
                    return null;
                }
                
                byte[] arrAnh = new byte[10000];
                String strSizeAnh = new String(sizeAnh);
                String[] outPut1 = strSizeAnh.split("S");
                
                int lan = Integer.parseInt(outPut1[0].replaceAll("\\D", ""));
                int du = Integer.parseInt(outPut1[1].replaceAll("\\D", ""));
                
                int count = size / 249;
                
                for(int j=0;j<=count;j++){
                    answer = channel.transmit(new CommandAPDU(0xB0,0x58,(byte)j,0x01));
                    String check1 = Integer.toHexString(answer.getSW());
                    if(check1.equals("9000")){
                        byte[] result = answer.getData();
                        int leng = 249;
                        if(j == count){
                            leng = size % 249;
                        }
                        System.arraycopy(result, 0, arrAnh, j*249, leng);
                    }
                }
                
                ByteArrayInputStream bais = new ByteArrayInputStream(arrAnh);
                try {
                    BufferedImage image  = ImageIO.read(bais);
                    return image;
                } catch (Exception e) {
                    System.err.println("Error image");
                }
            }
        } catch (Exception e) {
            System.err.println("error dowloadimage");
        }
        return null;
    }
    
}