package ch.zhaw.sml.iwi.gpi.examples;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

/**
 * JavaDelegate-Klasse, welche 'Hallo {Variable}' in Konsole ausgibt.
 * JavaDelegate wird beschrieben in
 * https://docs.camunda.org/manual/7.7/user-guide/process-engine/delegation-code/#java-delegate
 *
 * @author scep
 */
@Named("exampleAdapter")
public class ExampleDelegate implements JavaDelegate {

    // Eine neue Logger-Instanz wird gestartet, damit auf diese in der execute-
    // Methode zugegriffen werden kann.
    private static final Logger LOGGER = Logger.getLogger(ExampleDelegate.class.getName());

    /**
     * Wird aufgerufen bei Aufgabe 'Beispiel-Java-Klasse aufrufen'. Gibt 'Hallo
     * exampleField' zurück, wobei exampleField dem Inhalt der entsprechenden
     * Variable entspricht, welche vom Benutzer im Startformular erfasst wurde.
     *
     * @param execution Zugriff auf das DelegateExecution-Objekt mit z.B. dem
     * aktuellen Variablen-Zustand
     * @throws Exception
     */
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // In der Konsole wird ein Log-Eintrag auf der Informations-Ebene ausgegeben
        LOGGER.log(Level.INFO, "Hallo {0}!", execution.getVariable("exampleField"));
    }

}
