# Medix App

Aplicación móvil Android del proyecto Medix, orientada a facilitar la interacción de pacientes con servicios de agendamiento, autenticación, consentimiento informado, notificaciones y asistencia conversacional dentro del proyecto de grado.

## Descripción general

Este repositorio contiene la aplicación móvil de Medix desarrollada para Android. La aplicación permite registrar y autenticar pacientes, consultar citas, aceptar documentos legales, recibir notificaciones, visualizar información de perfil y usar flujos conversacionales por texto o voz para apoyar procesos relacionados con citas médicas.

El repositorio pertenece al sistema Medix y corresponde al cliente móvil del ecosistema. Su rol dentro de la arquitectura general es actuar como interfaz de usuario para pacientes, consumiendo servicios backend de datos, servicios de inteligencia artificial, autenticación con Supabase, mensajería con Firebase Cloud Messaging y comunicación en tiempo real mediante WebSocket.

## Tecnologías utilizadas

- Lenguaje: Kotlin
- Framework: Android SDK, Jetpack Compose
- Arquitectura y patrones: MVVM, repositorios, inyección de dependencias con Hilt
- Base de datos local: Room
- Persistencia local: DataStore Preferences, Android Security Crypto
- Comunicación HTTP: Retrofit, OkHttp, Gson Converter
- Comunicación en tiempo real: WebSocket mediante OkHttp
- Autenticación externa: Supabase Auth
- Notificaciones: Firebase Cloud Messaging
- Mapas: osmdroid
- Carga de imágenes: Coil Compose
- Herramientas: Gradle Wrapper, Android Gradle Plugin, Kotlin Serialization, KSP
- Pruebas: JUnit, AndroidX Test, Espresso, Compose UI Test

## Arquitectura del repositorio

```bash
/
├── .gitignore
├── Medix/
│   ├── app/
│   │   ├── build.gradle.kts
│   │   ├── proguard-rules.pro
│   │   └── src/
│   │       ├── androidTest/
│   │       ├── main/
│   │       │   ├── AndroidManifest.xml
│   │       │   ├── java/com/example/medix/
│   │       │   └── res/
│   │       └── test/
│   ├── build.gradle.kts
│   ├── gradle/
│   │   ├── libs.versions.toml
│   │   └── wrapper/
│   ├── gradle.properties
│   ├── gradlew
│   ├── gradlew.bat
│   └── settings.gradle.kts
└── README.md
```

- `.gitignore`: define archivos locales, credenciales, builds y artefactos que no deben versionarse.
- `Medix/`: proyecto Android principal.
- `Medix/app/`: módulo de aplicación Android.
- `Medix/app/src/main/AndroidManifest.xml`: declara permisos, actividad principal, servicio de Firebase Messaging y configuración de red.
- `Medix/app/src/main/java/com/example/medix/`: código fuente principal de la aplicación.
- `Medix/app/src/main/java/com/example/medix/core/`: utilidades transversales, autenticación, DataStore y configuración de red.
- `Medix/app/src/main/java/com/example/medix/data/`: DTO, mappers, fuentes locales y repositorios concretos.
- `Medix/app/src/main/java/com/example/medix/domain/`: entidades y contratos de repositorios.
- `Medix/app/src/main/java/com/example/medix/presentation/`: pantallas, componentes, navegación, temas y ViewModels.
- `Medix/app/src/main/java/com/example/medix/services/`: servicios de audio y mensajería Firebase.
- `Medix/app/src/test/`: pruebas unitarias locales.
- `Medix/app/src/androidTest/`: pruebas instrumentadas de Android.
- `Medix/gradle/libs.versions.toml`: catálogo centralizado de versiones de dependencias y plugins.
- `Medix/gradlew` y `Medix/gradlew.bat`: scripts de Gradle Wrapper para ejecutar tareas del proyecto.

## Requisitos previos

* Android Studio compatible con Android Gradle Plugin 9.0.0.
* JDK 11 o superior.
* Gradle Wrapper incluido en el repositorio.
* Android SDK con `compileSdk` 36 y `minSdk` 26.
* Dispositivo Android físico o emulador.
* Acceso a los servicios backend de Medix para datos, inteligencia artificial y WebSocket.
* Credenciales/configuración de Supabase.
* Archivo `google-services.json` para Firebase Cloud Messaging. Debe ser validado por el equipo, ya que está excluido del control de versiones.
* Variables o propiedades locales de configuración definidas en `local.properties` o como propiedades de Gradle.

## Instalación

```bash
git clone https://github.com/G11-Medix/medix-app.git
cd medix-app/Medix
```

