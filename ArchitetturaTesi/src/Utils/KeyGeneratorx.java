package Utils;
import java.security.*;

import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.io.*;
import java.security.SecureRandomSpi;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.print.attribute.HashDocAttributeSet;
import javax.xml.bind.DatatypeConverter;


public class KeyGeneratorx {
	private Key simmetricKey;
	private Key publickey;
	private Key privatekey;

	public static void main(String[] argv) throws Exception {
		KeyGeneratorx k = new KeyGeneratorx();
		k.AESsymmetricKey();
		k.RSAasymmetricKey();
		System.out.println(k.getSimmetricKey().getEncoded().length);
		System.out.println(k.getPublickey().getEncoded().length);
		System.out.println(k.getPrivatekey().getEncoded().length);
	}
	
	//metodo che prende in input come array di byte un oggetto, un digest firmato 
	//con una chiave privata e la chiave pubblica con cui decrittarlo
	//restituisce un boolean se la funzione di hash applicatta all'oggetto
	//è uguale alla firma ricevuta decrittata con la chiave passata come parametro
	public static boolean Signature(byte[] object, byte[] firma, Key key) throws NoSuchAlgorithmException, NoSuchProviderException {
		byte[] digestRicevuto = RSADecrypt(firma, key);
		byte[] mioDigest = getMessageDigestFromString(object);
		String mioDigestBase64 = DatatypeConverter.printBase64Binary(mioDigest);
		String digestRicevutoBase64 = DatatypeConverter.printBase64Binary(digestRicevuto);
		return mioDigestBase64.equals(digestRicevutoBase64);	
	}
	
	
	//trasforma una chiave simmetrica aes in array di byte
	public static byte[] saveAESkey(Key k) {
		return k.getEncoded();
	}
	
	
	
	//trasforma un'array di byte in una chiave aes
	public static SecretKey loadAESkey(byte[] key) {
		return new SecretKeySpec(key, 0, key.length, "AES");
	}
	
	//metodo per convertire una stringa in una chiave privata RSA
	public static Key loadPrivateKey(String key64) throws GeneralSecurityException {
	    //byte[] clear = base64Decode(key64);
		byte[] clear = DatatypeConverter.parseBase64Binary(key64);
	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
	    KeyFactory fact = KeyFactory.getInstance("RSA");
	    PrivateKey priv = fact.generatePrivate(keySpec);
	    Arrays.fill(clear, (byte) 0);
	    return priv;
	}
	
	//metodo per convertire una chiave privata RSA in una stringa
	public static String savePrivateKey(Key priv) throws GeneralSecurityException {
	    KeyFactory fact = KeyFactory.getInstance("RSA");
	    PKCS8EncodedKeySpec spec = fact.getKeySpec(priv, PKCS8EncodedKeySpec.class);
	    byte[] packed = spec.getEncoded();
	    //String key64 = base64Encode(packed);
	    String key64 = DatatypeConverter.printBase64Binary(packed);

	    Arrays.fill(packed, (byte) 0);
	    return key64;
	}
	
	//metodo per convertire una stringa in una chiave pubblica RSA
	public static Key loadPublicKey(String stored) throws GeneralSecurityException {
		byte[] data = DatatypeConverter.parseBase64Binary(stored);
	    X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
	    KeyFactory fact = KeyFactory.getInstance("RSA");
	    return fact.generatePublic(spec);
	}
	
	//metodo per convertire una chiave pubblica RSA in una stringa
	public static String savePublicKey(Key publ) throws GeneralSecurityException {
	      return DatatypeConverter.printBase64Binary(publ.getEncoded());
	}
	

	//metodo per generare un hash da un file passando come parametro
	//il path del fine da cui generare l'hash. 
	public static byte[] getMessageDigestFromFile(String filepath) throws IOException, NoSuchAlgorithmException, NoSuchProviderException {
		File file = new File(filepath);
		byte[] buffer = new byte[(int) file.length()];
		FileInputStream fis = new FileInputStream(file);
		fis.read(buffer);
		fis.close();
		MessageDigest md = MessageDigest.getInstance("SHA-384", "SUN");
		md.update(buffer);
		byte[] digest = md.digest();
		return digest;	
	}

	//metodo per generare l'hash di un array di byte
	public static byte[] getMessageDigestFromString(byte[] s) throws NoSuchAlgorithmException, NoSuchProviderException {
		MessageDigest md = MessageDigest.getInstance("SHA-384", "SUN");
		md.update(s);
		byte[] digest = md.digest();
		return digest;		
	}

	//metodo per settare localmente chiave pubblica e privata RSA
	public void RSAasymmetricKey() throws Exception {
		String algorithm = "RSA";
		KeyPair keyPair = KeyPairGenerator.getInstance(algorithm).generateKeyPair();
		this.setPublickey(keyPair.getPublic());
		this.setPrivatekey(keyPair.getPrivate());
	}

	//metodo per settare localmente chiave simmetrica aes
	public void AESsymmetricKey() throws Exception   {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		SecretKey key = keyGen.generateKey();
		this.setChiaveG(key); 
	}

	//metodo che restituisce una chiave simmetrica AES
	public static Key generateAESSimmetricKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		SecretKey key = keyGen.generateKey();
		return key;		
	}
	
	//metodo per effettuare una crittazione con RSA
	public static byte[] RSAEncrypt(byte[] pInput, Key rsaKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, rsaKey);
			byte[] encrypted = cipher.doFinal(pInput);
			return DatatypeConverter.printBase64Binary(encrypted).getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//metodo per effettuare una decrittazione tramite RSA
	public static byte[] RSADecrypt(byte[] pInput, Key rsaKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, rsaKey);
			byte[] encrypted = DatatypeConverter.parseBase64Binary(new String(pInput));
			return cipher.doFinal(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//metodo per effettuare una crittazione tramite AES
	public static byte[] AESEncrypt(byte[] pInput, Key aesKey) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encrypted = cipher.doFinal(pInput);
			return encrypted;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//metodo per effettuare una decrittazione tramite AES
	public static byte[] AESDecrypt(byte[] pInput, Key aesKey) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			byte[] decrypted = cipher.doFinal(pInput); 
			return decrypted;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//getter and setter

	public Key getSimmetricKey() {
		return simmetricKey;
	}
	public void setChiaveG(Key simmetricKey) {
		this.simmetricKey = simmetricKey;
	}

	public Key getPublickey() {
		return publickey;
	}

	private void setPublickey(PublicKey publickey) {
		this.publickey = publickey;
	}

	public Key getPrivatekey() {
		return privatekey;
	}

	private void setPrivatekey(PrivateKey privatekey) {
		this.privatekey = privatekey;
	}

}
