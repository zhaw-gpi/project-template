package ch.zhaw.sml.iwi.gpi.examples;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.h2.server.web.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Hauptklasse für die Prozessapplikation, welche diese mit allen Abhängigkeiten
 * ausführt. Basis ist das SpringBoot-Framework, welches von Camunda erweitert
 * wurde.
 *
 * Zusammengefasst werden dabei folgende Schritte durchlaufen: 0. Abhängigkeiten
 * herunterladen 1. Tomcat initialisieren 2. Camunda REST API aktivieren 3.
 * Camunda Job Executor initialisieren 4. Camunda Process Engine inklusive
 * Datenbank initialisieren gemäss application.properties und application.yaml
 * 5. Sofern noch nicht vorhanden, den Demo-Admin-User erstellen gemäss
 * application.yaml 6. Die Webapps (Tasklist, Admin, Cockpit) deployen 7.
 * Gefundene Prozesse (z.B. ExampleProcess) deployen 8. Alle Komponenten starten
 * => unter localhost:8080 ist Tomcat erreichbar *
 */
@SpringBootApplication
@EnableProcessApplication
public class CamundaSpringBootStarter {

    /**
     * Haupt-Methode, welche beim Build/Run-Befehl eine
     * Camunda-Prozessapplikation erstellt.
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(CamundaSpringBootStarter.class, args);
    }

    /**
     * Stellt sicher, das die H2-Konsole über http://localhost:8080/console
     * aufrufbar ist Die H2-Konsole wird verwendet, um auf die Camunda-Datenbank
     * per GUI zugreifen zu können.
     *
     * @return registration
     */
    @Bean
    public ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
        registration.addUrlMappings("/console/*");
        return registration;
    }
}
