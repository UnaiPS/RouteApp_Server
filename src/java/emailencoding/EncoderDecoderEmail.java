package emailencoding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.ResourceBundle;
import javax.crypto.BadPaddingException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * The class that encrypts and decrypts the email credentials.
 * @author Daira Eguzkiza
 */
public class EncoderDecoderEmail {

	private static byte[] salt = "aj375hvngh!!8?JQ".getBytes(); // Exactly 16 bytes

	/**
	 * Encrypts the text with AES, CBC mode and PKCS5Padding padding and returns it
	 * 
	 * @param clave  The user password
	 * @param mensaje he message to encrypt
         * @param tipo The kind of message, 1:email 2:password
         * @return The encrypted message
	 */
	public String cifrarTexto(String clave, String mensaje, int tipo) {
		String cifrado = null;
		KeySpec keySpec = null;
		SecretKeyFactory secretKeyFactory = null;
		try {

                        // We create a SecretKey using the password + salt
			keySpec = new PBEKeySpec(clave.toCharArray(), salt, 65536, 128); // AES-128
			secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
			SecretKey privateKey = new SecretKeySpec(key, 0, key.length, "AES");

                        // We create a Cipher with the algorithm we gonna use.
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] encodedMessage = cipher.doFinal(mensaje.getBytes()); // Mensaje cifrado !!!
			byte[] iv = cipher.getIV(); // vector de inicialización por modo CBC

			
                        // We save the codified message depending if its an email o a password: IV (16 bytes) + Mensaje
			byte[] combined = concatArrays(iv, encodedMessage);
                        String path = null;
                        if(tipo == 1){
                            path = "../emailencoding/Email.dat";

                        }else{
                            path = "../emailencoding/ContraEmail.dat";
                        }
                        fileWriter(this.getClass().getResource(path).getFile(),combined);
			cifrado = new String(encodedMessage);

		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return cifrado;
	}

	/**
	 * Decrypts the text with AES, CBC mode and PKCS5Padding padding and returns it
	 * 
	 * @param clave The user password
         * @param tipo The kind of message, 1:email 2:password
         * @return The decrypted mssage.
	 */
	public String descifrarTexto(String clave, int tipo) {
		String desencriptado = null;
                byte[] fileContent;
                String path = null;
		if(tipo == 1){
                    path = "../emailencoding/Email.dat";

                }else{
                    path = "../emailencoding/ContraEmail.dat";
                }
                fileContent = fileReader(this.getClass().getResource(path).getFile());
		KeySpec keySpec;
		SecretKeyFactory secretKeyFactory = null;
		try {
			// We create a SecretKey using the password + salt
			keySpec = new PBEKeySpec(clave.toCharArray(), salt, 65536, 128); // AES-128
			secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
			SecretKey privateKey = new SecretKeySpec(key, 0, key.length, "AES");

			// We create a Cipher with the algorithm we gonna use.
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec ivParam = new IvParameterSpec(Arrays.copyOfRange(fileContent, 0, 16)); // La IV está aquí
			cipher.init(Cipher.DECRYPT_MODE, privateKey, ivParam);
			byte[] decodedMessage = cipher.doFinal(Arrays.copyOfRange(fileContent, 16, fileContent.length));
			desencriptado = new String(decodedMessage);
		} catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return desencriptado;
	}

	/**
	 * Concats two arrays.
	 * 
	 * @param array1 One array.
	 * @param array2 The other array.
	 * @return The concat of both arrays.
	 */
	private byte[] concatArrays(byte[] array1, byte[] array2) {
		byte[] ret = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, ret, 0, array1.length);
		System.arraycopy(array2, 0, ret, array1.length, array2.length);
		return ret;
	}

	/**
	 * Writes a file.
	 * 
	 * @param path Path of the file
	 * @param text Text to write
	 */
	private void fileWriter(String path, byte[] text) {
		try (FileOutputStream fos = new FileOutputStream(path)) {
			fos.write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads a file
	 * 
	 * @param path Path of the file
	 * @return Readed text
	 */
	private byte[] fileReader(String path) {
		byte ret[] = null;
		File file = new File(path);
		try {
			ret = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * Main method of the class, used one time.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
                ResourceBundle properties = ResourceBundle.getBundle("emailencoding.prop");
                String clave = properties.getString("clave");
		EncoderDecoderEmail ejemploAES = new EncoderDecoderEmail();
		String mensajeCifrado1 = ejemploAES.cifrarTexto(clave, "euskocode", 1);
                String mensajeCifrado2 = ejemploAES.cifrarTexto(clave, "abcd*1234", 2);
	}
}