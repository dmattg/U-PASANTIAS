package com.sigepu.sigepu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// Nuevas importaciones necesarias para configurar las reglas de seguridad
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // ¡Importante! Habilita la seguridad web
public class SecurityConfig {

    // Herramienta de encriptacion
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Reglas de acceso
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desactivamos una protección llamada CSRF temporalmente para
            // poder hacer pruebas de registro sin complicaciones.
            .csrf(csrf -> csrf.disable()) 
            
            // Configuramos quién puede entrar a dónde
            .authorizeHttpRequests(auth -> auth
                // .permitAll() permite entrar a CUALQUIER ruta sin login.
                // Esto es solo para desarrollo inicial.
                .anyRequest().permitAll() 
            );

        return http.build();
    }
}