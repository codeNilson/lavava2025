package io.github.codenilson.lavava2025;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.github.codenilson.lavava2025.config.ValorantMapsProperties;

@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties(ValorantMapsProperties.class)
public class Lavava2025Application {

	public static void main(String[] args) {
		SpringApplication.run(Lavava2025Application.class, args);
	}

}
