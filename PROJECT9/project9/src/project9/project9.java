package project9;

import javacard.framework.*;
import javacard.framework.OwnerPIN;

public class project9 extends Applet
{
	// code of CLA byte in the command APDU header
    final static byte project9_CLA =(byte)0xB0;
	private final static byte[] test = new byte[8];
	private final static byte[] TEST_CHECK = new byte[]{(byte)0x01,(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x05};
    //K�ch thc m� pin
	private final static byte PIN_MIN_SIZE = (byte) 4;
	private final static byte PIN_MAX_SIZE = (byte) 16;
	// S lng m� pin ti a
	private final static byte MAX_NUM_PINS = (byte) 8;
	private final static byte[] PIN_INIT_VALUE={(byte)'S',(byte)'p',(byte)'a',(byte)'r',(byte)'k',(byte)'9',(byte)'9'};
	//INS-PIN
	private final static byte INS_CREATE_PIN = (byte) 0x40;
	private final static byte INS_VERIFY_PIN = (byte) 0x42;
	private final static byte INS_CHANGE_PIN = (byte) 0x44;
	private final static byte INS_UNBLOCK_PIN = (byte) 0x46;
	private final static byte INS_LOGOUT_ALL = (byte) 0x60;
	/* For the setup function - should only be called once */
	private boolean setupDone = false;
	// Applet initialization
	private final static byte INS_SETUP = (byte) 0x2A;
	private OwnerPIN[] pins, ublk_pins;
	/*
	 * Logged identities: this is used for faster access control, so we don't
	 * have to ping each PIN object
	 */
	private short logged_ids;
	
	
	/** Invalid input parameter to command */
	private final static short SW_INVALID_PARAMETER = (short) 0x9C0F;
	private final static short SW_INVALID_PARAMETER_1 = (short) 0x9C1F;
	/** Operation has been blocked for security reason */
	private final static short SW_IDENTITY_BLOCKED = (short) 0x9C0C;
	/** Entered PIN is not correct */
	private final static short SW_AUTH_FAILED = (short) 0x9C02;
	/** For debugging purposes */
	private final static short SW_INTERNAL_ERROR = (short) 0x9CFF;
	/** Required setup is not not done */
	private final static short SW_SETUP_NOT_DONE = (short) 0x9C04;
	/** Li P1*/
	private final static short SW_INCORRECT_P1 = (short) 0x9C10;
	/** Li P2*/
	private final static short SW_INCORRECT_P2 = (short) 0x9C11;
	private project9(byte[] bArray, short bOffset, byte bLength){
		if (!CheckPINPolicy(PIN_INIT_VALUE, (short) 0, (byte) PIN_INIT_VALUE.length))
		    ISOException.throwIt(SW_INTERNAL_ERROR);

	    ublk_pins = new OwnerPIN[MAX_NUM_PINS];
		pins = new OwnerPIN[MAX_NUM_PINS];

		/* C�i gi� tr pin mc nh*/
		pins[0] = new OwnerPIN((byte) 3, (byte) PIN_INIT_VALUE.length);
		pins[0].update(PIN_INIT_VALUE, (short) 0, (byte) PIN_INIT_VALUE.length);
		
		// debug
		register();
	}
	public boolean select() {
		LogOutAll();
		return true;
	}

	public void deselect() {
		LogOutAll();
	}
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		project9 wal = new project9(bArray, bOffset, bLength);
	}

