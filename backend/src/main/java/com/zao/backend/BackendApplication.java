package com.zao.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Punto de entrada de la aplicacion. @EnableJpaAuditing activa el llenado
 * automatico de fechaCreacion / fechaActualizacion definidos en EntidadBase
 * a traves de @CreatedDate y @LastModifiedDate.
 */
@SpringBootApplication
@EnableJpaAuditing
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
