/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacard;

import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import jdk.nashorn.internal.ir.Terminal;
import java.util.List;
import javax.smartcardio.*;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 *
 * @author Spark_Mac
 */
public class ConnectCard {
    public byte [] data;
    public String message;
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
            switch ((message.split("="))[1]) {
                case "9000":
                    return true;
                case "9C0F":
                    JOptionPane.showMessageDialog(null, "Bạn đã nhập sai PIN");
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
}