	public void process(APDU apdu)
	{
		if (selectingApplet())
			ISOException.throwIt(ISO7816.SW_NO_ERROR);

		byte[] buffer = apdu.getBuffer();
		if ((buffer[ISO7816.OFFSET_CLA] == 0) && (buffer[ISO7816.OFFSET_INS] == (byte) 0xA4))
			return;
		if (buffer[ISO7816.OFFSET_CLA] != project9_CLA)
			ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
		byte ins = buffer[ISO7816.OFFSET_INS];
		if (!setupDone && (ins != (byte) INS_SETUP))
			ISOException.throwIt(SW_SETUP_NOT_DONE);

		if (setupDone && (ins == (byte) INS_SETUP))
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		
		switch (ins)
		{
		case INS_SETUP:
			setup(apdu, buffer);
			break;
		case INS_CREATE_PIN:
			CreatePIN(apdu, buffer);
			break;
		case INS_VERIFY_PIN:
			VerifyPIN(apdu, buffer);
			break;
		case INS_CHANGE_PIN:
			ChangePIN(apdu, buffer);
			break;
		// case INS_UNBLOCK_PIN:
			// UnblockPIN(apdu, buffer);
			// break;
		case INS_LOGOUT_ALL:
			LogOutAll();
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}
	/*SETUP*/
	private void setup(APDU apdu, byte[] buffer) {
		// short bytesLeft = Util.makeShort((byte) 0x00, buffer[ISO7816.OFFSET_LC]);
		// if (bytesLeft != apdu.setIncomingAndReceive())
			// ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);

		// short base = (short) (ISO7816.OFFSET_CDATA);

		// byte numBytes = buffer[base++];
		// bytesLeft--;

		// OwnerPIN pin = pins[0];

		// if (!CheckPINPolicy(buffer, base, numBytes))
			// ISOException.throwIt(SW_INVALID_PARAMETER);

		// if (pin.getTriesRemaining() == (byte) 0x00)
			// ISOException.throwIt(SW_IDENTITY_BLOCKED);

		// if (!pin.check(buffer, base, numBytes))
			// ISOException.throwIt(SW_AUTH_FAILED);
			
		// base += numBytes;
		// bytesLeft-=numBytes;
		
		setupDone = true;
	}
	private void CreatePIN(APDU apdu, byte[] buffer) {
		//send B0 40 00 03 05 0409090909
		//send CLA:B0 INS:40 P1:00 P2:*max-tries-03* LC:*05* DATA:*pinsize-04|pincode-09090909*
		//byte pin_nb = buffer[ISO7816.OFFSET_P1];
		byte num_tries = buffer[ISO7816.OFFSET_P2];
		/* Kim tra ng nhp */
		// if ((create_pin_ACL == (byte) 0xFF)
				// || (((logged_ids & create_pin_ACL) == (short) 0x0000) && (create_pin_ACL != (byte) 0x00)))
			// ISOException.throwIt(SW_UNAUTHORIZED);
		// if ((pin_nb < 0) || (pin_nb >= MAX_NUM_PINS) || (pins[pin_nb] != null))
			// ISOException.throwIt(SW_INCORRECT_P1);
		short avail = Util.makeShort((byte) 0x00, buffer[ISO7816.OFFSET_LC]); // 05
		if (apdu.setIncomingAndReceive() != avail)
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		// ti thiu 1 byte s size pin v� 1 byte pin code
		if (avail < (short)2)
			ISOException.throwIt(SW_INVALID_PARAMETER);
		byte pin_size = buffer[ISO7816.OFFSET_CDATA]; // 04
		if (avail < (short) (1 + pin_size))
			ISOException.throwIt(SW_INVALID_PARAMETER);
		if (!CheckPINPolicy(buffer, (short) (ISO7816.OFFSET_CDATA + 1), pin_size))
			ISOException.throwIt(SW_INVALID_PARAMETER);
			
		////unblock
		// byte ucode_size = buffer[(short) (ISO7816.OFFSET_CDATA + 1 + pin_size)];
		// if (avail != (short) (1 + pin_size + 1 + ucode_size))
			// ISOException.throwIt(SW_INVALID_PARAMETER);
		// if (!CheckPINPolicy(buffer, (short) (ISO7816.OFFSET_CDATA + 1 + pin_size + 1), ucode_size))
			// ISOException.throwIt(SW_INVALID_PARAMETER);
			
		pins[0] = new OwnerPIN(num_tries, PIN_MAX_SIZE);
		pins[0].update(buffer, (short) (ISO7816.OFFSET_CDATA + 1), pin_size);
		
		/*===CHECK PIN===*/
		if(pins[0].check(TEST_CHECK,(short)0,(byte)TEST_CHECK.length)){
			test[1] = (byte)0x02;
		}
		else{
			test[1] = (byte)0x03;
		}
		apdu.setOutgoing();
		apdu.setOutgoingLength((short)test.length);
		Util.arrayCopy(test,(short)0,buffer,(short)0,(short)test.length);
		apdu.sendBytes((short)0,(short)test.length);
		/*===END CHECK PIN===*/
		
		////unblock
		//ublk_pins[pin_nb] = new OwnerPIN((byte) 3, PIN_MAX_SIZE);
		// Recycle variable pin_size
		//pin_size = (byte) (ISO7816.OFFSET_CDATA + 1 + pin_size + 1);
		//ublk_pins[pin_nb].update(buffer, pin_size, ucode_size);
	}
	private void VerifyPIN(APDU apdu, byte[] buffer) {
		//pin_nb: th t ca pin trong mng c�c pin
		// byte pin_nb = buffer[ISO7816.OFFSET_P1];
		
		byte pin_nb = (byte)0;
		if ((pin_nb < 0) || (pin_nb >= MAX_NUM_PINS))
			ISOException.throwIt(SW_INCORRECT_P1);
		OwnerPIN pin = pins[pin_nb];
		if (pin == null)
			ISOException.throwIt(SW_INCORRECT_P1);
		if (buffer[ISO7816.OFFSET_P2] != 0x00)
			ISOException.throwIt(SW_INCORRECT_P2);
		short numBytes = Util.makeShort((byte) 0x00, buffer[ISO7816.OFFSET_LC]);
		
		if (numBytes != apdu.setIncomingAndReceive())
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		if (!CheckPINPolicy(buffer, ISO7816.OFFSET_CDATA, (byte) numBytes))
			ISOException.throwIt(SW_INVALID_PARAMETER);
		if (pin.getTriesRemaining() == (byte) 0x00)
			ISOException.throwIt(SW_IDENTITY_BLOCKED);
		if (!pin.check(buffer, (short) ISO7816.OFFSET_CDATA, (byte) numBytes)) {
			LogoutIdentity(pin_nb);
			ISOException.throwIt(SW_AUTH_FAILED);
		}
		logged_ids |= (short) (0x0001 << pin_nb);
	}
	private void ChangePIN(APDU apdu, byte[] buffer) {
		//send B0 44 00 00 09 040909090903010203
		//send CLA:B0 INS:40 P1:00 P2:*max-tries-03* LC:*05* DATA:*pinsize-04|pincode-09090909*
		//byte pin_nb = buffer[ISO7816.OFFSET_P1];
		byte pin_nb = (byte)0;
		if ((pin_nb < 0) || (pin_nb >= MAX_NUM_PINS))
			ISOException.throwIt(SW_INCORRECT_P1);
		OwnerPIN pin = pins[pin_nb];
		if (pin == null)
			ISOException.throwIt(SW_INCORRECT_P1);
		if (buffer[ISO7816.OFFSET_P2] != (byte) 0x00)
			ISOException.throwIt(SW_INCORRECT_P2);
		short avail = Util.makeShort((byte) 0x00, buffer[ISO7816.OFFSET_LC]);
		if (apdu.setIncomingAndReceive() != avail)
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		// c� ti thiu 1 byte pin-size 1 byte pin-code
		if (avail < (short)4)
			ISOException.throwIt(SW_INVALID_PARAMETER);
		byte pin_size = buffer[ISO7816.OFFSET_CDATA];
		if (avail < (short) (1 + pin_size))
			ISOException.throwIt(SW_INVALID_PARAMETER);
		if (!CheckPINPolicy(buffer, (short) (ISO7816.OFFSET_CDATA + 1), pin_size))
			ISOException.throwIt(SW_INVALID_PARAMETER);
		byte new_pin_size = buffer[(short) (ISO7816.OFFSET_CDATA + 1 + pin_size)];
		if (avail < (short) (1 + pin_size + new_pin_size))
			ISOException.throwIt(SW_INVALID_PARAMETER);
		if (!CheckPINPolicy(buffer, (short) (ISO7816.OFFSET_CDATA + 1 + pin_size + 1), new_pin_size))
			ISOException.throwIt(SW_INVALID_PARAMETER);
		if (pin.getTriesRemaining() == (byte) 0x00)
			ISOException.throwIt(SW_IDENTITY_BLOCKED);
		if (!pin.check(buffer, (short) (ISO7816.OFFSET_CDATA + 1), pin_size)) {
			LogoutIdentity(pin_nb);
			ISOException.throwIt(SW_AUTH_FAILED);
		}
		pin.update(buffer, (short) (ISO7816.OFFSET_CDATA + 1 + pin_size + 1), new_pin_size);
		/*===CHECK PIN===*/
		if(pins[0].check(TEST_CHECK,(short)0,(byte)TEST_CHECK.length)){
			test[1] = (byte)0x02;
		}
		else{
			test[1] = (byte)0x03;
		}
		apdu.setOutgoing();
		apdu.setOutgoingLength((short)test.length);
		Util.arrayCopy(test,(short)0,buffer,(short)0,(short)test.length);
		apdu.sendBytes((short)0,(short)test.length);
		/*===END CHECK PIN===*/
		logged_ids &= (short) ((short) 0xFFFF ^ (0x01 << pin_nb));
	}
	/*CHECK PIN POLICY*/
	private boolean CheckPINPolicy(byte[] pin_buffer, short pin_offset, byte pin_size) {
		if ((pin_size < PIN_MIN_SIZE) || (pin_size > PIN_MAX_SIZE))
			return false;
		return true;
	}
	private void LogOutAll() {
		logged_ids = (short) 0x0000; // Nobody is logged in
		byte i;
		for (i = (byte) 0; i < MAX_NUM_PINS; i++)
			if (pins[i] != null)
				pins[i].reset();
	}
	/*ng xut*/
	private void LogoutIdentity(byte id_nb) {
		logged_ids &= (short) ~(0x0001 << id_nb);
	}
}
