package alertUtilities;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailHandler {
    private String sender;
    private String password;
    private String recipient;

    public void setSender(String sender){
        this.sender = sender;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setRecipient(String recipient){
        this.recipient = recipient;
    }

    public void sendEmail(String image) throws MessagingException, IOException {
        // Setting up GMAIL SMTP service
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true); // GMAIL SMTP authentication
        properties.put("mail.smtp.host", "smtp.gmail.com"); // GMAIL outgoing SMTP server
        properties.put("mail.smtp.port", 587); // GMAIL Port 587 (TLS)
        properties.put("mail.smtp.starttls.enable", true); // Transport Security Layer
        properties.put("mail.transport.protocol", "smtp");

        // Get account authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
        });

        // Creating alert and sending
        Message message = new MimeMessage(session);
        message.setSubject("Intruder Detected");

        Address recipient = new InternetAddress(this.recipient);
        message.setRecipient(Message.RecipientType.TO, recipient);


        MimeMultipart multipart = new MimeMultipart();

        MimeBodyPart attachment = new MimeBodyPart();
        MimeBodyPart msgBodyPart_1 = new MimeBodyPart();
        MimeBodyPart msgBodyPart_2 = new MimeBodyPart();

        attachment.attachFile(new File(image));
        msgBodyPart_1.setContent("Atomic Security has detected an intruder.<br> <br>", "text/html");
        msgBodyPart_2.setContent("<FONT COLOR=\"#52CA4E\">-NEW IMAGE-", "text/html");

        multipart.addBodyPart(msgBodyPart_1);
        multipart.addBodyPart(msgBodyPart_2);
        multipart.addBodyPart(attachment);

        message.setContent(multipart);

        Transport.send(message);
    }

}
