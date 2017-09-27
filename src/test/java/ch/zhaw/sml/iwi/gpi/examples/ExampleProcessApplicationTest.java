package ch.zhaw.sml.iwi.gpi.examples;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareAssertions.assertThat;
import org.junit.Rule;
import org.junit.Test;

/**
 * Sanity test to verify the example process is syntactical correct and can be
 * deployed to the engine. All "real" tests should be implemented in the
 * according modules (jbehave, assertions, needle, ...).
 */
public class ExampleProcessApplicationTest {

    @Rule
    public final ProcessEngineRule rule = new ProcessEngineRule(new StandaloneInMemProcessEngineConfiguration().buildProcessEngine());

    @Test
    @Deployment(resources = "ExampleProcess.bpmn")
    public void shouldDeployWithoutErrors() throws Exception {
        // nothing here, test successful if deployment works
        RuntimeService runtimeService = rule.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("ExampleProcess");
        assertThat(instance).isStarted();
    }

}