Instalar dependencias y compilar el proyecto mediante Gradle Wrapper:

```bash
./gradlew build
```

En Windows:

```bash
gradlew.bat build
```

También puede abrirse la carpeta `Medix/` desde Android Studio para sincronizar el proyecto y ejecutar la aplicación en un emulador o dispositivo físico.

## Variables de entorno

El proyecto lee configuración desde `Medix/local.properties` o desde propiedades de Gradle. Ejemplo:

```env
MEDIX_DATA_API_BASE_URL=http://localhost:8001/
MEDIX_AI_API_BASE_URL=http://localhost:8000/
MEDIX_WS_BASE_URL=ws://localhost:8000
SUPABASE_URL=https://proyecto.supabase.co
SUPABASE_ANON_KEY=clave_anonima_supabase
```

Notas:

* `MEDIX_API_BASE_URL` se mantiene como alias de compatibilidad para `MEDIX_DATA_API_BASE_URL`.
* No se deben versionar credenciales reales ni archivos sensibles.
* `google-services.json` es requerido para la integración con Firebase y debe ser suministrado por el equipo.

## Ejecución local

Desde la carpeta del proyecto Android:

```bash
cd Medix
./gradlew installDebug
```

También puede ejecutarse desde Android Studio seleccionando el módulo `app` y un dispositivo o emulador Android.

Para generar una compilación de depuración:

```bash
./gradlew assembleDebug
```

## Pruebas

Ejecutar pruebas unitarias locales:

```bash
cd Medix
./gradlew test
```

Ejecutar pruebas instrumentadas en un dispositivo o emulador:

```bash
cd Medix
./gradlew connectedAndroidTest
```

El repositorio incluye pruebas de ejemplo y una prueba unitaria para el parser de respuestas del asistente de voz.

## Uso general

La aplicación se usa como cliente móvil Android de Medix. Al ejecutarse, presenta flujos de autenticación, registro de paciente, consentimiento, agenda de citas, historial, perfil, notificaciones y asistencia por voz o chat.

La aplicación consume servicios REST y WebSocket. Algunos endpoints identificados en el código son:

```text
POST /conversation
POST /chat/conversation
POST /asr/transcribe
GET  /api/eps
GET  /auth/eligibility/{telefono}
POST /api/pacientes
GET  /api/pacientes/{id_paciente}/citas
GET  /api/pacientes/{id_paciente}/profile
GET  /appointment/confirmation
POST /api/dispositivos/token
GET  /api/aceptacion-documento/activo
POST /api/aceptacion-documento
GET  /api/aceptacion-documento/estado
WS   /ws/conversation/{sessionId}
```

Para acceder localmente, instale la aplicación en un emulador o dispositivo Android y configure las URLs de los servicios backend de acuerdo con el entorno disponible.

## Relación con otros repositorios

Este repositorio se relaciona con los servicios backend de Medix encargados de datos de pacientes, citas, documentos legales, notificaciones, servicios de inteligencia artificial, transcripción de audio y conversación en tiempo real.

La relación exacta con otros repositorios debe ser documentada por el equipo de desarrollo.

## Estado del proyecto

Prototipo académico finalizado.

## Convenciones

Convenciones detectadas:

* Uso de Kotlin y Jetpack Compose para la interfaz.
* Separación por capas `core`, `data`, `domain`, `presentation`, `di` y `services`.
* Uso de ViewModels para manejar estado de pantalla.
* Uso de repositorios para abstraer fuentes de datos y servicios externos.
* Uso de `libs.versions.toml` para centralizar versiones.
* Uso de `local.properties` o propiedades de Gradle para configuración sensible o dependiente del entorno.

Convenciones recomendadas:

* Nombres de ramas: `feature/nombre-funcionalidad`, `fix/descripcion-bug`, `docs/descripcion-documentacion`.
* Estilo de commits: mensajes claros en presente, por ejemplo `docs: actualiza README del cliente móvil`.
* Estructura de carpetas: mantener separación por responsabilidad y capa.
* Variables de entorno: documentar nuevas variables y evitar valores sensibles en el repositorio.
* Formato de código: aplicar el estilo oficial de Kotlin y mantener nombres descriptivos para clases, pantallas, DTO y repositorios.

## Autores

Proyecto desarrollado como parte del trabajo de grado.

Equipo de desarrollo:

* Adrián Eduardo Ruiz Cerquera
* Leonardo Velázquez Colin
* Diego Alejandro Jara Rojas
* Jairo Andrés Sierra Combariza

## Licencia

* CC BY-NC 4.0
