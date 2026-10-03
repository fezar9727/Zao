package com.zao.backend.api;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuracion del perfil "servicios" (evidencia AA5-EV03).
 *
 * Limita el escaneo de entidades y de repositorios JPA a lo que usan los
 * servicios web de esta fase (Restaurante y Empleado), de modo que Hibernate
 * solo valide esas tablas contra la base de datos de pruebas. Fuera de este
 * perfil esta clase no se activa y el resto de perfiles no cambia.
 *
 * Tambien define el codificador BCrypt que protege las contrasenas: solo se
 * usa la libreria spring-security-crypto, sin activar Spring Security.
 */
@Configuration
@Profile("servicios")
@EntityScan(basePackages = {"com.zao.backend.identidad", "com.zao.backend.restaurante"})
@EnableJpaRepositories(basePackages = "com.zao.backend.api.repository")
public class ConfiguracionServicios {

    /** Codificador BCrypt para guardar y verificar contrasenas. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
