package io.github.codenilson.lavava2025;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.github.codenilson.lavava2025.config.ValorantMapsProperties;

/**
 * Main application class for the Lavava2025 Valorant tournament management system.
 * This Spring Boot application provides a comprehensive platform for managing
 * Valorant tournaments, including player registration, team management, match tracking,
 * and ranking systems.
 * 
 * @author codenilson
 * @version 1.0
 * @since 2025-01-01
 */
@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties(ValorantMapsProperties.class)
public class Lavava2025Application {

	/**
	 * Main method to start the Lavava2025 application.
	 * 
	 * @param args command line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(Lavava2025Application.class, args);
	}

}
