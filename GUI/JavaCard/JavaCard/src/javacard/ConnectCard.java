/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacard;

import javax.smartcardio.TerminalFactory;
import jdk.nashorn.internal.ir.Terminal;

/**
 *
 * @author Spark_Mac
 */
public class ConnectCard {
    public void connectapplet(){
        try{
            TerminalFactory factory = TerminalFactory.getDefault();
        }
        catch(Exception ex){
        }
    }
}
