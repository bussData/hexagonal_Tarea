# DIAGRAMA ENTIDAD-RELACIÓN (E/R)
## Sistema Banco Digital

---

##  Diagrama E/R

```
┌─────────────────────────────────┐
│          CLIENTE                │
├─────────────────────────────────┤
│ PK  id: VARCHAR(50)             │
│     nombre: VARCHAR(100)        │
│     email: VARCHAR(100)         │
│     documento: VARCHAR(20)      │
│     fecha_creacion: TIMESTAMP   │
└──────────────┬──────────────────┘
               │
               │ 1:N
               │ "tiene"
               │
               ↓
┌────────────────────────────────────┐
│          CUENTA                    │
├────────────────────────────────────┤
│ PK  cuenta_id: VARCHAR(50)         │
│ FK  cliente_id: VARCHAR(50)        │
│     numero_cuenta: VARCHAR(20)     │
│     saldo: DECIMAL(15,2)           │
│     estado: VARCHAR(20)            │
│     fecha_creacion: TIMESTAMP      │
│     fecha_actualizacion: TIMESTAMP │
└──────────┬────────────┬────────────┘
           │            │
           │            │
   "origen"│            │"destino"
         N │            │ N
           │            │
           │            │
           ↓            ↓
┌─────────────────────────────────┐
│        TRANSACCION              │
├─────────────────────────────────┤
│ PK  transaccion_id: VARCHAR(50) │
│ FK  cuenta_origen_id: VARCHAR   │
│ FK  cuenta_destino_id: VARCHAR  │
│     monto: DECIMAL(15,2)        │
│     comision: DECIMAL(15,2)     │
│     tipo: VARCHAR(20)           │
│     estado: VARCHAR(20)         │
│     descripcion: VARCHAR(255)   │
│     fecha_creacion: TIMESTAMP   │
└─────────────────────────────────┘
```

---

## 📋 ENTIDADES Y ATRIBUTOS

### 1. CLIENTE

| Atributo | Tipo | Restricción | Descripción |
|----------|------|-------------|-------------|
| **id** | VARCHAR(50) | PK, NOT NULL | Identificador único del cliente |
| nombre | VARCHAR(100) | NOT NULL | Nombre completo |
| email | VARCHAR(100) | NOT NULL, UNIQUE | Correo electrónico |
| documento | VARCHAR(20) | NOT NULL, UNIQUE | Documento de identidad |
| fecha_creacion | TIMESTAMP | NOT NULL | Fecha de registro |

**Validaciones:**
- Email formato válido
- Documento único en el sistema

---

### 2. CUENTA

| Atributo | Tipo | Restricción | Descripción |
|----------|------|-------------|-------------|
| **cuenta_id** | VARCHAR(50) | PK, NOT NULL | Identificador único de cuenta |
| **cliente_id** | VARCHAR(50) | FK, NOT NULL | Referencia a CLIENTE |
| numero_cuenta | VARCHAR(20) | NOT NULL, UNIQUE | Número de cuenta |
| saldo | DECIMAL(15,2) | NOT NULL | Saldo actual |
| estado | VARCHAR(20) | NOT NULL | Estado: ACTIVO, CERRADO |
| fecha_creacion | TIMESTAMP | NOT NULL | Fecha de creación |
| fecha_actualizacion | TIMESTAMP | NOT NULL | Última actualización |

**Validaciones:**
- saldo >= 0
- numero_cuenta único
- estado valores permitidos: 'ACTIVO', 'CERRADO'

**Relaciones:**
- Un cliente puede tener N cuentas (1:N con CLIENTE)
- Una cuenta participa en N transacciones

---

### 3. TRANSACCION

| Atributo | Tipo | Restricción | Descripción |
|----------|------|-------------|-------------|
| **transaccion_id** | VARCHAR(50) | PK, NOT NULL | ID único de transacción |
| **cuenta_origen_id** | VARCHAR(50) | FK, NOT NULL | Cuenta origen |
| **cuenta_destino_id** | VARCHAR(50) | FK, NOT NULL | Cuenta destino |
| monto | DECIMAL(15,2) | NOT NULL | Monto transferido |
| comision | DECIMAL(15,2) | NOT NULL | Comisión cobrada ($5) |
| tipo | VARCHAR(20) | NOT NULL | Tipo: TRANSFERENCIA |
| estado | VARCHAR(20) | NOT NULL | Estado de transacción |
| descripcion | VARCHAR(255) | NULL | Descripción opcional |
| fecha_creacion | TIMESTAMP | NOT NULL | Fecha de transacción |

