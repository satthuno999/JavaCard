package buoi7;

import javacard.framework.*;
import buoi7.buoi7;

public class applet1 extends Applet
{
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new applet1().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
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
			buoi7.outputA(apdu, buf);
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

}
