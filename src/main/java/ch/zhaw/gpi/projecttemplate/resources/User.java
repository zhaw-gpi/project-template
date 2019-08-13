package ch.zhaw.gpi.projecttemplate.resources;

/**
 * Benutzer-Objekt, um Daten aus dem User-Rest-Service automatisch hier hinein deserialisieren zu können
 * 
 * @author scep
 */
public class User {
    private String userName;
    private String firstName;
    private String eMail;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
    
    
}
