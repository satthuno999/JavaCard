package project9;

import javacard.framework.*;
import javacard.framework.OwnerPIN;
import javacard.security.KeyBuilder;
import javacard.security.RandomData;
import javacardx.crypto.Cipher;
import javacard.security.AESKey;
import javacard.security.Key;
import javacard.security.KeyAgreement;
import javacard.security.KeyPair;
import javacard.security.RSAPrivateCrtKey;
import javacard.security.RSAPrivateKey;
import javacard.security.RSAPublicKey;

public class project9 extends Applet
{
	// CLA
    final static byte project9_CLA =(byte)0xB0;
    /** test v TESTCHECK  kim tra pin sau khi thay i*/
	private final static byte[] test = new byte[8];
	private final static byte[] TEST_CHECK = new byte[]{(byte)0x01,(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x05};
    //Kch thc m pin
	private final static byte PIN_MIN_SIZE = (byte) 4;
	private final static byte PIN_MAX_SIZE = (byte) 16;
	private final static byte[] PIN_INIT_VALUE={(byte)'S',(byte)'p',(byte)'a',(byte)'r',(byte)'k',(byte)'9',(byte)'9'};
	//Information
	public static byte[] OpData = new byte[60];
	public static byte lenData = (byte)0;
	
	public static byte[] OpID = new byte[256];
	public static byte lenID = (byte)0;
	public static byte[] OpNAME = new byte[256];
	public static byte lenNAME = (byte)0;
	public static byte[] OpDATE = new byte[256];
	public static byte lenDATE= (byte)0;
	public static byte[] OpPHONE = new byte[256];
	public static byte lenPHONE= (byte)0;
	
	public static byte[] OpImage,size;
	
	
	// Maximum number of keys handled by the Cardlet
	private final static byte MAX_NUM_KEYS = (byte) 16;
	//object ID
	private final static byte KEY_ACL_SIZE = (byte) 6;
	private final static byte ACL_READ = (byte) 0;
	private final static byte ACL_WRITE = (byte) 2;
	private final static byte ACL_USE = (byte) 4;
	// INS - Khi to
	private final static byte INS_SETUP = (byte) 0x2A;
	// Keys' use and management
	private final static byte INS_GEN_KEYPAIR = (byte) 0x30;
	//INS-PIN
	private final static byte INS_CREATE_PIN = (byte) 0x40;
	private final static byte INS_VERIFY_PIN = (byte) 0x42;
	private final static byte INS_CHANGE_PIN = (byte) 0x44;
	private final static byte INS_UNBLOCK_PIN = (byte) 0x46;
	
	private final static byte INS_CREATE_INFORMATION = (byte)0x47;
	private final static byte INS_OUT_INFORMATION = (byte)0x48;
	private final static byte OUT_ID = (byte)0x01;
	private final static byte OUT_NAME = (byte)0x02;
	private final static byte OUT_DATE = (byte)0x03;
	private final static byte OUT_PHONE = (byte)0x04;
	
	private final static byte INS_CREATE_IMAGE = (byte)0x52;
	private final static byte INS_CREATE_SIZEIMAGE = (byte)0x54;
	private final static byte INS_OUT_SIZEIMAGE = (byte)0x56;
	private final static byte INS_OUT_IMAGE = (byte)0x58;
	
	private final static byte INS_LOGOUT_ALL = (byte) 0x60;
	// Cipher Modes admitted in ComputeCrypt
	private final static byte OPT_DEFAULT = (byte) 0x00; // Use JC defaults
	private final static byte OPT_RSA_PUB_EXP = (byte) 0x01; // RSA: provide public exponent
	// Standard public ACL
	private static byte[] STD_PUBLIC_ACL;
	private static byte[] acl; // Temporary ACL
	/* Kim tra trng thi setup */
	private boolean setupDone = false;
	private byte create_object_ACL;
	private byte create_key_ACL;
	private byte create_pin_ACL;
	

	//PIN - m s nhn dng c nhn lu trong pins
	//PUK - m m kho c nhn lu trong unlk_pins
	private OwnerPIN pin, ublk_pin;
	/** ghi trng thi ng nhp*/
	private short logged_ids;
	
	// Key objects (allocated on demand)
	private Key[] keys;
	// Key ACLs
	private byte[] keyACLs;
	// Key Tries Left
	private byte[] keyTries;
	// Key iterator for ListKeys: it's an offset in the keys[] array.
	private byte key_it;
	//crypt
	private Cipher aesCipher;
	private AESKey aesKey;
	private static short KEY_SIZE = 32;
	//define
	private static byte LENGTH_BLOCK_AES = (byte)128;
	/*****
	*RSA**
	*****/
	private KeyPair[] keyPairs;
	// Offsets in buffer[] for key generation
	private final static short OFFSET_GENKEY_ALG = (short) (ISO7816.OFFSET_CDATA);
	private final static short OFFSET_GENKEY_SIZE = (short) (ISO7816.OFFSET_CDATA + 1);
	private final static short OFFSET_GENKEY_PRV_ACL = (short) (ISO7816.OFFSET_CDATA + 3);
	private final static short OFFSET_GENKEY_PUB_ACL = (short) (OFFSET_GENKEY_PRV_ACL + KEY_ACL_SIZE);
	private final static short OFFSET_GENKEY_OPTIONS = (short) (OFFSET_GENKEY_PUB_ACL + KEY_ACL_SIZE);
	private final static short OFFSET_GENKEY_RSA_PUB_EXP_LENGTH = (short) (OFFSET_GENKEY_OPTIONS + 1);
	private final static short OFFSET_GENKEY_RSA_PUB_EXP_VALUE = (short) (OFFSET_GENKEY_RSA_PUB_EXP_LENGTH + 2);
	/**
	* ERROR CONTROL*
	**/
	/** parameter passed invalid */
	private final static short SW_INVALID_PARAMETER = (short) 0x9C0F;
	/** returns error 9c0c when card is locked */
	private final static short SW_IDENTITY_BLOCKED = (short) 0x9C0C;
	/** tr li 9c02 khi nhp m pin sai */
	private final static short SW_AUTH_FAILED = (short) 0x9C02;
	/** tr li khi pin khng b kho*/
	private final static short SW_OPERATION_NOT_ALLOWED = (short) 0x9C03;
	/** Kim sot li */
	private final static short SW_INTERNAL_ERROR = (short) 0x9CFF;
	/** tr li 9c04 khi th cha c setup */
	private final static short SW_SETUP_NOT_DONE = (short) 0x9C04;
	/** Li P1*/
	private final static short SW_INCORRECT_P1 = (short) 0x9C10;
	/** Li P2*/
	private final static short SW_INCORRECT_P2 = (short) 0x9C11;
	/** Required operation was not authorized because of a lack of privileges */
	private final static short SW_UNAUTHORIZED = (short) 0x9C06;
	/** Algorithm specified is not correct */
	private final static short SW_INCORRECT_ALG = (short) 0x9C09;
	
	private project9(byte[] bArray, short bOffset, byte bLength){
		if (!CheckPINPolicy(PIN_INIT_VALUE, (short) 0, (byte) PIN_INIT_VALUE.length))
		    ISOException.throwIt(SW_INTERNAL_ERROR);
		OpImage = new byte[10000];
		size = new byte[7];

		/* Ci gi tr pin khi to*/
		pin = new OwnerPIN((byte) 3, (byte) PIN_INIT_VALUE.length);
		pin.update(PIN_INIT_VALUE, (short) 0, (byte) PIN_INIT_VALUE.length);
		
		register();
	}
	public boolean select() {
		LogOut();
		return true;
	}

	public void deselect() {
		LogOut();
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
		
		apdu.setIncomingAndReceive();
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
		case INS_UNBLOCK_PIN:
			UnblockPIN(apdu, buffer);
			break;
		case INS_LOGOUT_ALL:
			LogOut();
			break;
		case INS_CREATE_INFORMATION:
			SetupInformation(apdu,buffer);
			break;
		case INS_OUT_INFORMATION:
			OutputInformation(apdu,buffer);
			break;
		case INS_CREATE_IMAGE:
			SetupImage(apdu,buffer);
			break;
		case INS_CREATE_SIZEIMAGE:
			SetCount(apdu,buffer);
			break;
		case INS_OUT_SIZEIMAGE:
			OuputSize(apdu,buffer);
			break;
		case INS_OUT_IMAGE:
			OututImage(apdu,buffer);
			break;
		case INS_GEN_KEYPAIR:
			GenerateKeyPair(apdu, buffer);
			break;
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}
	/*SETUP*/
	private void setup(APDU apdu, byte[] buffer) {
		
		aesCipher = Cipher.getInstance(Cipher.ALG_AES_BLOCK_128_CBC_NOPAD, false);
        aesKey = (AESKey) KeyBuilder.buildKey(KeyBuilder.TYPE_AES, KeyBuilder.LENGTH_AES_128, false);
        byte[] keyBytes = JCSystem.makeTransientByteArray(KEY_SIZE, JCSystem.CLEAR_ON_DESELECT);
        try {
            RandomData rng = RandomData.getInstance(RandomData.ALG_SECURE_RANDOM);
            rng.generateData(keyBytes, (short) 0, KEY_SIZE);
            aesKey.setKey(keyBytes, (short) 0);
        } finally {
            Util.arrayFillNonAtomic(keyBytes, (short) 0, KEY_SIZE, (byte) 0);
        }
		
		setupDone = true;
	}
	private void CreatePIN(APDU apdu, byte[] buffer) {
		//send B0400003050409090909
		//send CLA:B0 INS:40 P1:00 P2:*max-tries-03* LC:*05* DATA:*pinsize-04|pincode-09090909*
		byte num_tries = buffer[ISO7816.OFFSET_P2];
		/* Kim tra ng nhp */
		// if ((create_pin_ACL == (byte) 0xFF)
				// || (((logged_ids & create_pin_ACL) == (short) 0x0000) && (create_pin_ACL != (byte) 0x00)))
			// ISOException.throwIt(SW_UNAUTHORIZED);
		short avail = Util.makeShort((byte) 0x00, buffer[ISO7816.OFFSET_LC]); // 05
		if (apdu.setIncomingAndReceive() != avail)
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		// ti thiu 1 byte s size pin v 1 byte pin code
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
			
		pin = new OwnerPIN(num_tries, PIN_MAX_SIZE);
		pin.update(buffer, (short) (ISO7816.OFFSET_CDATA + 1), pin_size);
		
		/*===CHECK PIN===*/
		if(pin.check(TEST_CHECK,(short)0,(byte)TEST_CHECK.length)){
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
		//pin_nb: th t ca pin trong mng cc pin
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
			LogOut();
			ISOException.throwIt(SW_AUTH_FAILED);
		}
		logged_ids = (short) (0x0010);
	}
	private void ChangePIN(APDU apdu, byte[] buffer) {
		//send 
		//send CLA:B0 INS:44 P1:00 P2:00 LC:*0B* DATA:*pinsize-04|pincode-09090909|pinsize-05|pincode-0102030405*
		//byte pin_nb = buffer[ISO7816.OFFSET_P1];
		if (pin == null)
			ISOException.throwIt(SW_INCORRECT_P1);
		if (buffer[ISO7816.OFFSET_P2] != (byte) 0x00)
			ISOException.throwIt(SW_INCORRECT_P2);
		short avail = Util.makeShort((byte) 0x00, buffer[ISO7816.OFFSET_LC]);
		if (apdu.setIncomingAndReceive() != avail)
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		// c ti thiu 1 byte pin-size 1 byte pin-code
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
			LogOut();
			ISOException.throwIt(SW_AUTH_FAILED);
		}
		pin.update(buffer, (short) (ISO7816.OFFSET_CDATA + 1 + pin_size + 1), new_pin_size);
		/*===CHECK PIN===*/
		if(pin.check(TEST_CHECK,(short)0,(byte)TEST_CHECK.length)){
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
		logged_ids = (short) (0x0010);
	}
	private void UnblockPIN(APDU apdu, byte[] buffer) {
		//byte pin_nb = buffer[ISO7816.OFFSET_P1];
		//OwnerPIN ublk_pin = ublk_pins[pin_nb];
		if (pin == null)
			ISOException.throwIt(SW_INCORRECT_P1);
		//if (ublk_pin == null)
			//ISOException.throwIt(SW_INTERNAL_ERROR);
		// Nu m PIN khng b chn, khng hp l
		if (pin.getTriesRemaining() != 0)
			ISOException.throwIt(SW_OPERATION_NOT_ALLOWED);
		if (buffer[ISO7816.OFFSET_P2] != 0x00)
			ISOException.throwIt(SW_INCORRECT_P2);
		short numBytes = Util.makeShort((byte) 0x00, buffer[ISO7816.OFFSET_LC]);
		
		if (numBytes != apdu.setIncomingAndReceive())
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		if (!CheckPINPolicy(buffer, ISO7816.OFFSET_CDATA, (byte) numBytes))
			ISOException.throwIt(SW_INVALID_PARAMETER);
		// if (!ublk_pin.check(buffer, ISO7816.OFFSET_CDATA, (byte) numBytes))
			// ISOException.throwIt(SW_AUTH_FAILED);
		//
		pin.resetAndUnblock();
	}
	private void SetupInformation(APDU apdu, byte[] buffer){
			lenData = buffer[ISO7816.OFFSET_LC];
			if(lenData>(byte) 0x3C){
				ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
			}
			
			Util.arrayCopyNonAtomic(buffer,ISO7816.OFFSET_CDATA,OpData,(short) 0x00,lenData);
					
			short flag = (short)0;
			byte[] objData = new byte[0];
			byte objDatalen = (short)0;
			for(short i=0;i<=lenData;i++){
				if((byte)(OpData[i]) == (byte)0x02){
					flag = (short)1;
					continue;
				}
				else if((byte)(OpData[i]) == (byte)0x03){
					flag = (short)0;
				};
				if(flag == (short)1){
					byte[] temp = new byte[objDatalen];
					if(objDatalen>0){
						for(short t=0;t<(short)temp.length;t++){
							temp[t] = objData[t];
						}
						
					}
					
					objData = new byte[objDatalen+1];
					if(objDatalen>0){
						for(short j=0;j<(short)objData.length;j++){
							if(j!=(short)temp.length){
								objData[j] = temp[j];
							}
						}
					}
					objData[objDatalen] = OpData[i];
					objDatalen++;
				}
				else if(flag == (short)0 && objDatalen != (byte)0){
					if(lenID == (byte)0){
						if(objDatalen > 6){
							ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
						}
						byte[] temp = encrypt(objData);
						Util.arrayCopyNonAtomic(temp,(short)0x00,OpID,(short)0x00,(short)(temp.length));
						lenID = (byte)(objDatalen);
						objData = new byte[0];
					}
					else if(lenNAME == (byte)0){
						if(objDatalen > 25){
							ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
						}
						byte[] temp = encrypt(objData);
						Util.arrayCopyNonAtomic(temp,(short)0x00,OpNAME,(short)0x00,(short)(temp.length));
						lenNAME = (byte)(objDatalen);
						objData = new byte[0];
					}
					else if(lenDATE == (byte)0){
						if(objDatalen > 10){
							ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
						}
						byte[] temp = encrypt(objData);
						Util.arrayCopyNonAtomic(temp,(short)0x00,OpDATE,(short)0x00,(byte)(temp.length));
						lenDATE = (byte)(objDatalen);
						objData = new byte[0];
					}
					else{
						if(objDatalen > 10){
							ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
						}
						byte[] temp = encrypt(objData);
						Util.arrayCopyNonAtomic(temp,(short)0x00,OpPHONE,(short)0x00,(byte)(temp.length));
						lenPHONE = (byte)(objDatalen);
					}
					objDatalen = (short)0;
				};
			}
	}
	private void OutputInformation(APDU apdu, byte[] buffer){
			if(buffer[ISO7816.OFFSET_P1] == OUT_ID){
				apdu.setOutgoing();
				apdu.setOutgoingLength(lenID);
				Util.arrayCopy(decrypt(OpID,lenID),(short)0,buffer,(short)0,lenID);
				apdu.sendBytes((short)0,lenID);
			}
			else if(buffer[ISO7816.OFFSET_P1] == OUT_NAME){
				apdu.setOutgoing();
				apdu.setOutgoingLength(lenNAME);
				Util.arrayCopy(decrypt(OpNAME,lenNAME),(short)0,buffer,(short)0,lenNAME);
				apdu.sendBytes((short)0,lenNAME);
			}
			else if(buffer[ISO7816.OFFSET_P1] == OUT_DATE){
				apdu.setOutgoing();
				apdu.setOutgoingLength(lenDATE);
				Util.arrayCopy(decrypt(OpDATE,lenDATE),(short)0,buffer,(short)0,lenDATE);
				apdu.sendBytes((short)0,lenDATE);
			}
			else if(buffer[ISO7816.OFFSET_P1] == OUT_PHONE){
				apdu.setOutgoing();
				apdu.setOutgoingLength(lenPHONE);
				Util.arrayCopy(decrypt(OpPHONE,lenPHONE),(short)0,buffer,(short)0,lenPHONE);
				apdu.sendBytes((short)0,lenPHONE);
			}
	}
	
	//Input Image
	private void SetupImage(APDU apdu, byte[] buffer){
		short p1 = (short)(buffer[ISO7816.OFFSET_P1]&0xff);
		short count = (short)(249 * p1);
		Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, OpImage, count, (short)249);
	}
	private void SetCount(APDU apdu, byte[] buffer){
		Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, size, (short)0, (short)7);
	}
	private void OuputSize(APDU apdu, byte[] buffer){
		Util.arrayCopy(size, (short)0, buffer, (short)0, (short)(size.length));
		apdu.setOutgoingAndSend((short)0,(short)7);
	}
	private void OututImage(APDU apdu, byte[] buffer){
		apdu.setOutgoing();
		short p = (short)(buffer[ISO7816.OFFSET_P1]&0xff);
		short count = (short)(249 * p);
		apdu.setOutgoingLength((short)249);
		apdu.sendBytesLong(OpImage, count, (short)249);
	}
	/*CHECK PIN POLICY*/
	private boolean CheckPINPolicy(byte[] pin_buffer, short pin_offset, byte pin_size) {
		if ((pin_size < PIN_MIN_SIZE) || (pin_size > PIN_MAX_SIZE))
			return false;
		return true;
	}
	private void LogOut() {
		logged_ids = (short) 0x0000; 
		pin.reset();
	}
	
	/*AES*/
    /**Encrypt*/
	private byte[] encrypt(byte[] encryptData) {
        aesCipher.init(aesKey, Cipher.MODE_ENCRYPT);
        short flag = (short) 1;
	    byte[] temp = new byte[256];
    	while(flag == (short)1){
    		for(short i=0;i<=(short) encryptData.length;i++){
    			if(i!=(short) encryptData.length){
					temp[i] = encryptData[i];
    			}
    			else{
	    			flag = (short) 0;
    			}
    		}
    	}
        // short newLength = addPadding(temp, (short) 0, (short) encryptData.length);
        byte[] dataEncrypted = JCSystem.makeTransientByteArray((short)256, JCSystem.CLEAR_ON_DESELECT);
        
        aesCipher.doFinal(temp, (short) 0 , (short)256, dataEncrypted, (short) 0x00);
        return dataEncrypted;
    }

    /**Decrypt
     * Decrypt data.
     */
    private byte[] decrypt(byte[] decryptData, byte length) {
        aesCipher.init(aesKey, Cipher.MODE_DECRYPT);
        byte[] dataDecrypted = JCSystem.makeTransientByteArray((short) 256, JCSystem.CLEAR_ON_DESELECT);
        aesCipher.doFinal(decryptData, (short) 0, (short) 256, dataDecrypted, (short) 0x00);
        // short newLength = removePadding(dataDecrypted, (short) length);
        return dataDecrypted;
    }
	 /**Add padding
     * Add padding for AES encryption.
     *
     * @param data
     * @param offset
     * @param length
     * @return
     */
    // private short addPadding(byte[] data, short offset, short length) {
        // data[(short) (offset + length++)] = (byte) 0x80;
        // while (length < 16 || (length % 16 != 0)) {
            // data[(short) (offset + length++)] = 0x00;
        // }
        // return length;
    // }

    /**remove padding
     * remove padding from decrypted result.
     *
     * @param data
     * @param length
     * @return
     */
    // private short removePadding(byte[] data, short length) {
        // while ((length != 0) && data[(short) (length - 1)] == (byte) 0x00) {
            // length--;
        // }
        // if (data[(short) (length - 1)] != (byte) 0x80) {
            // ISOException.throwIt(ISO7816.SW_DATA_INVALID);
        // }
        // length--;
        // return length;
    // }
    /************RSA
    *     RSA   *
    *************/
    private void GenerateKeyPair(APDU apdu, byte[] buffer) {
		short bytesLeft = Util.makeShort((byte) 0x00, buffer[ISO7816.OFFSET_LC]);
		if (bytesLeft != apdu.setIncomingAndReceive())
			ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		byte alg_id = buffer[OFFSET_GENKEY_ALG];
		switch (alg_id) {
			case KeyPair.ALG_RSA:
			case KeyPair.ALG_RSA_CRT:
				GenerateKeyPairRSA(buffer);
				break;
			case KeyPair.ALG_EC_FP:
				//GenerateKeyPairECFP(buffer);
				break;
			default:
				ISOException.throwIt(SW_INCORRECT_ALG);
		}
	}
    // Data has already been received 
	private void GenerateKeyPairRSA(byte[] buffer) {
		byte prv_key_nb = buffer[ISO7816.OFFSET_P1];
		if ((prv_key_nb < 0) || (prv_key_nb >= MAX_NUM_KEYS))
			ISOException.throwIt(SW_INCORRECT_P1);
		byte pub_key_nb = buffer[ISO7816.OFFSET_P2];
		if ((pub_key_nb < 0) || (pub_key_nb >= MAX_NUM_KEYS))
			ISOException.throwIt(SW_INCORRECT_P2);
		if (pub_key_nb == prv_key_nb)
			ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
		byte alg_id = buffer[OFFSET_GENKEY_ALG];
		short key_size = Util.getShort(buffer, OFFSET_GENKEY_SIZE);
		byte options = buffer[OFFSET_GENKEY_OPTIONS];
		RSAPublicKey pub_key = (RSAPublicKey) getKey(pub_key_nb, KeyBuilder.TYPE_RSA_PUBLIC, key_size);
		RSAPrivateKey prv_key = (RSAPrivateKey) getKey(prv_key_nb, alg_id == KeyPair.ALG_RSA ? KeyBuilder.TYPE_RSA_PRIVATE : KeyBuilder.TYPE_RSA_CRT_PRIVATE,	key_size);
		/* If we're going to overwrite a keyPair's contents, check ACL */
		if (pub_key.isInitialized() && !authorizeKeyOp(pub_key_nb,ACL_WRITE))
			ISOException.throwIt(SW_UNAUTHORIZED);
		if (prv_key.isInitialized() && !authorizeKeyOp(prv_key_nb,ACL_WRITE))
			ISOException.throwIt(SW_UNAUTHORIZED);
		/* Store private key ACL */
		Util.arrayCopy(buffer, OFFSET_GENKEY_PRV_ACL, keyACLs, (short) (prv_key_nb * KEY_ACL_SIZE), KEY_ACL_SIZE);
		/* Store public key ACL */
		Util.arrayCopy(buffer, OFFSET_GENKEY_PUB_ACL, keyACLs, (short) (pub_key_nb * KEY_ACL_SIZE), KEY_ACL_SIZE);
		switch (options) {
		case OPT_DEFAULT:
			if (pub_key.isInitialized())
				pub_key.clearKey();
			break;
		case OPT_RSA_PUB_EXP:
			short exp_length = Util.getShort(buffer, OFFSET_GENKEY_RSA_PUB_EXP_LENGTH);
			pub_key.setExponent(buffer, OFFSET_GENKEY_RSA_PUB_EXP_VALUE, exp_length);
			break;
		default:
			ISOException.throwIt(SW_INVALID_PARAMETER);
		}
		if ((keyPairs[pub_key_nb] == null) && (keyPairs[prv_key_nb] == null)) {
			keyPairs[pub_key_nb] = new KeyPair(pub_key, prv_key);
			keyPairs[prv_key_nb] = keyPairs[pub_key_nb];
		} else if (keyPairs[pub_key_nb] != keyPairs[prv_key_nb])
			ISOException.throwIt(SW_OPERATION_NOT_ALLOWED);
		KeyPair kp = keyPairs[pub_key_nb];
		if ((kp.getPublic() != pub_key) || (kp.getPrivate() != prv_key))
			// This should never happen according with this Applet policies
			ISOException.throwIt(SW_INTERNAL_ERROR);
		// We Rely on genKeyPair() to make all necessary checks about types
		kp.genKeyPair();
	}
	
	//get Key
	private Key getKey(byte key_nb, byte key_type, short key_size) {
		
		if (keys[key_nb] == null) {
			// We have to create the Key

			/* Check that Identity n.0 is logged */
			if ((create_key_ACL == (byte) 0xFF)
					|| (((logged_ids & create_key_ACL) == (short) 0x0000) && (create_key_ACL != (byte) 0x00)))
				ISOException.throwIt(SW_UNAUTHORIZED);
			
			keys[key_nb] = KeyBuilder.buildKey(key_type, key_size, false);
			
		} else {
			if ((keys[key_nb].getSize() != key_size) || (keys[key_nb].getType() != key_type))
				ISOException.throwIt(SW_OPERATION_NOT_ALLOWED);
		}
		return keys[key_nb];
	}
	/** Check from key_nb key ACL if an operation can be done */
	boolean authorizeKeyOp(byte key_nb, byte op) {
		short acl_offset = (short) (key_nb * KEY_ACL_SIZE+ op);
		short required_ids = Util.getShort(keyACLs, acl_offset);
		return ((required_ids != (short) 0xFFFF) && ((short) (required_ids & logged_ids) == required_ids));
	}
	
	/** Check from ACL if the corresponding key can be overwritten */
	boolean authorizeKeyOp(byte[] ACL, byte op) {
		short required_ids = Util.getShort(ACL, (short)op);
		return ((required_ids != (short) 0xFFFF) && ((short) (required_ids & logged_ids) == required_ids));
	}
}
