/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
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
import org.apache.commons.text.StringEscapeUtils;
/**
 *
 * @author 2dam
 */
public class EmailSender {
     // Server mail user & pass
    private static String user = null;
    private static String pass = null;
    private String sendTo;
    // DNS Host + SMTP Port
    private static String smtp_host = null;
    private static int smtp_port = 0;

    // Default DNS Host + port
    private static final String DEFAULT_SMTP_HOST = "posta.tartanga.eus";
    private static final int DEFAULT_SMTP_PORT = 25;
    
    public static int sendEmail(String email) throws MessagingException{
        try{
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
        message.setSubject("Recupera tu cuenta"); // Asunto del mensaje

        // A mail can have several parts
        Multipart multipart = new MimeMultipart();

        // A message part (the message, but can be also a File, etc...)
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent("aquí viene el texto para decirle lo de la contraseña", "text/html");
        multipart.addBodyPart(mimeBodyPart);

        // Adding up the parts to the MIME message
        message.setContent(multipart);

        // And here it goes...
        Transport.send(message);
        }catch(Exception e){
            return 2;
        }
        return 1;
    }
    
    public String emailBody(String email){
                StringBuilder contentBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\2dam\\Documents\\NetBeansProjects\\Server\\src\\java\\mediafiles\\email.html"))) 
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
             emailhtml = emailhtml.replace("$Model.email", email);
             return emailhtml;
    }
    
    
}
