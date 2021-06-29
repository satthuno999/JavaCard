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

/**
 *
 * @author Spark_Mac
 */
public class ConnectCard {
    public void connectapplet(){
        try{
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            
            CardTerminal terminal = terminals.get(0);
            
            Card card = terminal.connect("*");
            
            CardChannel channel = card.getBasicChannel();
            
            byte[] aid = {(byte)0x25,(byte)0x10,(byte)0x19,(byte)0x99,(byte)0x00,(byte)0x00,(byte)0x00};
            ResponseAPDU answer = channel.transmit(new CommandAPDU(0x00,0xA4,0x04,0x00,aid));
        }
        catch(Exception ex){
        }
    }
}
