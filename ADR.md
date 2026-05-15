# Architectural Decision Records (ADRs)
## Sistema Banco Digital

---

# ADR-001: Adopción de Arquitectura Hexagonal (Ports & Adapters)

## Estado
Aceptado

## Fecha
2026-05-11

## Contexto
El sistema "Banco Digital" necesita gestionar cuentas y transferencias entre clientes. Se requiere una arquitectura que
aísle la lógica de negocio (dominio) de los detalles de infraestructura (base de datos, APIs, notificaciones), permitiendo 
que el núcleo del sistema sea testeable, mantenible y extensible de forma independiente a los canales de entrada y salida.

## Decisión
Implementar la Arquitectura Hexagonal (Ports & Adapters) separando el sistema en tres capas: 
`domain`, `application` e `infrastructure`, donde el dominio no depende de ningún componente externo.

## Alternativas Consideradas
1. **Arquitectura en Capas Tradicional (Layered Architecture)** — Fácil de implementar, pero genera alto 
   acoplamiento entre capas y dificulta el reemplazo de infraestructura.

2. **Arquitectura Hexagonal (Ports & Adapters)** — ELEGIDA — Desacopla el dominio de la infraestructura 
   mediante interfaces (puertos), facilitando el testing unitario y la extensibilidad.

3. **Arquitectura MVC clásica** — Adecuada para aplicaciones web simples, pero no provee suficiente 
   separación entre lógica de negocio y persistencia en sistemas bancarios.

## Consecuencias

### Positivas
- El dominio (`Cliente`, `Cuenta`, `Transaccion`) es completamente independiente de frameworks y detalles técnicos.
- Los casos de uso pueden probarse unitariamente sin necesidad de base de datos real.
- Es posible reemplazar adaptadores (e.g., cambiar MySQL por H2) sin modificar la lógica de negocio.
- Facilita agregar nuevos canales de entrada (REST, CLI, etc.) sin afectar el núcleo del sistema.

### Negativas
- Mayor cantidad de interfaces y clases que una arquitectura simple en capas.
- Curva de aprendizaje más alta para desarrolladores que no conocen el patrón.

---

# ADR-002: Selección del Sistema de Persistencia (H2 vs MySQL)

## Estado
Aceptado

## Fecha
2026-05-11

## Contexto
El sistema requiere persistir entidades como `CLIENTE`, `CUENTA` y `TRANSACCION` con 
relaciones definidas (1:N entre cliente y cuenta, N:N entre cuenta y transacción). 
Se debe elegir un motor de base de datos que permita un desarrollo ágil, sea fácil de 
configurar en entornos locales y soporte las restricciones de integridad definidas en el modelo E/R.

## Decisión
Utilizar **H2** como base de datos embebida para el entorno de desarrollo/pruebas, 
con la posibilidad de migrar a **MySQL** en producción mediante el patrón Adapter en la capa de infraestructura.

## Alternativas Consideradas
1. **MySQL** — Motor robusto para producción, pero requiere instalación, configuración de servidor 
 y credenciales, lo que complica el desarrollo local y las pruebas automatizadas.

2. **H2 (Base de datos embebida)** — ELEGIDA — No requiere instalación externa, se 
 configura en memoria o en archivo, ideal para desarrollo y pruebas. La arquitectura 
 hexagonal permite cambiarla fácilmente por MySQL en producción.

## Consecuencias

### Positivas
- Configuración cero: H2 corre embebida en la JVM sin servicios externos.
- Ideal para pruebas de integración rápidas y reproducibles.
- El patrón Adapter en la capa `infrastructure/adapters/output` permite intercambiar H2 por MySQL 
  sin cambiar el dominio ni los casos de uso.
- Reduce el tiempo de setup del entorno de desarrollo.

### Negativas
- H2 no replica al 100% el comportamiento de MySQL en producción (diferencias en tipos de datos, funciones SQL).
- Los datos en memoria se pierden al reiniciar la aplicación (en modo in-memory).

---

