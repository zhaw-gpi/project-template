Björn Scheppler, 1.8.2017

# Read Me project-template
In diesem Projekt geht es darum, aufzuzeigen, wie Camunda per Spring Boot mit integriertem Tomcat und filebasierter H2-Datenbank genutzt werden kann.

# Verwendete Quellen
Erste Version stammt von Peter Heinrich. Ich habe vor allem sichergestellt, dass die aktuellen Versionen von Camunda/Spring Boot Starter im pom.xml verwendet werden sowie das Ganze ausführlich dokumentiert für das bessere Verständnis durch uns aber auch Studierende und schliesslich noch den Namespace von ...ifi... zu ...iwi... geändert.
Weitere Quellen:
https://camunda.github.io/camunda-bpm-spring-boot-starter/docs/current/index.html
https://github.com/camunda/camunda-bpm-spring-boot-starter
https://forum.camunda.org/c/community-extensions/spring-boot-starter
https://github.com/camunda/camunda-bpm-spring-boot-starter/issues

# Status
1. Tomcat wird gestartet, Camunda mit den Prozessen und Eigenschaften (application.properties und application.yaml) deployt
2. Man kann sich anschliessend auf http://localhost:8080 mit demo:demo anmelden und einen Prozess starten.
3. Erstellte Filter und Tasks, Prozessinstanzen, usw. werden dank file-basierter H2-Datenbank erhalten, auch wenn Tomcat gestoppt wird.
4. Man muss noch von Hand mindestens einen Task Filter erstellen, damit überhaupt erstellte Tasks angezeigt werden (Create a filter, wobei Assigneee = ${currentUser()} ist).
5. Obwohl eigentlich Camunda Version 7.7 eingestellt ist im POM, wird die Engine als 7.7, die Webapps als 7.6 deployt. Dies ist vermutlich ohne Hack nicht ganz einfach zu lösen. Aber die nächste Version 2.2 von Camunda Spring Boot Starter löst das Problem ohnehin.
6. Mindestens in Eclipse muss man die Java-Applikation terminieren, weil bei einem nächsten Run As Java Application auf der Main-Methode ansonsten steht, dass der Port 8080 bereits gesperrt sei.
7. Dass man noch application.properties und application.yaml verwenden muss, ist etwas unschön. Eventuell liesse sich mindestens ein Teil davon stattdessen über Annotations lösen. Habe dies aber nicht getestet, mehr dazu hier in Kap. 4.2 und 4.3: https://camunda.github.io/camunda-bpm-spring-boot-starter/docs/current/index.html#_overriding_a_default_configuration
8. Den Prozess selbst konnte ich nicht instanzieren, weil in der History-Tabelle ein Feld fehle (org.h2.jdbc.JdbcSQLException: Feld "OPERATION_ID_" nicht gefunden). Aus Zeitgründen bin ich dem nicht nachgegangen. Eventuell ist dieser Fehler ja auch nur bei mir.

# Learnings
1. Die Idee von Peter mit Spring Boot ist fantastisch. Wenn man in die Issues (siehe oben bei Quellen) sieht, gibt es vermutlich dann zwar schon noch die eine oder andere kleine Überraschung, aber diese sollten zu bewältigen sein.
2. Die Idee ist deshalb so bestechend, weil
 1. Keine WAR-Files deployen muss.
 2. Keine Camunda-Distribution mit Tomcat separat installiert werden muss, sondern eine Entwicklungsumgebung, JDK und Maven ausreichen.
 3. Die Fehlerquellen dadurch massiv sinken.
 4. Die Zeit fürs Deployment massiv sinken.
 5. Eine Remote-Debugging-Konfiguration entfällt: Statt Run as, kann man einfach Debug As verwenden.
3. Als einziger Nachteil sehe ich bis jetzt nur, dass die Studierenden die Komplexität nicht erfassen, weil einfach im Hintergrund alles magic abläuft. Dem könnte man begegnen mit:
 1. In der Vorlesung wird den Studierenden "klassisch" gezeigt, wie ein solcher Stack aufgebaut ist und wie man Camunda "normalerweise" deployt.
 2. Mit guten Systemdiagrammen wird gezeigt, wie der Stack im Hintergrund aufgebaut ist.
 3. Durch ein Debugging geht man mit den Studierenden Schritt für Schritt durch den ganzen Build-Prozess und erklärt, was da gerade geschieht.