**Validaciones:**
- monto > 0
- cuenta_origen_id ≠ cuenta_destino_id
- tipo valores: 'TRANSFERENCIA', 'DEPOSITO', 'RETIRO'
- estado valores: 'PENDIENTE', 'COMPLETADA', 'FALLIDA', 'CANCELADA'

**Relaciones:**
- Muchas transacciones a una cuenta origen (N:1)
- Muchas transacciones a una cuenta destino (N:1)

---

## 🔗 RELACIONES

### CLIENTE ←→ CUENTA (1:N)
```
Un cliente TIENE muchas cuentas
Una cuenta PERTENECE a un cliente
```

### CUENTA ←→ TRANSACCION (N:N)
```
Una cuenta puede ser ORIGEN de muchas transacciones
Una cuenta puede ser DESTINO de muchas transacciones
```


---

## SCRIPTS SQL

### Crear Tablas

```sql
-- Tabla CLIENTE
CREATE TABLE cliente (
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    documento VARCHAR(20) NOT NULL UNIQUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CHECK (email LIKE '%@%.%')
);

-- Tabla CUENTA
CREATE TABLE cuenta (
    cuenta_id VARCHAR(50) PRIMARY KEY,
    cliente_id VARCHAR(50) NOT NULL,
    numero_cuenta VARCHAR(20) NOT NULL UNIQUE,
    saldo DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    CHECK (saldo >= 0),
    CHECK (estado IN ('ACTIVO', 'CERRADO'))
);

-- Tabla TRANSACCION
CREATE TABLE transaccion (
    transaccion_id VARCHAR(50) PRIMARY KEY,
    cuenta_origen_id VARCHAR(50) NOT NULL,
    cuenta_destino_id VARCHAR(50) NOT NULL,
    monto DECIMAL(15,2) NOT NULL,
    comision DECIMAL(15,2) NOT NULL DEFAULT 5.00,
    tipo VARCHAR(20) NOT NULL DEFAULT 'TRANSFERENCIA',
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    descripcion VARCHAR(255),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cuenta_origen_id) REFERENCES cuenta(cuenta_id),
    FOREIGN KEY (cuenta_destino_id) REFERENCES cuenta(cuenta_id),
    CHECK (monto > 0),
    CHECK (cuenta_origen_id != cuenta_destino_id),
    CHECK (tipo IN ('TRANSFERENCIA', 'DEPOSITO', 'RETIRO')),
    CHECK (estado IN ('PENDIENTE', 'COMPLETADA', 'FALLIDA', 'CANCELADA'))
);

-- Índices para mejor performance
CREATE INDEX idx_cuenta_cliente ON cuenta(cliente_id);
CREATE INDEX idx_transaccion_origen ON transaccion(cuenta_origen_id);
CREATE INDEX idx_transaccion_destino ON transaccion(cuenta_destino_id);
CREATE INDEX idx_transaccion_fecha ON transaccion(fecha_creacion);
```

---

### Datos de Ejemplo

```sql
-- Insertar clientes
INSERT INTO cliente (id, nombre, email, documento) VALUES
('CLI001', 'Juan Pérez', 'juan.perez@email.com', '12345678'),
('CLI002', 'María García', 'maria.garcia@email.com', '87654321'),
('CLI003', 'Carlos López', 'carlos.lopez@email.com', '11223344');

-- Insertar cuentas
INSERT INTO cuenta (cuenta_id, cliente_id, numero_cuenta, saldo, estado) VALUES
('CTA001', 'CLI001', '0001-234567', 1000.00, 'ACTIVO'),
('CTA002', 'CLI002', '0001-234568', 2500.00, 'ACTIVO'),
('CTA003', 'CLI003', '0001-234569', 500.00, 'ACTIVO');

-- Insertar transacción de ejemplo
INSERT INTO transaccion 
(transaccion_id, cuenta_origen_id, cuenta_destino_id, monto, comision, tipo, estado, descripcion) 
VALUES
('TXN001', 'CTA001', 'CTA002', 100.00, 5.00, 'TRANSFERENCIA', 'COMPLETADA', 'Transferencia de prueba');
```