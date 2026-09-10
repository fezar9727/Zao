# Guía de instalación — Fase 1 de Zào (paso a paso sin saltarse nada)

Esta guía asume Windows (por la ruta ~/OneDrive/Escritorio/Zao que ya tenías),
NetBeans 31 ya instalado, y MySQL corriendo localmente. Si algo no coincide
con tu setup real, avisame y la ajusto.

---

## PASO 1 — Descomprimir el zip sobre tu proyecto

1. Cerrá NetBeans si lo tenés abierto con el proyecto Zao (para evitar
   conflictos de archivos bloqueados mientras se copian).
2. Ubicá el archivo `Zao-Fase1-Modelo-de-Datos.zip` que descargaste (normalmente
   en tu carpeta `Descargas`).
3. Click derecho sobre el zip → **"Extraer todo..."** → elegí una carpeta
   temporal, por ejemplo `Descargas\zao-fase1-temp`.
4. Abrí esa carpeta temporal. Vas a ver una carpeta `backend` adentro.
5. Abrí en otra ventana del explorador tu proyecto real:
   `C:\Users\TU_USUARIO\OneDrive\Escritorio\Zao\backend`
6. **Copiá estos elementos** desde la carpeta temporal hacia tu proyecto real,
   reemplazando cuando el explorador te lo pregunte:
   - La carpeta `src` completa (reemplaza la que ya tenías, incluida la
     clase `Turno.java` de ejemplo que ya autorizaste a descartar).
   - El archivo `pom.xml` (reemplaza el que ya tenías — si le habías hecho
     cambios manuales que querés conservar, avisame antes de reemplazar y
     los fusionamos a mano).
   - El archivo `application.properties.example` (va en la raíz de `backend`,
     junto al `pom.xml`).
   - El archivo `FASE1-MODELO-DE-DATOS.md` (va en la raíz de `backend`, es
     solo documentación, no afecta la compilación).

---

## PASO 2 — Verificar que MySQL está corriendo y crear la base de datos

1. Abrí **MySQL Workbench** (o la herramienta que uses para administrar MySQL).
2. Conectate a tu servidor local (normalmente `localhost:3306`, usuario `root`
   o el que hayas configurado).
3. Si la conexión falla, MySQL no está corriendo: abrí el **Panel de Servicios
   de Windows** (`Win + R`, escribí `services.msc`, Enter), buscá el servicio
   `MySQL80` (o similar), click derecho → **Iniciar**.
4. Una vez conectado, ejecutá esta consulta para crear la base de datos vacía
   (Flyway va a crear las tablas adentro, vos solo creás el contenedor):

```sql
CREATE DATABASE zao_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

5. Anotá el usuario y contraseña que usás para conectarte — los vas a
   necesitar en el paso 4.

---

## PASO 3 — Abrir el proyecto en NetBeans

1. Abrí NetBeans 31.
2. Menú **Archivo → Abrir Proyecto...**
3. Navegá hasta `C:\Users\TU_USUARIO\OneDrive\Escritorio\Zao\backend`.
4. NetBeans debería reconocerlo como proyecto Maven (ícono de café con una
   "M"). Seleccionalo y click en **Abrir Proyecto**.
5. **No lo actives todavía** — primero necesita bajar las dependencias
   nuevas del `pom.xml` (Spring Boot, JPA, Flyway, etc.), y eso requiere
   internet. NetBeans lo hace solo, pero puede tardar unos minutos la
   primera vez porque descarga bastantes librerías.
6. En el panel de **Proyectos** (izquierda), click derecho sobre el proyecto
   `backend` → **Limpiar y Construir** (Clean and Build). Mirá la pestaña de
   **Salida** (Output) abajo: ahí vas a ver a Maven descargando dependencias.
7. Si termina con `BUILD SUCCESS` en verde, las dependencias bajaron bien y
   el código compiló. Si sale `BUILD FAILURE`, copiame el error exacto de esa
   pestaña de Salida y lo resolvemos.

---

## PASO 4 — Configurar application-local.properties

1. Dentro de NetBeans, en el panel de Proyectos, navegá a:
   `backend → Recursos → application.properties.example`
   (o buscalo directo en el explorador de Windows en
   `Zao\backend\application.properties.example`).
2. Hacé una **copia** de ese archivo en la misma carpeta
   (`src\main\resources`, mové el .example ahí si no está, o copiá su
   contenido a un archivo nuevo) y renombrala a `application-local.properties`.

   **Importante**: el archivo real que Spring Boot lee debe llamarse
   `application-local.properties` y vivir en `src/main/resources/`, no en la
   raíz del proyecto — el `.example` es solo la plantilla de referencia.
3. Abrí `application-local.properties` y reemplazá:
   - `TU_USUARIO_MYSQL` → tu usuario real de MySQL (ej. `root`).
   - `TU_CONTRASENA_MYSQL` → tu contraseña real.
   - Si tu base de datos no se llama `zao_db` como en el paso 2, ajustá la
     URL también.
4. Guardá el archivo.
5. Verificá que `application-local.properties` esté en tu `.gitignore` (el
   que ya tenías + lo que agregamos antes con `node_modules/`, `.env`, etc.)
   — este archivo **nunca** debe subirse a GitHub porque tiene tu contraseña
   real.

---

## PASO 5 — Decirle a NetBeans que use el perfil "local"

Spring Boot necesita saber que debe leer `application-local.properties`
además del `application.properties` base. Hay dos formas:

**Opción simple (recomendada para empezar):** en NetBeans, click derecho
sobre el proyecto → **Propiedades** → **Ejecutar** (Run) → en el campo
**Argumentos VM** (VM Options) escribí:

```
-Dspring.profiles.active=local
```

Click en **Aceptar**.

---

## PASO 6 — Ejecutar y verificar que las tablas se crean

1. Click derecho sobre el proyecto `backend` → **Ejecutar** (Run), o el botón
   de play verde en la barra de herramientas.
2. Mirá la pestaña de **Salida**. Deberías ver logs de Spring Boot arrancando,
   y en algún punto líneas de **Flyway** mencionando que está aplicando
   migraciones (`Migrating schema "zao_db" to version "1 - crear tabla
   restaurante"`, y así hasta la V10).
3. Si ves `Started BackendApplication in X seconds` al final sin errores en
   rojo, funcionó.
4. Volvé a **MySQL Workbench**, refrescá el panel de esquemas (click derecho
   sobre `zao_db` → **Refresh All**), y deberías ver estas 12 tablas nuevas:

   `restaurante`, `empleado`, `estacion`, `turno`, `checklist`,
   `item_checklist`, `justificacion_pendiente`, `balanceo_carga_semanal`,
   `retroalimentacion_areas`, `reunion_seguimiento`, `reunion_asistente`,
   `bitacora_auditoria`, más una tabla `flyway_schema_history` que Flyway
   crea solo para llevar registro de qué migraciones ya aplicó.

5. Si las 13 tablas aparecen: **Fase 1 verificada y funcionando de punta a
   punta**, felicitaciones. Avisame y seguimos con Fase 2.

---

## Si algo falla

Copiame el mensaje de error exacto (de la pestaña Salida de NetBeans o de
MySQL Workbench) tal cual aparece, sin resumirlo — los errores de Spring
Boot suelen decir exactamente qué línea o qué propiedad está mal, y con el
texto completo lo resuelvo mucho más rápido que si me lo describís de
memoria.