# ADR-003: Estrategia de Validaciones en Tres Capas

## Estado
Aceptado

## Fecha
2026-05-13

## Contexto
El sistema requiere validar datos de entrada según las reglas definidas en el modelo E/R (`database.md`).
Se debe decidir en qué capa de la arquitectura hexagonal se ubica cada tipo de validación,
respetando el principio de que el dominio no dependa de infraestructura.

## Decisión
Distribuir las validaciones en tres capas con responsabilidades claras:

| Capa | Ubicación | Tipo de validación |
|------|-----------|-------------------|
| **Infraestructura** | `infraestructure/adapters/input/` (Request DTOs) | Formato HTTP: campos nulos, tamaños, regex (`@Valid`, `@NotBlank`, `@Email`, `@DecimalMin`) |
| **Aplicación** | `application/usecases/` (ServiceImpl) | Reglas con acceso a BD: unicidad de email, documento, número de cuenta |
| **Dominio** | `domain/model/` (Entidades) | Reglas puras de la entidad sin dependencias externas |

## Validaciones implementadas por entidad

### Cliente (`Cliente.java` + `ClienteServiceImpl.java`)
- `hasValidEmail()` → email contiene `@`, `.` y longitud > 5
- `hasValidNombre()` → nombre no nulo, mínimo 2 caracteres
- `hasValidDocumento()` → documento no nulo, mínimo 8 caracteres
- `existeByDocumento()` → documento único en BD (capa aplicación)
- `existeByEmail()` → email único al actualizar (capa aplicación)

### Cuenta (`Cuenta.java` + `CuentaServiceImpl.java`)
- `hasValidSaldo()` → saldo >= 0 (database.md: `saldo >= 0`)
- `hasValidEstado()` → estado es `'ACTIVO'` o `'CERRADO'`
- `hasValidNumCuenta()` → número de cuenta no nulo ni vacío
- `esCuentaUnica()` → número de cuenta único en BD (capa aplicación)

### Transaccion (`Transaccion.java` + `TransaccionServiceImpl.java`)
- `hasValidMonto()` → monto > 0 (database.md: `monto > 0`)
- `cuentasDistintas()` → cuenta_origen_id ≠ cuenta_destino_id
- `hasValidTipo()` → tipo es `'TRANSFERENCIA'`, `'DEPOSITO'` o `'RETIRO'`
- `hasValidEstado()` → estado es `'PENDIENTE'`, `'COMPLETADA'`, `'FALLIDA'` o `'CANCELADA'`
- Cuentas origen y destino en estado `'ACTIVO'` (capa aplicación)
- Saldo cuenta origen > 0 (capa aplicación)

## Consecuencias

### Positivas
- Las reglas de negocio puras viven en el dominio, independientes de Spring y JPA.
- Las validaciones HTTP (`@Valid`) detienen peticiones malformadas antes de llegar al dominio.
- Las validaciones de unicidad se centralizan en los casos de uso, donde tienen acceso a los puertos de repositorio.

### Negativas
- Mayor cantidad de métodos de validación distribuidos en distintas capas.
- Requiere disciplina para no mezclar capas (e.g., no llamar repositorios desde el dominio).

---

# ADR-004: Generación de IDs con UUID en Adaptadores de Salida

## Estado
Aceptado

## Fecha
2026-05-14

## Contexto
Las entidades `ClienteEntity` y `CuentaEntity` usan `@Id` de tipo `String` sin `@GeneratedValue`,
ya que el dominio define IDs de negocio como `VARCHAR(50)`. JPA no puede auto-generar IDs de tipo String,
lo que causaba `JpaSystemException: Identifier must be manually assigned before calling persist()` cuando
el request no incluía ID.

## Decisión
Generar un `UUID` en los **adaptadores de salida** (`ClienteRepositoryAdapter` y `CuentaRepositoryAdapter`)
justo antes de llamar a `jpaRepository.save()`, solo cuando el ID llega nulo o vacío.

