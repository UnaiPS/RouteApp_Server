/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Jon Calvo Gaminde
 */
public class Hasher {
    
    public static String encrypt(String data) {
        MessageDigest messageDigest = null;
        try{
            messageDigest = MessageDigest.getInstance("MD5");
            byte dataBytes[] = data.getBytes();
            messageDigest.update(dataBytes);
        }catch(NoSuchAlgorithmException ex) {
            Logger.getLogger(Hasher.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
        }
        return DatatypeConverter.printHexBinary(messageDigest.digest());
    }
}
