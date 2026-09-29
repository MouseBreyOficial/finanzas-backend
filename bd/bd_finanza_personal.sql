-- ============================================================================
-- FINANZA PERSONAL - SCRIPT FINAL PARA BASE DE DATOS NUEVA (PostgreSQL)
-- Estado del modelo: Backend V8 / Frontend V7.4
--
-- USO:
--   1. Crear una base de datos vacia (por ejemplo: finanza_personal_bd).
--   2. Conectarse a esa base.
--   3. Ejecutar SOLO este archivo.
--
-- IMPORTANTE:
--   - NO ejecutar despues los archivos migracion_*.sql en una BD nueva.
--   - Este script ya incluye las mejoras historicas de alertas, saldo_inicial
--     y nombre_usuario unico sin diferenciar mayusculas/minusculas.
--   - No crea un usuario Administrador porque la contrasena debe almacenarse
--     con BCrypt; el primer usuario puede registrarse desde la aplicacion/API.
-- ============================================================================

BEGIN;

-- ============================================================================
-- 1. USUARIOS
-- ============================================================================
CREATE SEQUENCE usuario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE usuarios (
    id_usuario          BIGINT       NOT NULL DEFAULT nextval('usuario_id_seq'),
    nombre_usuario      VARCHAR(100) NOT NULL,
    hash_contrasena     VARCHAR(255) NOT NULL,
    nombre_completo     VARCHAR(200),
    correo_electronico  VARCHAR(200),
    estado_registro     INTEGER      DEFAULT 1,
    descripcion_baja    VARCHAR(500),
    fecha_baja          TIMESTAMP,
    usuario_baja        VARCHAR(100),

    -- Auditoria (BaseModel)
    usuario_creacion     VARCHAR(100),
    fecha_creacion       TIMESTAMP,
    usuario_modificacion VARCHAR(100),
    fecha_modificacion   TIMESTAMP,

    CONSTRAINT pk_usuarios PRIMARY KEY (id_usuario)
);

ALTER SEQUENCE usuario_id_seq OWNED BY usuarios.id_usuario;

-- Unico ignorando mayusculas/minusculas y espacios externos.
-- Ej.: Administrador, administrador y " Administrador " son el mismo usuario.
CREATE UNIQUE INDEX uk_usuarios_nombre_usuario_lower
    ON usuarios (LOWER(TRIM(nombre_usuario)));

