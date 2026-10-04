package com.zao.backend.api;

import com.zao.backend.common.EntidadBase;
import com.zao.backend.identidad.Empleado;
import com.zao.backend.restaurante.Restaurante;
import com.zao.backend.tareas.Checklist;
import com.zao.backend.tareas.ItemChecklist;
import com.zao.backend.turnos.Estacion;
import com.zao.backend.turnos.Turno;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuracion del perfil "servicios" (evidencia AA5-EV03).
 *
 * Define de forma exacta las entidades que usan los servicios web (Restaurante,
 * Empleado, Estacion, Turno, Checklist e ItemChecklist), de modo que Hibernate
 * solo valide esas tablas contra la base de datos de pruebas y deje por fuera la
 * entidad del modulo academico de AA3, que usa otra tabla. Fuera de este perfil
 * esta clase no se activa y el resto de perfiles no cambia.
 *
 * Tambien define el codificador BCrypt que protege las contrasenas: solo se
 * usa la libreria spring-security-crypto, sin activar Spring Security.
 */
@Configuration
@Profile("servicios")
@EnableJpaRepositories(basePackages = "com.zao.backend.api.repository")
public class ConfiguracionServicios {

    /** Lista exacta de entidades que gestiona JPA en este perfil. */
    @Bean
    public PersistenceManagedTypes persistenceManagedTypes() {
        return PersistenceManagedTypes.of(
                EntidadBase.class.getName(),
                Restaurante.class.getName(),
                Empleado.class.getName(),
                Estacion.class.getName(),
                Turno.class.getName(),
                Checklist.class.getName(),
                ItemChecklist.class.getName());
    }

    /** Codificador BCrypt para guardar y verificar contrasenas. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
