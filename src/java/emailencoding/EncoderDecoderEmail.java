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
 *
 * @author Daira Eguzkiza
 */
public class EncoderDecoderEmail {

	private static byte[] salt = "aj375hvngh!!8?JQ".getBytes(); // Exactamente 16 bytes

	/**
	 * Cifra un texto con AES, modo CBC y padding PKCS5Padding (sim�trica) y lo
	 * retorna
	 * 
	 * @param clave   La clave del usuario
	 * @param mensaje El mensaje a cifrar
         * @param tipo El mensaje que quiera leer; 1:email, 2:contraseña de éste.
         * @return el mensaje cifrado
	 */
	public String cifrarTexto(String clave, String mensaje, int tipo) {
		String cifrado = null;
		KeySpec keySpec = null;
		SecretKeyFactory secretKeyFactory = null;
		try {

			// Creamos un SecretKey usando la clave + salt
			keySpec = new PBEKeySpec(clave.toCharArray(), salt, 65536, 128); // AES-128
			secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
			SecretKey privateKey = new SecretKeySpec(key, 0, key.length, "AES");

			// Creamos un Cipher con el algoritmos que vamos a usar
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] encodedMessage = cipher.doFinal(mensaje.getBytes()); // Mensaje cifrado !!!
			byte[] iv = cipher.getIV(); // vector de inicialización por modo CBC

			// Guardamos el mensaje codificado en el archivo correspondiente dependiendo
                        // de si es email o contraseña: IV (16 bytes) + Mensaje
			byte[] combined = concatArrays(iv, encodedMessage);
                        String path = null;
                        if(tipo == 1){
                            path = "..\\emailencoding\\Email.dat";

                        }else{
                            path = "..\\emailencoding\\ContraEmail.dat";
                        }
                        fileWriter(this.getClass().getResource(path).getFile(),combined);
			cifrado = new String(encodedMessage);

		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return cifrado;
	}

	/**
	 * Descifra un texto con AES, modo CBC y padding PKCS5Padding (sim�trica) y lo
	 * retorna
	 * 
	 * @param clave La clave del usuario
         * @param tipo Lo que se quiere desencriptar; 1:email, 2:contraseña de éste.
         * @return el mensaje una vez sido desencriptado.
	 */
	public String descifrarTexto(String clave, int tipo) {
		String desencriptado = null;
                byte[] fileContent;
                String path = null;
		if(tipo == 1){
                    path = "..\\emailencoding\\Email.dat";

                }else{
                    path = "..\\emailencoding\\ContraEmail.dat";
                }
                fileContent = fileReader(this.getClass().getResource(path).getFile());
		KeySpec keySpec;
		SecretKeyFactory secretKeyFactory = null;
		try {
			// Creamos un SecretKey usando la clave + salt
			keySpec = new PBEKeySpec(clave.toCharArray(), salt, 65536, 128); // AES-128
			secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
			SecretKey privateKey = new SecretKeySpec(key, 0, key.length, "AES");

			// Creamos un Cipher con el algoritmos que vamos a usar
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
	 * Retorna una concatenación de ambos arrays
	 * 
	 * @param array1
	 * @param array2
	 * @return Concatenación de ambos arrays
	 */
	private byte[] concatArrays(byte[] array1, byte[] array2) {
		byte[] ret = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, ret, 0, array1.length);
		System.arraycopy(array2, 0, ret, array1.length, array2.length);
		return ret;
	}

	/**
	 * Escribe un fichero
	 * 
	 * @param path Path del fichero
	 * @param text Texto a escibir
	 */
	private void fileWriter(String path, byte[] text) {
		try (FileOutputStream fos = new FileOutputStream(path)) {
			fos.write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retorna el contenido de un fichero
	 * 
	 * @param path Path del fichero
	 * @return El texto del fichero
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
	 * Main de la clase usada una sola vez para crear los archivos con las credenciales encriptadas.
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