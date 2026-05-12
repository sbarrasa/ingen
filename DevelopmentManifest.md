# Development Manifest

Este manifiesto establece los lineamientos y estándares de desarrollo para el proyecto, asegurando la consistencia, calidad y mantenibilidad del código.

## Directivas de Desarrollo

### 1. Estructuras de Datos
*   **Java Records:** Se deben utilizar `records` para la definición de clases de datos (DTOs, POJOs inmutables) siempre que sea posible, aprovechando su concisión y naturaleza inmutable.

### 2. Convenciones de Nomenclatura
*   **Idioma:** Toda la nomenclatura (clases, métodos, variables, comentarios técnicos) debe realizarse exclusivamente en **inglés**.
*   **Estilo:** Los nombres deben ser **claros y concisos**, evitando abreviaturas ambiguas y buscando que el código sea autodocumentado.

### 3. Calidad y Pruebas
*   **Test-Driven Development (TDD):** Se priorizará el desarrollo orientado a pruebas. Los tests deben crearse antes de la implementación del componente.
*   **Alcance de Pruebas:** Se deben implementar pruebas para cada componente lógico creado, siempre que aporte valor (por ejemplo, no es obligatorio testear la estructura básica de un Java Record, pero sí la lógica de negocio, controladores y servicios).

### 4. Framework de Trabajo
*   **Spring Framework:** Se establece **Spring** (Spring Boot) como el framework de desarrollo principal para la arquitectura del proyecto, aprovechando sus capacidades de inyección de dependencias, gestión de configuración y ecosistema de módulos.

### 5. Control de Versiones
*   **Mensajes de Commit:** Cada commit debe tener un mensaje descriptivo que explique claramente qué cambios reales se realizaron en el código.
*   **Nomenclatura de Ramas:** Las ramas de desarrollo deben llevar un nombre que identifique claramente la tarea o funcionalidad que se está implementando.

---
*Este documento es de cumplimiento obligatorio para todos los colaboradores del proyecto.*
