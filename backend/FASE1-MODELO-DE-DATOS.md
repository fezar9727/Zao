# Zào — Fase 1: Arquitectura y Modelo de Datos

## Entidades y relaciones

```
Restaurante (raíz multi-tenant)
 ├── Empleado (rol, autorización de datos)
 │     └── ReunionSeguimiento (como admin, o como asistente vía reunion_asistente)
 ├── Estacion
 │     └── Turno (empleado + estacion)
 │           ├── Checklist (1:1)
 │           │     └── ItemChecklist (N)
 │           │           └── JustificacionPendiente (si queda pendiente y es crítico)
 │           └── RetroalimentacionAreas
 └── ReunionSeguimiento (INDIVIDUAL o GRUPAL)

BalanceoCargaSemanal → Empleado (independiente de Turno, se calcula agregando historial)
BitacoraAuditoria → transversal a todo el sistema, no depende de ninguna otra tabla vía FK
                     (guarda restaurante_id y entidad_id como referencias sueltas a propósito,
                     para que sobreviva aunque la entidad original se marque como eliminada)
```

## Por qué el modelo se ve así

**Multi-tenant desde la raíz**: cada tabla de negocio cuelga de `restaurante_id`
(directo o indirecto vía `empleado_id`/`turno_id`). Ningún query de negocio en
Fase 2 debe construirse sin filtrar por el restaurante del usuario autenticado.

**Borrado lógico universal**: `EntidadBase` da a toda entidad de negocio los
campos `activo`, `eliminado_en`, `eliminado_por`. Nunca se ejecuta un DELETE
real. Esto es requisito explícito del proyecto y tiene respaldo legal
(Decreto 1377 de 2013, art. 9: la supresión de un dato no procede si existe
un deber legal o contractual de conservarlo).

**Dos entidades deliberadamente inmutables y sin soft-delete**:
`JustificacionPendiente` y `BitacoraAuditoria`. No es un descuido — es porque
un registro que se puede editar o "eliminar lógicamente" pierde su valor
como evidencia. La Ley 527 de 1999 (art. 9) exige integridad del mensaje de
datos para que tenga valor probatorio; por eso estas dos tablas no tienen
setters de sus campos de contenido ni columnas de eliminación en absoluto.

**Autorización de datos en `Empleado`**: `autorizacionDatosAceptada` +
`fechaAutorizacionDatos`, exigido por la Ley 1581 de 2012 (art. 9) para el
tratamiento de datos personales del titular (el empleado).

**`esNocturno` / `esFestivo` en `Turno`**: Zào no calcula nómina, pero desde
julio de 2026 la jornada laboral colombiana es de 42 horas semanales
(Ley 2101 de 2021) con recargo nocturno de 7:00 p.m. a 6:00 a.m. Guardar esta
marca ahora evita rediseñar la tabla cuando se construya un módulo de
reportes o una integración con nómina.

**`ReunionSeguimiento` con `TipoReunion`**: separa explícitamente reuniones
INDIVIDUALES (privadas, un solo empleado, notas de desempeño) de GRUPALES
(briefings de equipo, sin datos de desempeño individual). La regla de que
una reunión INDIVIDUAL solo puede tener un asistente se valida en el service
de Fase 2, no a nivel de base de datos.

## Lo que NO se implementa todavía (a propósito)

- Controladores, servicios y repositorios — eso es Fase 2.
- El algoritmo real de cálculo de `BalanceoCargaSemanal` — se modela la
  tabla de resultado, no el cálculo.
- Bean Validation en DTOs de entrada — los DTOs no existen todavía porque no
  hay controladores; las anotaciones `@NotBlank`, `@Size`, etc. que ya están
  en las entidades son válidas igual para JPA, pero los DTOs de la Fase 2
  tendrán su propia validación en la frontera HTTP.
- Configuración de Spring Security / JWT.
- Los permisos GRANT/REVOKE reales sobre `bitacora_auditoria` en MySQL
  (el comentario en la migración V10 dice qué hacer, pero eso se ejecuta
  como parte de la configuración del entorno en Fase 2, no en el DDL).

## Próximo paso sugerido

Fase 2: crear los repositorios Spring Data JPA, los DTOs con Bean Validation,
los servicios con la lógica de negocio (incluyendo el `AuditoriaListener` que
escribe automáticamente en `BitacoraAuditoria` cada vez que se guarda o
marca como eliminada una entidad), y los controladores REST — empezando por
el módulo de turnos, como marca la guía de fases.
