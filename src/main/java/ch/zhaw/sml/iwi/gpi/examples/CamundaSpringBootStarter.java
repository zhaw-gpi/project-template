package ch.zhaw.sml.iwi.gpi.examples;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.h2.server.web.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Ruft Camunda Spring Boot Starter auf, welches zusammengefasst folgende Schritte durchläuft:
 * 1. Tomcat initialisieren
 * 2. Camunda REST API aktivieren
 * 3. Camunda Job Executor initialisieren
 * 4. Camunda Process Engine inklusive Datenbank initialisieren gemäss application.properties und application.yaml
 * 5. Sofern noch nicht vorhanden, den Demo-Admin-User erstellen gemäss application.yaml
 * 6. Die Webapps (Tasklist, Admin, Cockpit) deployen
 * 7. Gefundene Prozesse (z.B. TwitterDemoProcess) deployen
 * 8. Alle Komponenten starten => unter localhost:8080 ist Tomcat erreichbar *
 */
@SpringBootApplication
@EnableProcessApplication
public class CamundaSpringBootStarter {

    /**
     * Main-Methode, welche die Spring-Application aufruft
     * @param args 
     */
    public static void main(String[] args) {
        SpringApplication.run(CamundaSpringBootStarter.class, args);
    }

    /**
     * Stellt sicher, das die H2-Konsole über http://localhost:8080/console aufrufbar ist
     * 
     * @return 
     */
    @Bean
    public ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
        registration.addUrlMappings("/console/*");
        return registration;
    }
}
