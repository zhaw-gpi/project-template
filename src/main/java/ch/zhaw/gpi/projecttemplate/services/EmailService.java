package ch.zhaw.gpi.projecttemplate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Sendet Mails über Spring Mail-Komponente basierend auf den entsprechenden
 * Application Properties für den SMTP-Server. Basierend auf:
 * https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/integration.html#mail
 * http://www.baeldung.com/spring-email
 * https://www.quickprogrammingtips.com/spring-boot/how-to-send-email-from-spring-boot-applications.html
 * @author scep
 */
@Service
public class EmailService {
    
    // Verdrahtet die Spring Java Mail-Sender-Klasse
    @Autowired
    public JavaMailSender javaMailSender;
    
    /**
     * Methode, um eine einfache Mail zu senden
     * @param to        Empfänger
     * @param subject   Betreff
     * @param body      Mail-Text
     */
    public void sendMail(String to, String subject, String body) {
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
        } catch(MailException mailException) {
            System.err.println(mailException.getLocalizedMessage());
        }
    }
}
