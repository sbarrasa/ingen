# Development Manifest

Este manifiesto establece los lineamientos y estandares de desarrollo para el proyecto, asegurando la consistencia, calidad y mantenibilidad del codigo.

## Directivas de Desarrollo

### 1. Estructuras de Datos
*   **Java Records:** Se deben utilizar `records` para la definicion de clases de datos (DTOs, POJOs inmutables) siempre que sea posible, aprovechando su concision y naturaleza inmutable.

### 2. Convenciones de Nomenclatura
*   **Idioma:** Toda la nomenclatura (clases, metodos, variables, comentarios tecnicos) debe realizarse exclusivamente en **ingles**.
*   **Estilo:** Los nombres deben ser **claros y concisos**, evitando abreviaturas ambiguas y buscando que el codigo sea autodocumentado.

### 3. Calidad y Pruebas
*   **Test-Driven Development (TDD):** Se priorizara el desarrollo orientado a pruebas. Los tests deben crearse antes de la implementacion del componente.
*   **Alcance de Pruebas:** Se deben implementar pruebas para cada componente logico creado, siempre que aporte valor (por ejemplo, no es obligatorio testear la estructura basica de un Java Record, pero si la logica de negocio, controladores y servicios).
*   **Aislamiento de Pruebas:** Siempre que sea posible, se debe evitar el uso del contexto de Spring en las pruebas. Por eso es importante incluir las dependencias de las clases en el constructor, facilitando pruebas unitarias aisladas y de ejecucion rapida.

### 4. Framework de Trabajo
*   **Spring Framework:** Se establece **Spring** (Spring Boot) como el framework de desarrollo principal para la arquitectura del proyecto, aprovechando sus capacidades de inyeccion de dependencias, gestion de configuracion y ecosistema de modulos.

### 5. Control de Versiones
*   **Mensajes de Commit:** Cada commit debe tener un mensaje descriptivo que explique claramente que cambios reales se realizaron en el codigo.
*   **Nomenclatura de Ramas:** Las ramas de desarrollo deben llevar un nombre que identifique claramente la tarea o funcionalidad que se esta implementando.

---
*Este documento es de cumplimiento obligatorio para todos los colaboradores del proyecto.*
