package ch.zhaw.gpi.projecttemplate.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Zusätzlich zur Standard-Einstellungsdatei application.properties soll auch
 * mail.properties, respektive die darin enthaltenen Key-Value-Paare ausgelesen
 * werden.
 */
@Configuration
@PropertySource("classpath:mail.properties")
public class ApplicationConfiguration {

}