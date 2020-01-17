/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import emailencoding.EncoderDecoderEmail;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
/**
 *
 * @author Daira Eguzkiza
 */
public class EmailSender {
    // DNS Host + SMTP Port
    private String smtp_host = null;
    private int smtp_port = 0;
    private String fileToRead = "";
    
    // Default DNS Host + port
    private static final String DEFAULT_SMTP_HOST = "posta.tartanga.eus";
    private static final int DEFAULT_SMTP_PORT = 25;
        
    public EmailSender(String host, String port) {
        this.smtp_host = (host == null ? DEFAULT_SMTP_HOST : host);
        this.smtp_port = (port == null ? DEFAULT_SMTP_PORT : Integer.parseInt(port));
    }
    
    public EmailSender() {
       
    }
    /**
     * Sends the email with all the data entered
     * @param email the email we want to send the email to
     * @param contrasena the new password we want to send the user.
     * @return the integer 200 if everything's gone right
     * @throws MessagingException 
     */
    public int sendEmail(String email, String contrasena, int type) throws MessagingException{
        String subject = null;
        if(type==0) {
            fileToRead= "..\\mediafiles\\email.html";
            subject = "Account Recovery";
        } else {
            fileToRead= "..\\mediafiles\\emailchangeaccount.html";
            subject = "Email Confirmation";
        }
        fileToRead = this.getClass().getResource(fileToRead).getFile();
        try{
            ResourceBundle prop = ResourceBundle.getBundle("emailencoding.prop");
            String clave = prop.getString("clave");
            EncoderDecoderEmail decoder = new EncoderDecoderEmail();
            String user=decoder.descifrarTexto(clave, 1);
            String pass=decoder.descifrarTexto(clave, 2);
         // Mail properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtp_host);
	properties.put("mail.smtp.port", smtp_port);
        properties.put("mail.smtp.ssl.trust", smtp_host);
        properties.put("mail.imap.partialfetch", false);

        // Authenticator knows how to obtain authentication for a network connection.
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        // MIME message to be sent
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(user)); // Ej: emisor@gmail.com
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email)); // Ej: receptor@gmail.com
        message.setSubject(subject); // Asunto del mensaje

        // A mail can have several parts
        Multipart multipart = new MimeMultipart();

        // A message part (the message, but can be also a File, etc...)
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        String cuerpo = this.emailBody(contrasena);
        mimeBodyPart.setContent(cuerpo, "text/html");
        multipart.addBodyPart(mimeBodyPart);

        // Adding up the parts to the MIME message
        message.setContent(multipart);

        // And here it goes...
        Transport.send(message);
        }catch(Exception e){
            return 500;
        }
        return 200;
    }
    
    /**
     * Creates the html we want to send the user with the password where it's 
     * supposed to be.
     * @param newPass the new password.
     * @return a String with the html content.
     */
    public String emailBody(String newPass){
                StringBuilder contentBuilder = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new FileReader(fileToRead))) 
            {
                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) 
                {
                    contentBuilder.append(sCurrentLine).append("\n");
                }
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
             String emailhtml = contentBuilder.toString();
             emailhtml = emailhtml.replace("$Model.newPassword", newPass);
             return emailhtml;
    }
    
    
}
