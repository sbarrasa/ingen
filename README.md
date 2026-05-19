# customer-product-json-fixed

Proyecto Maven en **Java 21 + Spring Boot 3 + MongoDB** para CRUD de `Customer` y documentos `Product` asociados a `customerId`.

La regeneración corrige el problema de serialización/deserialización JSON del campo `type` en productos.

## Stack

- Java 21
- Spring Boot 3.3.5
- Spring Web MVC
- Spring Data MongoDB
- Jakarta Validation
- Jackson
- JUnit 5 + Mockito
- Bruno para pruebas HTTP

## Estructura

```text
src/main/java/com/cvi/poc
├── CustomerProductApplication.java
├── commons/domain
│   ├── ApiError.java
│   ├── BadRequestException.java
│   ├── BusinessException.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── config
│   └── JacksonConfig.java
├── customers/domain
│   ├── CustomerController.java
│   ├── CustomerDocument.java
│   ├── CustomerMapper.java
│   ├── CustomerRepository.java
│   ├── CustomerRequest.java
│   ├── CustomerResponse.java
│   └── CustomerService.java
└── products/domain
    ├── ProductController.java
    ├── ProductDocument.java
    ├── ProductItem.java
    ├── ProductMapper.java
    ├── ProductRepository.java
    ├── ProductRequest.java
    ├── ProductResponse.java
    ├── ProductService.java
    └── ProductType.java
```

## Ejecutar local

```bash
docker compose up -d mongodb
mvn spring-boot:run
```

La API queda disponible en:

```text
http://localhost:8080
```

MongoDB local:

```text
mongodb://localhost:27017/customer_product_db
```

También podés cambiar la conexión con:

```bash
export MONGODB_URI=mongodb://localhost:27017/customer_product_db
```

## Endpoints Customers

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/v1/customers` | Lista customers |
| GET | `/api/v1/customers/{id}` | Busca customer por id |
| GET | `/api/v1/customers/search?cuit=20-12345678-9` | Busca customer por CUIT |
| POST | `/api/v1/customers` | Crea customer |
| PUT | `/api/v1/customers/{id}` | Actualiza customer |
| DELETE | `/api/v1/customers/{id}` | Borra customer |

### Crear customer

```json
{
  "names": "Sergio",
  "lastNames": "Rozenberg",
  "cuit": "20-12345678-9"
}
```

## Endpoints Products

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/v1/products` | Lista documentos de productos |
| GET | `/api/v1/products/{id}` | Busca documento de productos por id |
| GET | `/api/v1/products/customer/{customerId}` | Lista productos por customerId |
| POST | `/api/v1/products` | Crea documento de productos asociado a customer |
| PUT | `/api/v1/products/{id}` | Actualiza documento de productos |
| DELETE | `/api/v1/products/{id}` | Borra documento de productos |

### Crear documento de productos, payload recomendado

```json
{
  "customerId": "CUSTOMER_ID_REAL",
  "products": [
    {
      "type": "DEBIT_CARD",
      "name": "Main Debit Card",
      "description": "Salary account debit card",
      "active": true
    },
    {
      "type": "SAVING_ACCOUNT",
      "name": "Main Saving Account",
      "description": "Primary saving account",
      "active": true
    }
  ]
}
```

### Payload legacy soportado

El backend también acepta el formato viejo con `type` en raíz:

```json
{
  "customerId": "CUSTOMER_ID_REAL",
  "type": "CREDIT_CARD",
  "name": "Gold Credit Card",
  "description": "Root-level legacy product payload",
  "active": true
}
```

También acepta el alias `productType`:

```json
{
  "customerId": "CUSTOMER_ID_REAL",
  "productType": "CREDIT_CARD",
  "name": "Gold Credit Card"
}
```

## Valores válidos para ProductType

```text
DEBIT_CARD
CREDIT_CARD
SAVING_ACCOUNT
CREDIT_ACCOUNT
```

## Corrección aplicada sobre serialización JSON

La corrección principal está en `ProductType`:

```java
@JsonCreator
public static ProductType fromJson(String value) {
    if (value == null || value.isBlank()) {
        throw new IllegalArgumentException(
            "Product type is required. Use field 'type' with DEBIT_CARD, CREDIT_CARD, SAVING_ACCOUNT or CREDIT_ACCOUNT"
        );
    }
    return ProductType.valueOf(value.trim().toUpperCase());
}

@JsonValue
public String toJson() {
    return name();
}
```

Además:

- `ProductItem.type` usa `@JsonProperty("type")`.
- Se agregó `@JsonAlias("productType")` para compatibilidad.
- `ProductRequest` normaliza el payload recomendado con lista `products` y el payload legacy de un solo producto.
- `GlobalExceptionHandler` devuelve errores JSON claros para `400 Bad Request`.
- `application.yml` configura Jackson con `accept-case-insensitive-enums` y fechas ISO.

## Probar con Bruno

Importar la carpeta:

```text
bruno/
```

Flujo recomendado:

1. Ejecutar `Create Customer`.
2. Copiar el `id` devuelto.
3. Crear una variable Bruno llamada `customerId` con ese valor.
4. Ejecutar `Create Product List`.
5. Ejecutar `Create Single Product Legacy Payload` para validar compatibilidad.
6. Ejecutar `Invalid Product Type` y `Missing Product Type` para validar errores controlados.

## Tests

```bash
mvn clean test
```

Incluye pruebas de:

- Serialización/deserialización de `ProductType`.
- Deserialización de `ProductRequest` con lista `products`.
- Deserialización compatible con `productType`.
- Servicio de productos validando existencia de customer.
- Mapper de customer.
