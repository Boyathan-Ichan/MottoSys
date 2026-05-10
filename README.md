# MottoSys 📱📦

**MottoSys** es una solución integral de administración para negocios locales (misceláneas, tiendas de detergentes, abarrotes, etc.). Diseñada para ofrecer un control total sobre el inventario y las ventas, combinando la potencia de la nube con la resiliencia del almacenamiento local.

## 🚀 Características Principales

- **Gestión de Inventario:** Control detallado de stock, precios de compra y venta.
- **Módulo de Ventas Híbrido:** Interfaz moderna desarrollada en **Jetpack Compose** que se adapta según el hardware del usuario.
- **Soporte Handheld:** Optimización para dispositivos de escaneo profesional mediante entrada de texto dedicada.
- **Escaneo por Cámara:** Integración de visión artificial para negocios que utilizan la cámara del móvil como escáner.
- **Sincronización en la Nube:** Gestión de usuarios y licencias mediante **Supabase (PostgreSQL)**.
- **Modo Offline:** Base de datos local robusta en **SQLite** para garantizar la operatividad sin interrupciones de red.

## 🛠️ Stack Técnico

- **Lenguaje:** Kotlin 2.0.21
- **Arquitectura:** Pseudo-MVVM (Model-View-ViewModel)
- **UI:** XML View System + Jetpack Compose (Módulo de Ventas)
- **Base de Datos Local:** SQLite Puro
- **Backend/API:** Supabase (PostgreSQL) + Retrofit 2.11.0
- **Mínimo SDK:** API 21 (Android 5.0 Lollipop) - *Compatibilidad con el 100% de dispositivos.*

## 🏗️ Arquitectura del Proyecto

El proyecto sigue una estructura organizada para separar la lógica de negocio de la interfaz:

*   **Network Layer:** Implementación de Retrofit para el consumo de la API REST de Supabase.
*   **Data Layer:** Gestión de persistencia local con un `DatabaseHelper` personalizado para SQLite.
*   **Shared ViewModel:** Comunicación fluida entre Fragments y sincronización de datos de usuario en tiempo real.

## 🔐 Seguridad y Licenciamiento

MottoSys cuenta con un sistema de verificación de integridad en el arranque:
1.  **Validación de Red:** Conexión obligatoria para verificar el estado de la cuenta.
2.  **Control de Acceso:** Sistema de licencias dinámico (Activo, Bloqueado, Pendiente).
3.  **Protocolo de Emergencia:** Capacidad de ejecución de mantenimiento remoto para proteger la integridad del sistema en casos de vulneración de seguridad.

## 📦 Instalación y Configuración

1.  Clonar el repositorio:
    ```bash
    git clone [https://github.com/tu-usuario/mottosys.git](https://github.com/tu-usuario/mottosys.git)
