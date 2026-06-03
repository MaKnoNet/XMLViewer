package de.makno.xmlviewer.app;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring-Boot-Einstiegspunkt der Demo-App (nur Demo – die {@code XmlViewer}-Komponente selbst hat
 * keine Spring-Abhängigkeit). Start: {@code ./gradlew bootRun}, dann http://localhost:8080.
 */
@SpringBootApplication
public class Application implements AppShellConfigurator {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
