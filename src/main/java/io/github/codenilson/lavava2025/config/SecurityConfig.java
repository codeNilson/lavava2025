package io.github.codenilson.lavava2025.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("dev")
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desativa CSRF para facilitar o acesso ao H2 Console
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable()) // Permite uso de frames (necessário para
                                                                              // o H2 Console)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // Libera acesso ao H2 Console
                        .requestMatchers(HttpMethod.POST, "/players").permitAll() // Libera criação de jogadores
                        .anyRequest().authenticated() // Protege todas as outras rotas
                )
                .httpBasic(Customizer.withDefaults()); // Habilita autenticação HTTP Basic

        return http.build();
    }
}
