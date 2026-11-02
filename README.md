# Nexus Gestión - ERP & Punto de Venta

Sistema integral de gestión comercial, facturación y control de cuentas corrientes desarrollado para negocios físicos. Diseñado con una arquitectura modular y escalable, priorizando la agilidad en el Punto de Venta (POS) y el seguimiento detallado de deudores.

## Módulos Principales

- **Padrón de Clientes:** Gestión ABM de contactos y perfiles.
- **Cuentas Corrientes (Cobranzas):** Registro de deudas, pagos parciales y saldos en tiempo real con historial.
- **Catálogo de Productos:** Control de inventario dinámico, categorización y precios.
- **Punto de Venta (POS):** Interfaz ágil de carrito de compras AJAX, validación automática de stock, selección de métodos de pago e impresión de tickets térmicos.
- **Tablero de Comando (Reportes):** Dashboard gerencial interactivo con métricas de facturación, gráficos de rendimiento (Chart.js) y ranking automático de clientes.

## Stack Tecnológico

- **Backend:** Java 21, Spring Boot 3, Spring Data JPA
- **Frontend:** Thymeleaf, Bootstrap 5, JavaScript (Fetch API), Chart.js
- **Base de Datos:** MySQL (Hibernate / Relaciones OneToMany/ManyToOne)
- **Arquitectura:** MVC Layered (Model, Repository, Service, Controller, DTOs)

## Instalación y Configuración

1. Clonar el repositorio:

   ```bash
   git clone https://github.com/jmemmanuelsosa-tech/sistema-gestion-spring.git
   ```

2. Configurar las credenciales de conexión a MySQL en el archivo `src/main/resources/application.properties`.

3. Compilar e iniciar la aplicación mediante Maven:

   ```bash
   ./mvnw spring-boot:run
   ```

4. Acceder en el navegador web a `http://localhost:8080`.
