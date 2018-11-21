package ch.zhaw.gpi.projecttemplate.delegates;

import ch.zhaw.gpi.projecttemplate.resources.User;
import ch.zhaw.gpi.projecttemplate.services.UserService;
import javax.inject.Named;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author scep
 */
@Named("getUserInformationAdapter")
public class GetUserInformationDelegate implements JavaDelegate {
    
    @Autowired
    private UserService userService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String anfrageStellenderBenutzer = (String) execution.getVariable("anfrageStellenderBenutzer");
        
        User user = userService.getUser(anfrageStellenderBenutzer);
        
        if(user == null){
            throw new BpmnError("User not found", "Es wurde kein Benutzer '" + anfrageStellenderBenutzer + "' gefunden.");
        }
        
        execution.setVariable("firstName", user.getFirstName());
        execution.setVariable("emailAddress", user.geteMail());
    }
    
}
