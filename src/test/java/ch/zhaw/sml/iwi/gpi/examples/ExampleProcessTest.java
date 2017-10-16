package ch.zhaw.sml.iwi.gpi.examples;

import java.util.HashMap;
import java.util.Map;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.complete;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;
import org.camunda.bpm.engine.test.mock.MockExpressionManager;
import org.junit.AfterClass;

import org.junit.Rule;
import org.junit.Test;

/**
 * Just a quick test on the happy path as an example...
 */
public class ExampleProcessTest {

    /*
     * Necessary preparation of the test environment. 
     * We want to do everything in-memory and also 
     * make sure that the database is droped after every test.
     */
    private static final ProcessEngineConfiguration processEngineConfiguration = new StandaloneInMemProcessEngineConfiguration() {
        {
            jobExecutorActivate = false;
            expressionManager = new MockExpressionManager();
            databaseSchemaUpdate = DB_SCHEMA_UPDATE_CREATE_DROP;
        }
    };

    private static final ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule(processEngine);

    @AfterClass
    public static void shutDown() {       
        processEngine.close();
    }
 
    /*
     * Actual testcode starts here 
     */
    @Test
    @Deployment(resources = "Process.bpmn")
    public void testTheAgeGatewayPassed() throws Exception {
        // Let's start directly before the gateway
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.
                createProcessInstanceByKey("Process").
                startAfterActivity("UserTask1").
                setVariable("age", 13).
                execute();
        // Given the age, the process should have run the service delegate and 
        // then end.
        assertThat(processInstance).isEnded();
    }

    @Test
    @Deployment(resources = "Process.bpmn")
    public void testTheAgeGatewayNotPassed() throws Exception {
        // Let's start directly before the gateway
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.
                createProcessInstanceByKey("Process").
                startAfterActivity("UserTask1").
                setVariable("age", 11).
                execute();
        // Given that the gateway works as expected we shoul be routed to 
        // UserTask2
        assertThat(processInstance).task().hasName("UserTask2");
    }

    @Test
    @Deployment(resources = "Process.bpmn")
    public void testHappyPath() throws Exception {
        // First we test if the "thing" actually deploys ...
        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process");
        String pid = processInstance.getProcessInstanceId();
        // Yes? Did it really start?
        assertThat(processInstance).isStarted();
        // We should be here ...
        assertThat(processInstance).task().hasName("UserTask1");
        // Let's inject some variables
        Map<String, Object> variables = new HashMap();
        variables.put("name", "Thomas");
        variables.put("age", 11);
        variables.put("approved", true);
        runtimeService.setVariables(pid, variables);
        // Presume that the user has done his input        
        complete(assertThat(processInstance).task().getActual());
        // Given that the gateway works as expected we shoul be routed to 
        // UserTask2
        assertThat(processInstance).task().hasName("UserTask2");
        // Compete and reset the age variable
        complete(assertThat(processInstance).task().getActual(), withVariables("age", 13));
        // We should be back to UserTask1 by now
        assertThat(processInstance).task().hasName("UserTask1");
        complete(assertThat(processInstance).task().getActual());
        // Given the age, the process should have run the service delegate and 
        // then end. 
        assertThat(processInstance).isEnded();
    }

}
