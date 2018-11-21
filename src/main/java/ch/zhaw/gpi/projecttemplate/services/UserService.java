package ch.zhaw.gpi.projecttemplate.services;

import ch.zhaw.gpi.projecttemplate.resources.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Stellt einen Benutzerdaten-bezogenen Dienst bereit
 * Nutzt hierzu seinerseit einen REST Service
 * 
 * @author scep
 */
@Component
public class UserService {
    
    // Deklarieren einer Spring Web RestTemplate-Variable, welches als Rest-Client fungiert
    private final RestTemplate restTemplate;
    
    // Auslesen der REST-Server-Endpoint-Adresse aus einer application.properties-Umgebungsvariable
    @Value("${userservice.endpoint}")
    private String userServiceEndpoint;

    // Constructor der Klasse, bei welchem ein neues RestTemplate-Objekt instanziert wird
    public UserService() {
        restTemplate = new RestTemplate();
    }
    
    /**
     * Methode, um den Benutzer zu einem Benutzernamen zu erhalten
     * 
     * @param userName   Benutzername
     * @return           null oder ein Benutzer-Objekt
     */
    public User getUser(String userName) {        
        // Aufruf des REST-Services über die getForObject-Methode von RestTemplate, welche als Parameter erwartet:
        // - Die URL mit Parametern, wobei der in {} gesetzte Teil dann durch den Parameter userName ersetzt wird
        // - Die Klassendefinition, in welcher die Antwort aufbereitet werden soll (von JSON zu dieser Klasse)
        // - Eine passende Anzahl an Parameter-Werten, welche die {} in der URL ersetzt (hier nur einer)
        // Zurücknehmen der Ressource und Deserialisierung als User-Objekt
        try{
            // Versuchen, eine Karte zu erhalten
            User user = restTemplate.getForObject(
                    userServiceEndpoint + "/users/{userName}",
                    User.class,
                    userName);
            
            // Wenn es klappt, diese zurückgeben
            return user;
        } catch(HttpClientErrorException httpClientErrorException) {
            // Wenn es fehlschlägt, in Abhängigkeit des HTTP-Status-Codes anders vorgehen
            // Wenn Resource nicht gefunden wird, dann null zurückgeben
            if(httpClientErrorException.getStatusCode() == HttpStatus.NOT_FOUND){
                return null;
            } else {
                // Ansonsten den Fehler weiter reichen
                throw httpClientErrorException;
            }
        }
    }
}