-- ============================================================================
-- 2. CUENTAS
--    saldo_inicial: referencia del dinero con el que se creo la cuenta.
--    saldo_actual : saldo que cambia con ingresos/gastos/pagos.
-- ============================================================================
CREATE SEQUENCE cuentas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE cuentas (
    id_cuenta           BIGINT       NOT NULL DEFAULT nextval('cuentas_id_seq'),
    nombre_cuenta       VARCHAR(100) NOT NULL,
    saldo_inicial       NUMERIC(12,2),
    saldo_actual        NUMERIC(12,2),
    id_usuario          BIGINT       NOT NULL,

    -- Auditoria (BaseModel)
    usuario_creacion     VARCHAR(100),
    fecha_creacion       TIMESTAMP,
    usuario_modificacion VARCHAR(100),
    fecha_modificacion   TIMESTAMP,

    CONSTRAINT pk_cuentas PRIMARY KEY (id_cuenta),
    CONSTRAINT fk_cuentas_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

ALTER SEQUENCE cuentas_id_seq OWNED BY cuentas.id_cuenta;
CREATE INDEX idx_cuentas_id_usuario ON cuentas(id_usuario);

-- ============================================================================
-- 3. INGRESOS
-- ============================================================================
CREATE SEQUENCE ingresos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE ingresos (
    id_ingreso          BIGINT        NOT NULL DEFAULT nextval('ingresos_id_seq'),
    monto               NUMERIC(12,2) NOT NULL,
    fecha               DATE          NOT NULL,
    descripcion         VARCHAR(255),
    id_cuenta           BIGINT        NOT NULL,

    -- Auditoria (BaseModel)
    usuario_creacion     VARCHAR(100),
    fecha_creacion       TIMESTAMP,
    usuario_modificacion VARCHAR(100),
    fecha_modificacion   TIMESTAMP,

    CONSTRAINT pk_ingresos PRIMARY KEY (id_ingreso),
    CONSTRAINT fk_ingresos_cuenta
        FOREIGN KEY (id_cuenta) REFERENCES cuentas(id_cuenta)
);

ALTER SEQUENCE ingresos_id_seq OWNED BY ingresos.id_ingreso;
CREATE INDEX idx_ingresos_id_cuenta ON ingresos(id_cuenta);
CREATE INDEX idx_ingresos_fecha ON ingresos(fecha);

-- ============================================================================
-- 4. GASTOS
--    categoria se conserva como texto porque la aplicacion reutiliza las
--    categorias existentes y permite escribir categorias nuevas.
-- ============================================================================
CREATE SEQUENCE gastos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE gastos (
    id_gasto            BIGINT        NOT NULL DEFAULT nextval('gastos_id_seq'),
    monto               NUMERIC(12,2) NOT NULL,
    fecha               DATE          NOT NULL,
    categoria           VARCHAR(100)  NOT NULL,
    descripcion         VARCHAR(255),
    id_cuenta           BIGINT        NOT NULL,

    -- Auditoria (BaseModel)
    usuario_creacion     VARCHAR(100),
    fecha_creacion       TIMESTAMP,
    usuario_modificacion VARCHAR(100),
    fecha_modificacion   TIMESTAMP,

    CONSTRAINT pk_gastos PRIMARY KEY (id_gasto),
    CONSTRAINT fk_gastos_cuenta
        FOREIGN KEY (id_cuenta) REFERENCES cuentas(id_cuenta)
);

ALTER SEQUENCE gastos_id_seq OWNED BY gastos.id_gasto;
CREATE INDEX idx_gastos_id_cuenta ON gastos(id_cuenta);
CREATE INDEX idx_gastos_fecha ON gastos(fecha);
CREATE INDEX idx_gastos_categoria_normalizada ON gastos(LOWER(TRIM(categoria)));

-- ============================================================================
-- 5. ALERTAS
--    Incluye todas las mejoras de alertas recurrentes y pago asociado.
-- ============================================================================
CREATE SEQUENCE alertas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE alertas (
    id_alerta           BIGINT       NOT NULL DEFAULT nextval('alertas_id_seq'),
    descripcion         VARCHAR(255) NOT NULL,
    fecha_alerta        DATE         NOT NULL,
    tipo                VARCHAR(50)  NOT NULL,

    -- Mejoras de alertas
    es_recurrente       BOOLEAN      DEFAULT FALSE,
    frecuencia          VARCHAR(20),
    dia_mes             INTEGER,
    estado              VARCHAR(20)  DEFAULT 'PENDIENTE',
    monto               NUMERIC(12,2),
    categoria           VARCHAR(100),
    id_cuenta           BIGINT,

    id_usuario          BIGINT       NOT NULL,

    -- Auditoria (BaseModel)
    usuario_creacion     VARCHAR(100),
    fecha_creacion       TIMESTAMP,
    usuario_modificacion VARCHAR(100),
    fecha_modificacion   TIMESTAMP,

    CONSTRAINT pk_alertas PRIMARY KEY (id_alerta),
    CONSTRAINT fk_alertas_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    CONSTRAINT fk_alertas_cuenta
        FOREIGN KEY (id_cuenta) REFERENCES cuentas(id_cuenta)
);

ALTER SEQUENCE alertas_id_seq OWNED BY alertas.id_alerta;
CREATE INDEX idx_alertas_id_usuario ON alertas(id_usuario);
CREATE INDEX idx_alertas_id_cuenta ON alertas(id_cuenta);
CREATE INDEX idx_alertas_fecha_estado ON alertas(fecha_alerta, estado);

COMMIT;

-- ============================================================================
-- FIN DEL SCRIPT
--
-- Incluido en este archivo:
--   [OK] usuarios + datos de baja/auditoria
--   [OK] nombre_usuario unico case-insensitive (LOWER + TRIM)
--   [OK] cuentas.saldo_inicial
--   [OK] cuentas.saldo_actual
--   [OK] ingresos
--   [OK] gastos + categoria
--   [OK] alertas.es_recurrente
--   [OK] alertas.frecuencia
--   [OK] alertas.dia_mes
--   [OK] alertas.estado
--   [OK] alertas.monto
--   [OK] alertas.categoria
--   [OK] alertas.id_cuenta
--   [OK] claves foraneas e indices de consulta
-- ============================================================================
