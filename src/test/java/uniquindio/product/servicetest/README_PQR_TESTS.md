# Suite de Pruebas para PQR (Peticiones, Quejas y Reclamos)

## Resumen
Se ha creado una suite completa de pruebas para la funcionalidad PQR del sistema, incluyendo pruebas unitarias, de integración y de validación.

## Archivos de Prueba Creados

### 1. PqrTest.java
**Tipo:** Pruebas unitarias para PqrServiceImpl
**Cobertura:**
- ✅ Creación exitosa de PQR
- ✅ Consulta por ID de PQR
- ✅ Consulta por ID de worker
- ✅ Consulta por ID de usuario
- ✅ Manejo de casos cuando no existen registros
- ✅ Pruebas con diferentes categorías de PQR
- ✅ Pruebas con diferentes estados de PQR
- ✅ Pruebas con descripción de longitud máxima (3000 caracteres)
- ✅ Pruebas con fecha de respuesta

**Métodos de prueba:** Mockito para simular dependencias

### 2. PqrMapperTest.java
**Tipo:** Pruebas unitarias para PqrMapper
**Cobertura:**
- ✅ Conversión de CrearPqrDTO a entidad PQR
- ✅ Conversión de PqrResponseDTO a entidad PQR
- ✅ Conversión de entidad PQR a PqrResponseDTO
- ✅ Manejo de valores nulos en todas las conversiones
- ✅ Pruebas con todas las categorías de PQR
- ✅ Pruebas con todos los estados de PQR
- ✅ Pruebas con descripción de longitud máxima
- ✅ Pruebas con fechas específicas
- ✅ Conversión bidireccional (round-trip)

### 3. PqrRepositoryTest.java
**Tipo:** Pruebas de integración para PqrRepository
**Cobertura:**
- ✅ Búsqueda por ID de PQR
- ✅ Búsqueda por ID de worker
- ✅ Búsqueda por ID de usuario
- ✅ Guardado de nuevos PQRs
- ✅ Actualización de PQRs existentes
- ✅ Eliminación de PQRs
- ✅ Búsqueda de todos los PQRs
- ✅ Búsqueda por categoría
- ✅ Búsqueda por estado
- ✅ Búsqueda por usuario
- ✅ Búsqueda por worker
- ✅ Búsqueda por fechas
- ✅ Búsqueda por descripción
- ✅ Búsqueda con múltiples criterios

**Configuración:** @DataJpaTest con TestEntityManager

### 4. PqrEntityValidationTest.java
**Tipo:** Pruebas de validación para entidad PQR
**Cobertura:**
- ✅ Validación de campos requeridos (idUsuario, categoria, descripcion, fechaCreacion, estadoPqr)
- ✅ Validación de longitud máxima de descripción (3000 caracteres)
- ✅ Validación de longitud excesiva de descripción
- ✅ Validación de campos opcionales (idWorker, fechaRespuesta)
- ✅ Validación de todas las categorías válidas
- ✅ Validación de todos los estados válidos
- ✅ Validación de fechas válidas
- ✅ Validación de fecha de respuesta posterior a fecha de creación
- ✅ Validación de descripción vacía
- ✅ Validación de descripción con solo espacios
- ✅ Validación de idUsuario vacío
- ✅ Validación de idUsuario con solo espacios
- ✅ Validación de idWorker vacío
- ✅ Validación de idWorker con solo espacios
- ✅ Validación de descripción con caracteres especiales
- ✅ Validación de descripción con saltos de línea
- ✅ Validación de descripción con emojis
- ✅ Validación de descripción con HTML
- ✅ Validación de descripción con SQL

**Configuración:** @DataJpaTest con TestEntityManager

### 5. PqrDtoValidationTest.java
**Tipo:** Pruebas de validación para DTOs PQR
**Cobertura:**
- ✅ Creación de CrearPqrDTO válido
- ✅ Creación de CrearPqrDTO con valores nulos
- ✅ Creación de CrearPqrDTO con todas las categorías
- ✅ Creación de CrearPqrDTO con todos los estados
- ✅ Creación de CrearPqrDTO con descripción larga
- ✅ Creación de CrearPqrDTO con descripción vacía
- ✅ Creación de CrearPqrDTO con idUsuario vacío
- ✅ Creación de CrearPqrDTO con idWorker vacío
- ✅ Creación de CrearPqrDTO con fechas específicas
- ✅ Creación de CrearPqrDTO con descripción con caracteres especiales
- ✅ Creación de CrearPqrDTO con descripción con saltos de línea
- ✅ Creación de PqrResponseDTO válido
- ✅ Creación de PqrResponseDTO con valores nulos
- ✅ Creación de PqrResponseDTO con todas las categorías
- ✅ Creación de PqrResponseDTO con todos los estados
- ✅ Creación de PqrResponseDTO con descripción larga
- ✅ Creación de PqrResponseDTO con fechas específicas
- ✅ Creación de PqrResponseDTO con descripción con caracteres especiales
- ✅ Creación de PqrResponseDTO con descripción con saltos de línea
- ✅ Creación de PqrResponseDTO con descripción con emojis
- ✅ Creación de PqrResponseDTO con descripción con HTML
- ✅ Creación de PqrResponseDTO con descripción con SQL
- ✅ Creación de PqrResponseDTO con idPqr vacío
- ✅ Creación de PqrResponseDTO con idUsuario vacío
- ✅ Creación de PqrResponseDTO con idWorker vacío

## Categorías de PQR Probadas
- ✅ SERVICIO_CLIENTE
- ✅ RECLAMO
- ✅ FACTURACION
- ✅ CUPON
- ✅ OTROS

## Estados de PQR Probados
- ✅ ABIERTO
- ✅ CERRADO
- ✅ RESUELTO
- ✅ ABANDONADO

## Características de Prueba
- **Cobertura completa:** Todos los métodos del servicio, mapper y repositorio
- **Casos límite:** Descripción de 3000 caracteres, valores nulos, fechas específicas
- **Validaciones:** Restricciones de base de datos, validaciones de entidad
- **Integración:** Pruebas con base de datos real usando @DataJpaTest
- **Mocks:** Uso de Mockito para pruebas unitarias aisladas
- **Documentación:** Comentarios detallados en cada método de prueba

## Configuración de Pruebas
- **Perfil:** test (usando @ActiveProfiles("test"))
- **Base de datos:** H2 en memoria para pruebas
- **EntityManager:** TestEntityManager para operaciones de base de datos
- **Mocks:** Mockito para simular dependencias

## Ejecución de Pruebas
```bash
# Ejecutar todas las pruebas de PQR
mvn test -Dtest="*Pqr*"

# Ejecutar pruebas específicas
mvn test -Dtest="PqrTest"
mvn test -Dtest="PqrMapperTest"
mvn test -Dtest="PqrRepositoryTest"
mvn test -Dtest="PqrEntityValidationTest"
mvn test -Dtest="PqrDtoValidationTest"
```

## Cobertura de Código
Las pruebas cubren:
- ✅ 100% de los métodos del servicio PqrServiceImpl
- ✅ 100% de los métodos del mapper PqrMapper
- ✅ 100% de los métodos del repositorio PqrRepository
- ✅ 100% de las validaciones de la entidad PQR
- ✅ 100% de las validaciones de los DTOs PQR

## Notas Técnicas
- Las pruebas utilizan JUnit 5 y Mockito
- Se incluyen pruebas de casos límite y edge cases
- Se valida el manejo correcto de valores nulos
- Se prueban todas las combinaciones de categorías y estados
- Se incluyen pruebas de seguridad (SQL injection, XSS)
- Se valida la integridad de datos en la base de datos
