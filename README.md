Björn Scheppler, 3.8.2018

# Camunda Projekttemplate (project-template)
Dieses Maven-Projekt kann genutzt werden als Startpunkt für eigene auf Camunda beruhende Projekte. Enthalten sind folgende Funktionalitäten:
1. Spring Boot 2.0.2 konfiguriert für Tomcat
2. Camunda Spring Boot Starter 3.0.0
3. Camunda Process Engine, REST API und Webapps (Tasklist, Cockpit, Admin) in der Version 7.9
4. H2-Datenbank-Unterstützung (von Camunda Engine benötigt)
5. "Sinnvolle" Grundkonfiguration in application.properties für Camunda, Datenbank und Tomcat
6. Ein Beispielprozess (Verarbeitung von Tweet-Anfragen) bestehend aus:
    1. BPMN-Modell mit User Tasks und Service Tasks
    2. HTML-Formulare als Implementation für die User Tasks/das Startformular
    3. Eine JavaDelegate-Klasse als Implementation für den Service Task "Mitarbeiter benachrichtigen" (per Mail)
    4. Für die Implementation für den Service Task "Tweet senden" ist eines der folgenden separaten Github-Projekte zu nutzen:
        1. External Task Client Mocking Template (https://github.com/zhaw-gpi/external-task-client-mocking-template)
        2. External Task Client Spring Boot Template (https://github.com/zhaw-gpi/external-task-client-spring-boot-template)
7. Ein Beispielprozess (Zeit-gesteurtes Tweet senden) bestehend aus einem BPMN-Modell mit:
    1. einem Timer-Start-Ereignis, damit nach dem Deployment alle 10 Sekunden eine neue Instanz gestartet wird. Dies damit man zum Zeigen der Worker-Funktionalität nicht jedes Mal in der Task List einen Prozess durchspielen muss.
    2. einem Script Task, welcher per JavaScript die aktuelle Uhrzeit als zu veröffentlichender Tweet-Content aufbereitet
    3. einem Service Task, der 1:1 identisch ist mit demjenigen aus dem anderen Beispielprozess, um die Aufgabe von einem External Task Client ausführen zu lassen.
    4. Das Topic heisst hier bewusst anders (SendTweetTime), weil hier nur das Mocking-Projekt darauf reagieren soll, da sonst rasch das Twitter-Konto gesperrt würde bei so vielen ähnlichen Posts.

## Verwendete Quellen
Die aktuelle Version basiert vor allem auf dem Get Started-Beispiel von Camunda 7.9 (https://docs.camunda.org/get-started), verwendet aber auch das Know-How aus dem Umzugsprojekt des Herbstsemesters 2017.

## Vorbereitungen, Deployment und Start
1. Erstmalig oder bei Problemen ein Clean & Build (Netbeans), respektive "mvn clean install" (Cmd) durchführen
2. Bei Änderungen am POM-File oder bei (Neu)kompilierungsbedarf genügt ein Build (Netbeans), respektive "mvn install"
3. Damit der Mail-Versand funktioniert, ist der Bereich # Mail-Konfiguration in application.properties anzupassen, falls nicht Gmail genutzt wird, respektive sind in Netbeans unter Project ->Properties ->Actions -> Run project -> Set Properties: Add neue Umgebungsvariablen anzulegen: Env.mailUser=BENUTZERNAME und Env.mailPass=PASSWORT. Das kann z.B. das Konto von zwi.sml@gmail.com sein oder ein Beliebiges -> Achtung: falls Zwei-Faktoren-Authentifizierung aktiviert ist, muss ein App-Passwort erstellt werden gemäss https://support.google.com/accounts/answer/185833?hl=de.
4. Für den Start ist ein Run (Netbeans), respektive "java -jar .\target\project-template-3.0.1.jar" (Cmd) erforderlich. Dabei wird Tomcat wird gestartet, die Datenbank erstellt/hochgefahren, Camunda in der Version 7.9 mit dem Beispiel-Prozess und den Eigenschaften (application.properties) hochgefahren.
5. Das Beenden geschieht mit Stop Build/Run (Netbeans), respektive CTRL+C (Cmd)
6. Falls man die bestehenden Prozessdaten nicht mehr benötigt und die Datenbank inzwischen recht angewachsen ist, genügt es, die Datei zhaw-gpi.mv.db im Wurzelverzeichnis des Projekts zu löschen.

## Grundlegende Nutzung (Tasklist und Cockpit)
1. http://localhost:8080 aufrufen
2. Anmelden mit Benutzername und Passwort a
3. Tasklist öffnen (und in einem separaten Tab das Cockpit)
4. "Start Process" > "Verarbeitung von Tweet-Anfragen"
5. Nun wird man durch den Prozess geführt. Folgende Hinweise:
    1. Bei E-Mail-Adresse eine funktionierende Mail-Adresse eingeben, an die man auch wirklich eine Benachrichtigung will.
    2. Bei "Tweet-Anfrage" prüfen ist ein Claim erforderlich, da bewusst jede Person aus der Kommunikationsabteilung, die Aufgabe ausführen können soll. Der Nutzer a hat als Admin Zugriff auf alle Aufgaben. PS: Selbst wenn man einen zusätzlichen Nicht-Admin-Benutzer erstellt und diesen nicht der Gruppe kommunikationsabteilung zuweist, sieht er trotzdem alle Aufgaben für diese. Grund: Authorisierung ist standardmässig deaktiviert => Jeder Benutzer hat alle Rechte.
    3. Damit der External Task "Tweet senden" auch tatsächlich erledigt wird, muss eines der beiden Projekte (siehe Punkt 6.4 in der Einleitung) gestartet sein.
6. Im Cockpit kann man bei Bedarf den Prozessfortschritt und mehr verfolgen

## Fortgeschrittene Nutzung (Duplikat-Tweet-Fehler und Behebung)
In diesem Beispiel geht es darum zu zeigen, wie ein Fehler im External Task Client zu einem Incident der entsprechenden Prozessinstanz führt, welcher im Cockpit "behoben" werden kann und über einen Retry dieses Mal fehlerfrei abläuft.
1. Auf der Twitter-Timeline den letzten Post in die Zwischenablage kopieren (Ziel ist, eine DuplicateStatusException zu provozieren)
2. Tasklist öffnen (und in einem separaten Tab das Cockpit)
3. "Start Process" > "Verarbeitung von Tweet-Anfragen"
4. Den Text aus der Zwischenablage einfügen
5. Im nächsten Schritt die Tweet-Anfrage genehmigen => ein neuer Task wird erstellt
6. Spätestens jetzt den External Task Client Spring Boot Template (https://github.com/zhaw-gpi/external-task-client-spring-boot-template) starten
7. Dieser wird den Task aufnehmen, aber einen Fehler produzieren (DuplicateStatusException) und diesen an die Process Engine weitergeben.
8. Dies führt dort zu einem Incident (im Cockpit zu sehen).
9. Eine Variante, den Incident im Cockpit zu beheben ist:
    1. Die Variable TweetContent so anpassen, dass sie kein Duplikat mehr darstellt.
    2. Einen Retry des External Tasks anzustossen.
    3. Warten, bis es dieses Mal fehlefrei durchläuft.

## Fortgeschrittene Nutzung (H2 Console)
1. Um auf die Datenbankverwaltungs-Umgebung zuzugreifen, http://localhost:8080/console eingeben.
2. Anmeldung über:
    1. Benutzername sa
    2. Passwort: leer lassen
    3. URL jdbc:h2:./zhaw-gpi

## Fortgeschrittene Nutzung (Zugriff über REST)
Die Engine kann auch per REST API gesteuert werden. Hierzu die Dokumentation unter https://docs.camunda.org/manual/7.9/reference/rest/ lesen. Wegen Spring Boot ist die URL für die REST API minimal anders als in der Dokumentation beschrieben. Sie ist: http://localhost:8080/rest/. So gibt z.B. http://localhost:8080/rest/engine den Namen der Engine (default) zurück.