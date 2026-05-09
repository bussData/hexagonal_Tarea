# TRABAJO FINAL - ARQUITECTURA DE SOFTWARE
## MÓDULO 2 : Patrones, Estilos Arquitectónicos, Componentes y Conectores
## Sistema de Gestión Bancaria

---

## PROBLEMA A RESOLVER

Desarrollar **"Banco Digital"**, un sistema bancario simple que permita:

- Crear cuentas bancarias
- Transferir dinero entre cuentas

---

##  REQUISITOS FUNCIONALES

### 1. Gestión Básica de Cuentas
- Crear cuenta (nombre, saldo inicial)
- Consultar saldo

### 2. Operación Principal
- Transferir dinero entre cuentas
- Validar saldo suficiente

### 3. Notificación Simple
- Notificar al cliente después de transferencia
- Soportar 1 canal (consola)

---

## 🏛️ REQUISITOS ARQUITECTÓNICOS

### Arquitectura: HEXAGONAL (Ports & Adapters)

**Estructura requerida:**
```
src/
├── domain/                 # DOMAIN (sin dependencias externas)
│   └── model/              # Entidades (Account, Transaction)
├── application/            # APPLICATION (sin dependencias externas)
│   ├── ports/
│   │   ├── input/          # Casos de uso (interfaces)
│   │   └── output/         # Interfaces para BD, notificaciones.
│   └── usecases/           # Lógica de negocio
│
└── infraestructure/        # INFRAESTRUCTURE
        ├── adapters/       # Adapter
        │      ├── input/   # REST API, CLI, etc.
        │      └── output/  # BD, APIs externas 
        └── config/         # Patrón Singleton

```

**Regla crítica:** El domain NO debe depender de adapters.

---

##  PATRONES DE DISEÑO OBLIGATORIOS

### 1. ADAPTER (mínimo 2 implementaciones)

### 2. SINGLETON (mínimo 1 implementación)

---

##  ADRs OBLIGATORIOS (mínimo 1)

Documentar decisiones arquitectónicas en formato Markdown:

### Plantilla ADR:
```markdown
# ADR-001: [Título]

## Estado
Aceptado

## Fecha
YYYY-MM-DD

## Contexto
[¿Qué problema resuelve? 2-3 líneas]

## Decisión
[¿Qué decidiste hacer? 1 línea]

## Alternativas Consideradas
1. Opción A
2. Opción B - ELEGIDA

## Consecuencias
### Positivas 
- Beneficio 1
- Beneficio 2

### Negativas 
- Desventaja 1
```

### ADRs Sugeridos:
1. **ADR-001:** Por qué usar Arquitectura Hexagonal
2. **ADR-002:** Sistema de persistencia (H2 vs Mysql)

---

## 📦 ENTREGABLES

### 1. Código Fuente (70%)
- Código en repositorio Git
- Estructura hexagonal clara
- README con instrucciones básicas de ejecución

### 2. Documentación (30%)
- **2 ADRs** en formato Markdown (carpeta `/docs`)
- **Diagrama simple** de arquitectura hexagonal
- README explicando brevemente los patrones

---

## 📊 EVALUACIÓN

| Criterio | Peso |
|----------|------|
| Arquitectura Hexagonal (separación correcta) | 25% |
| Patrón Adapter (2 implementaciones) | 25% |
| Patrón Singleton (1 implementación) | 15% |
| ADRs ( documento ) | 15% |
| Funcionalidad completa | 10% |
| Documentación | 10% |

---
