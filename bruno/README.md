# Bruno Collection - Customer Product API

Colección Bruno para probar la API local en `http://localhost:8080`.

## Orden recomendado

1. Customers / 01 - Create Customer
2. Customers / 02 - List Customers
3. Customers / 03 - Get Customer By Id
4. Customers / 04 - Search Customer By CUIT
5. Products / 01 - Create Product Document With Product List
6. Products / 02 - Create Product Legacy Root Type
7. Products / 03 - Create Product Using Alias productType
8. Products / 04 - List Products
9. Products / 05 - Get Product By Id
10. Products / 06 - Get Products By Customer
11. Products / 07 - Update Product Document
12. Negative Cases / 01 - Invalid Product Type
13. Negative Cases / 02 - Missing Product Type
14. Products / 08 - Delete Product
15. Customers / 05 - Update Customer
16. Customers / 06 - Delete Customer

## Variables

La colección usa environment `Local`:

- `baseUrl`: `http://localhost:8080`
- `customerId`: se intenta guardar automáticamente luego de crear customer.
- `productId`: se intenta guardar automáticamente luego de crear product.
- `cuit`: CUIT usado para búsqueda.

Si tu versión de Bruno no actualiza variables desde scripts, copiá manualmente el `id` devuelto por el response a las variables `customerId` y `productId`.

## Corrección clave de serialización JSON

Para products usar:

```json
{
  "customerId": "{{customerId}}",
  "products": [
    {
      "type": "DEBIT_CARD",
      "name": "Main Debit Card",
      "description": "Salary account debit card",
      "active": true
    }
  ]
}
```

También se incluye prueba de compatibilidad con `productType`, pero el contrato recomendado es `type`.
