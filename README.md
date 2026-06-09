# TNT-AppMascotas

## Repositorio para la materia Taller de Nuevas Tecnologias

## Integrantes: 
- Barea Matías Ezequiel 
- Pablovich Lucia Sofía 

## Propuesta: 
### 🐾 Aplicacion para cuidado integral y seguimiento de mascotas

Un grupo de veterinarios y dueños responsables de mascotas nos ha planteado la necesidad de desarrollar una aplicación móvil que funcione como un asistente digital para el cuidado integral de animales domésticos, con el objetivo de mejorar su bienestar y facilitar la organización de sus cuidados diarios.

La aplicación permitirá a los usuarios registrar el perfil de sus mascotas (perros, gatos u otros), incluyendo información relevante como edad, peso, historial médico y vacunas. A partir de estos datos, el sistema generará recordatorios automáticos y personalizados sobre vacunación, desparasitación, alimentación y turnos veterinarios, mediante notificaciones en tiempo real que ayuden a los usuarios a cumplir con los cuidados necesarios.

Además, la solución incorporará un sistema de seguimiento diario (tracker), donde los usuarios podrán registrar de manera simple e intuitiva información sobre el estado de la mascota, incluyendo alimentación, actividad física (paseos, juego), estado de ánimo y posibles síntomas o comportamientos inusuales. Esta funcionalidad permitirá llevar un historial detallado en el tiempo, facilitando la detección de cambios o problemas de salud.

La aplicación también ofrecerá la posibilidad de adjuntar imágenes en los registros diarios, permitiendo documentar visualmente el estado de la mascota (por ejemplo, lesiones, evolución física o cambios de comportamiento). Esto enriquecerá el historial y brindará información más completa al momento de compartirla con profesionales veterinarios.

Se espera que los usuarios puedan visualizar esta información de forma clara mediante un historial organizado, y que puedan compartir fácilmente los datos relevantes con veterinarios para mejorar el seguimiento y diagnóstico.

### 📁 Estructura del repositorio
TNT-AppMascotas\
├── AppMascotasV1/     # Release 1 demo\
├── docs/              # Documentación\
├── AppMascotasV2/     # Release actual (JetPack Compose) \
└── README.md

### Estructura del proyecto

AppMascotasV2 - Arquitectura CLEAN

com.appmascotasv2.smartpaws/
├── AppMascotasApp.kt
├── MainActivity.kt
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt
│   │   ├── dao/
│   │   │   └── ...
│   │   └── entity/
│   │       └── ...
│   ├── mapper/
│   │   └── ...Mapper.kt
│   └── repository/
│       └── ...RepositoryImpl.kt
├── domain/
│   ├── model/
│   │   └── ...
│   ├── repository/
│   │   └── ...Repository.kt
│   └── usecase/
│       ├── auth/
│       │   └── ...UseCase.kt
│       ├── mascota/
│       │    └── ...UseCase.kt
│       └── .../
├── presentation/
│   ├── navigation/NavGraph.kt
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── feature/
│       ├── auth/
│       │   ├── LoginScreen.kt
│       │   └── LoginViewModel.kt
│       ├── main/MainScreen.kt
│       └── mascota/
│           ├── ...Screen.kt
│           └── ...ViewModel.kt
└── di/AppContainer.kt

 
### 🚀 Cómo levantar el proyecto
1. Clonar el repositorio
git clone https://github.com/Matiaseze/TNT-AppMascotas.git
cd TNT-AppMascotas

2. Abrir proyecto
Abrir Android Studio
Seleccionar "Open"
Elegir la carpeta:
AppMascotasV2/ <-- Actual

3. Sincronizar Gradle
Una vez abierto el proyecto:

Android Studio detectará automáticamente la configuración
Esperar a que finalice la sincronización de Gradle
Si no inicia automáticamente:
Click en "Sync Project with Gradle Files"

4. Ejecutar la aplicación
Conectar un dispositivo movil android o iniciar un emulador
Presionar ▶️ Run
