/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.zhaw.sml.iwi.gpi.examples;

import java.util.Map;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ServiceTask1 implements JavaDelegate{

    @Override
    public void execute(DelegateExecution de) throws Exception {
        System.out.println("printing all process variables.");
        
        Map<String, Object> variables = de.getVariables();
        
        for (Map.Entry<String, Object> entry: variables.entrySet()){
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }
    }
    
}
