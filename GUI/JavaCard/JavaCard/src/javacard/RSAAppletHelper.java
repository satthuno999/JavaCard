/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacard;

import drivercardhostapp.commons.constants.APPLET_CONSTANTS;
import drivercardhostapp.commons.constants.AUTH_CONSTANTS;
import drivercardhostapp.commons.constants.INFO_CONSTANTS;
import drivercardhostapp.commons.constants.ISO7816;
import drivercardhostapp.commons.constants.RSA_CONSTANTS;
import drivercardhostapp.commons.utils.ConvertData;
import drivercardhostapp.commons.utils.RSAData;
import drivercardhostapp.model.Person;
import java.security.PublicKey;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

/**
 *
 * @author Admin
 */
public class RSAAppletHelper {

    private Card card;
    private TerminalFactory factory;
    private final CardChannel channel;
    private CardTerminal terminal;
    private List<CardTerminal> terminals;

    private static RSAAppletHelper instance;

    private RSAAppletHelper(CardChannel channel) {
        this.channel = channel;
    }

    public static RSAAppletHelper getInstance(CardChannel channel) {
        if (instance == null) {
            instance = new RSAAppletHelper(channel);

        }
        return instance;
    }
    
    public byte[] requestSign(byte[] data) throws CardException {
        try {
            if (selectRSAApplet()) {
                ResponseAPDU response = sendAPDU(
                        APPLET_CONSTANTS.CLA, RSA_CONSTANTS.INS_SIGN, 0, 0, data);
                String check = Integer.toHexString(response.getSW());
                if (check.equals(ISO7816.SW_NO_ERROR)) {
                      return response.getData();
                }
            }
        } catch (CardException ex) {
            Logger.getLogger(RSAAppletHelper.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return null;
    }

    public PublicKey getPublicKey() throws CardException {
        try {
            if (selectRSAApplet()) {
                ResponseAPDU response = sendAPDU(
                        APPLET_CONSTANTS.CLA, RSA_CONSTANTS.INS_GET_PUB_MODULUS, 0, 0, null);
                String check = Integer.toHexString(response.getSW());
                if (check.equals(ISO7816.SW_NO_ERROR)) {
                    byte[] modulusBytes = response.getData();
                    ResponseAPDU response1 = sendAPDU(
                            APPLET_CONSTANTS.CLA, RSA_CONSTANTS.INS_GET_PUB_EXPONENT, 0, 0, null);
                    String check1 = Integer.toHexString(response1.getSW());
                    if (check1.equals(ISO7816.SW_NO_ERROR)) {
                        byte[] exponentBytes = response1.getData();
                        PublicKey publicKey= RSAData.initPublicKey(modulusBytes, exponentBytes);
                        if(publicKey!=null){
                            RSAData.savePublicKey(publicKey);
                            return publicKey;
                        }
                    }
                }
            }
        } catch (CardException ex) {
            Logger.getLogger(RSAAppletHelper.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return null;
    }

    public boolean selectRSAApplet() throws CardException {
        try {
            System.out.println();
            System.out.println("||===================||");
            System.out.println("||   SELECT RSA...  ||");
            System.out.println("||===================||");
            ResponseAPDU response = selectApplet(APPLET_CONSTANTS.AID_RSA_APPLET);
            String check = Integer.toHexString(response.getSW());

            if (check.equals(ISO7816.SW_NO_ERROR)) {
                byte[] baCardUid = response.getData();

                System.out.print("Card UID = 0x");
                for (int i = 0; i < baCardUid.length; i++) {
                    System.out.printf("%02X ", baCardUid[i]);
                }
                System.out.println("");
                return true;
            }
        } catch (CardException ex) {
            Logger.getLogger(HostAppHelper.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Lỗi select info applet");
            throw ex;
        }
        System.out.println("Lỗi khởi tạo length");
        return false;
    }

    public ResponseAPDU sendAPDU(
            int cla, int ins, int p1, int p2, byte[] data
    ) throws CardException {
        if (channel == null) {
            throw new CardException(ISO7816.SW_UNKNOWN);
        }
        return channel.transmit(new CommandAPDU(
                cla, ins, p1, p2, data));
    }

    // select
    private ResponseAPDU selectApplet(byte[] aid) throws CardException {
        ResponseAPDU response = channel.transmit(new CommandAPDU(
                0x00, (byte) 0xA4,
                0x04, 0x00,
                aid)
        );
        return response;
    }
}
