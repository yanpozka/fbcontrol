package exselenium;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {

    private final Properties props;
    private final String email, emailpasswd;
    public String ccemail;

    public Email(String email, String passwd) {
        this.email = email;
        this.emailpasswd = passwd;
        props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.user", email);
        props.setProperty("mail.smtp.auth", "true");
    }

    public int sendEmailOrNot(int count) throws IOException {
        File file = new File(".info");
        
        if (!file.exists()) {
            System.out.println("[+] The file .info doesn't exist, created.");
            file.createNewFile();
            try (PrintWriter fwriter = new PrintWriter(file, "UTF-8")) {
                fwriter.print(count);
                fwriter.close();
            }
            return 0;
        } else {
            BufferedReader breader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file)));
            int current = Integer.valueOf(breader.readLine());
            try {
                byte sent = 0;
                if (current < count) {
                    this.sendEmail(getLessMessage(count - current));
                    sent = 1;
                } else if (current > count) {
                    this.sendEmail(getMoreMessage(current - count));
                    sent = 1;
                }
                return sent;
            } catch (MessagingException e) {
                System.out.println("[-] Error sending email");
                System.out.println(e.getMessage());
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
        return -1;
    }

    private void sendEmail(String msj) throws MessagingException {
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
        // Quien envia el correo
        message.setFrom(new InternetAddress("laura_yandry@con.amollll.com"));

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        if (!"".equals(ccemail))
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccemail));
        
        message.setSubject("Una alerta del robot nuestro");

        message.setText(msj, "ISO-8859-1", "html");

        Transport t = session.getTransport("smtp");
        t.connect(email, emailpasswd);
        t.sendMessage(message,message.getAllRecipients());
        t.close();
    }

    private String getLessMessage(int c) {
        return String.format("<h3>Ahora tienes <i>%d</i> amigos menos. <i>Muuy bien !!</i></h3>", c);
    }

    private String getMoreMessage(int c) {
        return String.format("<h2>Ahora tienes <i>%d</i> amigos mas. Jummmmm</h2>", c);
    }
}
