package project9;

import javacard.framework.*;
import javacard.framework.ISOException;

public class projectPIN extends Applet
{
	final static byte Pin_CLA = (byte)0x00;
	
	// Applet initialization
	private final static byte INS_SETUP = (byte) 0x2A;
	
	// External authentication
	private final static byte INS_CREATE_PIN = (byte) 0x40;
	private final static byte INS_VERIFY_PIN = (byte) 0x42;
	private final static byte INS_CHANGE_PIN = (byte) 0x44;
	private final static byte INS_UNBLOCK_PIN = (byte) 0x46;
	private final static byte INS_LOGOUT_ALL = (byte) 0x60;
	
	/*DEFINE*/
	// Minium PIN size
	private final static byte PIN_MIN_SIZE = (byte) 4;
	// Maximum PIN size
	private final static byte PIN_MAX_SIZE = (byte) 16;
	
	
	//ERROR
	private byte SW_UNAUTHORIZED = (byte)0x6910;
	private byte SW_INCORRECT_P1 = (byte)0x6911;
	private byte SW_INVALID_PARAMETER = (byte)0x6920;
	private short CreatePIN(APDU apdu,byte[] buffer)
	{
		if(!PIN[0].isValidated())
			ISOException.throwIt(SW_UNAUTHORIZED);
			
		byte pin_nb = buffer[ISO7816.OFFSET_P1];
		byte pin_tries = buffer[ISO7816.OFFSET_P2];
		
		if((pin_nb<0)||(pin_nb >= MAX_NUM_PINS)||(PIN[pin_nb]!=null))
			ISOException.throwIt(SW_INCORRECT_P1);
			
		short bytesLeft = Util.makeShort((byte) 0x00, buffer[ISO7816.OFFSET_LC]);
		
		if(bytesLeft<4)
			ISOException.throwIt(SW_INVALID_PARAMETER);
		byte pin_size = buffer[ISO7816.OFFSET_CDATA];
		if(bytesLeft<(short)(1+ pin_size + 1))
			ISOException.throwIt(SW_INVALID_PARAMETER);
		if(!)

	}
	
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new projectPIN().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}

	public void process(APDU apdu)
	{
		if (selectingApplet())
		{
			return;
		}

		byte[] buf = apdu.getBuffer();
		switch (buf[ISO7816.OFFSET_INS])
		{
		case (byte)0x00:
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
