package com.zao.backend.turnos.api;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuracion exclusiva del perfil "academico" (evidencias de la Guia 7).
 *
 * Por defecto, Spring Boot escanea TODAS las clases anotadas con @Entity
 * dentro del paquete base de la aplicacion (com.zao.backend y sus
 * subpaquetes), incluyendo las entidades JPA reales del proyecto Zao
 * (por ejemplo las del modulo de balanceo de carga semanal). Como esas
 * entidades reales viven en la base de datos "zao_db" y este perfil
 * conecta contra la base academica "zao", Hibernate fallaria al intentar
 * validar tablas que no existen en "zao".
 *
 * Esta clase, activa SOLO cuando el perfil "academico" esta encendido,
 * le indica a Spring que escanee unicamente el paquete
 * com.zao.backend.turnos.api (donde vive el modulo academico de
 * EstacionTrabajo), dejando fuera de la validacion cualquier entidad
 * real del proyecto. En cualquier otro perfil (local, etc.) esta clase
 * no se activa y el comportamiento por defecto de Zao no cambia en nada.
 */
@Configuration
@Profile("academico")
@EntityScan(basePackages = "com.zao.backend.turnos.api")
@EnableJpaRepositories(basePackages = "com.zao.backend.turnos.api")
public class ConfiguracionAcademicaJpa {
}
