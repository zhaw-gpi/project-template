package ch.zhaw.gpi.projecttemplate.services;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Sendet Mails über Spring Mail-Komponente basierend auf den entsprechenden
 * Application Properties für den SMTP-Server. Basierend auf:
 * https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/integration.html#mail
 * http://www.baeldung.com/spring-email
 * https://www.quickprogrammingtips.com/spring-boot/how-to-send-email-from-spring-boot-applications.html
 *
 * @author scep
 */
@Service
public class EmailService {

    // Verdrahtet die Spring Java Mail-Sender-Klasse
    @Autowired
    public JavaMailSender javaMailSender;

    // Application.Properties-Eigenschaften in Variablen auslesen
    @Value("${mail.debug}")
    private Boolean debugMail;

    /**
     * Methode, um eine einfache Mail zu senden
     *
     * @param to Empfänger
     * @param subject Betreff
     * @param body Mail-Text
     */
    public void sendSimpleMail(String to, String subject, String body) {
        // Prüfen, ob Mail nur in Konsole ausgegeben werden soll
        if (debugMail) {
            this.debugMail(to, subject, body, null);
        } else {
            // Instanziert eine neue SimpleMail-Nachricht
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            // Legt Empfänger, Betreff und Mail-Text fest
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(body);

            // Versucht, die Mail abzusenden. Klappt es nicht, wird die Fehlermeldung
            // in der Konsole ausgegeben
            try {
                javaMailSender.send(simpleMailMessage);
            } catch (MailException me) {
                System.err.println(me.getLocalizedMessage());
            }
        }
    }

    /**
     * Methode, um eine HTML-Mail mit allenfalls einem (!) Anhang zu senden
     *
     * @param to Empfänger
     * @param subject Betreff
     * @param body Mail-Text
     * @param pathToAttachment Pfad zum anzuhängenden Dokument oder Null
     * @param nameOfAttachment Angezeigter Name der anzuhängenden Datei
     */
    public void sendMimeMail(String to, String subject, String body, Optional<String> pathToAttachment, Optional<String> nameOfAttachment) {
        // Prüfen, ob Mail nur in Konsole ausgegeben werden soll
        if (debugMail) {
            this.debugMail(to, subject, body, pathToAttachment);
        } else {
            // Instanziert eine neue Mime-Nachricht
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            // Spring nimmt einem mit der MimeMessageHelper-Klasse viel Arbeit ab, deshalb nutzen wir diese
            // und versuchen, sie zu instanzieren und alles weitere zu tun
            try {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, pathToAttachment.isPresent());

                // Legt Empfänger, Betreff und Mail-Text fest
                mimeMessageHelper.setTo(to);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(body);

                // Fügt das Attachment hinzu, falls Pfad-Angabe vorhanden
                if (pathToAttachment.isPresent()) {
                    // Ein Spring FileSystemResource (kapselt Java File)-Objekt konstruieren mit der Pfad-Angabe
                    FileSystemResource fileSystemResource = new FileSystemResource(pathToAttachment.get());

                    // Name des Attachments festlegen
                    String nameAttachment;
                    if(nameOfAttachment.isPresent()){
                        nameAttachment = nameOfAttachment.get();
                    } else {
                        nameAttachment = fileSystemResource.getFilename();
                    }
                    
                    // Die Datei und deren Name als Attachment hinzufügen
                    mimeMessageHelper.addAttachment(nameAttachment, fileSystemResource.getFile());
                }

                // Versucht, die Mail abzusenden. Klappt es nicht, wird die Fehlermeldung
                // in der Konsole ausgegeben
                try {
                    javaMailSender.send(mimeMessage);
                } catch (MailException me) {
                    System.err.println(me.getLocalizedMessage());
                }
            } catch (MessagingException me) {
                System.err.println(me.getLocalizedMessage());
            }
        }
    }

    /**
     * Sendet das Mail nicht, sondern gibt die Angaben in die Konsole aus
     *
     * @param to Empfänger
     * @param subject Betreff
     * @param body Mail-Text
     * @param pathToAttachment Pfad zum anzuhängenden Dokument oder Null
     */
    private void debugMail(String to, String subject, String body, Optional<String> pathToAttachment) {
        System.out.println("########### BEGIN MAIL ##########################");
        System.out.println("############################### Mail-Subjekt: " + subject);
        System.out.println("############################### Mail-Empfänger: " + to);
        if (pathToAttachment.isPresent()) {
            System.out.println("############################### Mail-Anhang: " + pathToAttachment.get());
        }
        System.out.println(body);
        System.out.println("########### END MAIL ############################");
    }
}
