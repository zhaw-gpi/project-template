package ch.zhaw.sml.ifi.gpi.examples;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class CamundaSpringBootStarter {

  public static void main(String[] args) {
    SpringApplication.run(CamundaSpringBootStarter.class, args);
  }
}
