/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.logging.Logger;
import javax.crypto.Cipher;

/**
 *
 * @author Unai Pérez Sánchez
 */
public class Decrypt {
  private Logger LOGGER = Logger.getLogger(Decrypt.class.getName());
    public byte[] descifrarTexto(byte[] mensaje) {
        KeyReader rsa = new KeyReader();
        
        byte[] decodedMessage = null;
        try {
                // Private key
                byte fileKey[] = rsa.fileReader("..\\keys\\Private.key");
                
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(fileKey);
                PrivateKey privateKey = keyFactory.generatePrivate(pKCS8EncodedKeySpec);

                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                decodedMessage = cipher.doFinal(mensaje);
        } catch (Exception e) {
                LOGGER.severe(e.getLocalizedMessage());
        }
        return decodedMessage;
    }
}