```java
if (entity.getId() == null || entity.getId().trim().isEmpty()) {
    entity.setId(UUID.randomUUID().toString());
}
```

## Por qué en el Adaptador y no en el Dominio
Según la arquitectura hexagonal, el dominio no debe conocer detalles de persistencia.
Generar el ID en el adaptador respeta esta separación: el dominio recibe el ID ya asignado
al retornar la entidad guardada.

## Consecuencias

### Positivas
- El dominio no depende de lógica de generación de IDs.
- IDs únicos garantizados sin configuración adicional en JPA.
- Compatible con futura migración a MySQL sin cambios en el dominio.

### Negativas
- El ID se genera en infraestructura, no en el dominio (aceptable para este caso de uso).

---

# ADR-005: Uso de MapStruct para Mapeo entre Capas

## Estado
Aceptado

## Fecha
2026-05-11

## Contexto
La arquitectura hexagonal requiere transformar objetos entre capas:
`Request → Domain`, `Domain → Entity`, `Entity → Domain`, `Domain → Response`.
Hacer estos mapeos manualmente genera código repetitivo y propenso a errores.

## Decisión
Utilizar **MapStruct** (`@Mapper`, `@Mapping`) para generar automáticamente el código de mapeo
en tiempo de compilación, usando `componentModel = "spring"` para integración con el contexto de Spring.

## Mappers implementados
- `ClienteMapper` — mapea entre `ClienteRequest`, `Cliente` (dominio), `ClienteEntity`, `ClienteResponse`
- `CuentaMapper` — mapea entre `CuentaRequest`, `Cuenta` (dominio), `CuentaEntity`, `CuentaResponse`
- `TransaccionMapper` — mapea entre `TransaccionRequest`, `Transaccion` (dominio), `TransaccionEntity`, `TransaccionResponse`

## Nota sobre TransaccionRequest
`TransaccionRequest` recibe `cuentaOrigenId` y `cuentaDestinoId` como `String` (no como objetos).
El mapper usa `@Mapping(target = "cuentaOrigen.cuentaId", source = "cuentaOrigenId")` para construir
el objeto `Cuenta` del dominio con solo el ID, suficiente para que el servicio busque la cuenta completa en BD.

## Consecuencias

### Positivas
- Código de mapeo generado en compilación: sin overhead en runtime.
- Reduce boilerplate significativamente.
- Errores de mapeo detectados en tiempo de compilación.

### Negativas
- Curva de aprendizaje en anotaciones de MapStruct (`@BeanMapping`, `ignoreByDefault`, etc.).
- Configuraciones de mapeo complejas (objetos anidados) requieren anotaciones adicionales.

---

# ADR-006: Bean Validation en DTOs de Entrada HTTP

## Estado
Aceptado

## Fecha
2026-05-13

## Contexto
Se necesita validar el formato básico de las peticiones HTTP antes de que lleguen a los casos de uso,
para devolver `400 Bad Request` con mensajes claros cuando los datos son incorrectos.

## Decisión
Agregar `spring-boot-starter-validation` al `pom.xml` y anotar los campos de los DTOs Request
con anotaciones de Bean Validation. Activar con `@Valid` en los controllers.

## Anotaciones aplicadas

### `ClienteRequest`
- `@NotBlank` en `nombre`, `email`, `documento`
- `@Email` en `email`
- `@Size(min=2)` en `nombre`, `@Size(min=8)` en `documento`

### `CuentaRequest`
- `@NotNull` en `cliente`, `saldo`
- `@NotBlank` en `numCuenta`, `estado`
- `@DecimalMin("0.0")` en `saldo`
- `@Pattern(regexp = "ACTIVO|CERRADO")` en `estado`
- `@Size(max=20)` en `numCuenta`

## Consecuencias

### Positivas
- Errores de formato detectados antes de llegar al dominio.
- Respuesta `400 Bad Request` automática con detalle del campo inválido.

### Negativas
- Duplicación parcial con validaciones del dominio (intencional: cada capa protege su contrato).
