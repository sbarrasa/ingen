# Spring Boot Demo App

Esta es una aplicación base de Spring Boot configurada con las siguientes características:

- **Java:** 21
- **Spring Boot:** 3.2.5
- **Gestión de dependencias:** Maven
- **Actuator:** Configurado para monitoreo y salud.

## Cómo ejecutar

Para iniciar la aplicación, debes situarte en la raíz del módulo `spring-boot-demo` y usar el comando de Maven:

```powershell
cd spring-boot-demo
./mvnw spring-boot:run
```

## Endpoints de Actuator

Una vez que la aplicación esté corriendo, puedes probar los siguientes endpoints en tu navegador:

- **Actuator Index:** [http://localhost:8080/actuator](http://localhost:8080/actuator)
- **Health Check:** [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- **Info Endpoint:** [http://localhost:8080/actuator/info](http://localhost:8080/actuator/info)
