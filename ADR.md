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
- El dominio (`Account`, `Transaction`) es completamente independiente de frameworks y detalles técnicos.
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

