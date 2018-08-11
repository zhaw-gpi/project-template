package ch.zhaw.gpi.projecttemplate.delegates;

import ch.zhaw.gpi.projecttemplate.services.EmailService;
import javax.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementiert den Service Task "Mitarbeiter benachrichtigen", indem dem Mitarbeiter
 * per Mail (nutzt Email-Service-Klasse) der Grund für die Ablehnung oder die erfolgreiche
 * Übermittlung des Tweets mitgeteilt wird
 * @author scep
 */
@Named("notifyEmployeeAdapter")
public class NotifyEmployeeDelegate implements JavaDelegate{
    
    // Verdrahted die Mailservice-Klasse
    @Autowired
    EmailService emailService;

    @Override
    public void execute(DelegateExecution de) throws Exception {
        // Empfänger-Mail-Adresse aus Prozessvariable auslesen
        String emailAddress = (String) de.getVariable("emailAddress");
        
        // Zu veröffentlichenden Tweet aus Prozessvariable auslesen
        String tweetContent = (String) de.getVariable("tweetContent");
        
        // Prüfergebnis aus Prozessvariable auslesen
        String checkResult = (String) de.getVariable("checkResult");
        
        // Bemerkungen zum Prüfergebnis aus Prozessvariable auslesen
        String checkResultComment = (String) de.getVariable("checkResultComment");
        
        // Anrede zusammenbauen (VERBESSERN)
        String mailAnrede = "Hallo xyz";
        
        // Hauptteil des Textes zusammenbauen basierend auf Prüfergebnis
        String mailHauptteil;
        if(checkResult.equals("rejected")){
            mailHauptteil = "Leider wurde diese Tweet-Anfrage abgelehnt mit " +
                    "folgender Begründung:\n" + checkResultComment;
        } else {
            mailHauptteil = "Ihr Tweet wurde geposted. Herzlichen Dank für Ihren Beitrag.";
        }
        
        // Mail-Text zusammenbauen
        String mailBody = mailAnrede + "\n\n" + "Sie haben folgenden Text zum " +
                "Veröffentlichen als Tweet vorgeschlagen:\n" + tweetContent + "\n\n" +
                mailHauptteil + "\n\n" + "Ihre Kommunikationsabteilung";
        
        // Mail über Mailservice versenden
        emailService.sendSimpleMail(emailAddress, "Neuigkeiten zu Ihrer Tweet-Anfrage", mailBody);
    }
    
}
