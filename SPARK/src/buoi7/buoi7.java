package buoi7;

import javacard.framework.*;


public class buoi7 extends Applet
{
	private static final byte CLA = 0x00;
	public static byte[] OpA = new byte[8];
	public static byte lenA;
	
	public static void outputA(APDU apdu, byte buf[]){
		apdu.setOutgoing();
		apdu.setOutgoingLength(lenA);
		Util.arrayCopy(OpA,(short)0,buf,(short)0,lenA);
		apdu.sendBytes((short)0,lenA);
	}
	
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new buoi7().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}

	public void process(APDU apdu)
	{
		if (selectingApplet())
		{
			return;
		}

		byte[] buf = apdu.getBuffer();
		apdu.setIncomingAndReceive();
		switch (buf[ISO7816.OFFSET_INS])
		{
		case (byte)0x00:
			Util.arrayCopyNonAtomic(buf,ISO7816.OFFSET_CDATA,OpA,(short) 0x00,buf[ISO7816.OFFSET_LC]);
			lenA = buf[ISO7816.OFFSET_LC];
			break;
		case (byte)0x01:
			outputA(apdu,buf);
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
