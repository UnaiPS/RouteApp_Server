/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Jon Calvo Gaminde
 */
public class Hasher {
    
    public static String encrypt(String data) throws NoSuchAlgorithmException {
        MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance("MD5");
        byte dataBytes[] = data.getBytes();
        messageDigest.update(dataBytes);
        return DatatypeConverter.printHexBinary(messageDigest.digest());
    }
}
