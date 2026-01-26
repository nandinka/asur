-- ddl.sql - definicion de objetos de base de datos
-- proyecto asur - asociacion de sordos del uruguay
-- schema: proyecto

SET search_path TO proyecto;

-- =====================================================
-- tablas de dominio (enumerados)
-- =====================================================

-- tipo de documento
CREATE TABLE tipo_documento (
    id_tipo_documento SMALLINT PRIMARY KEY,
    descripcion VARCHAR(50) NOT NULL UNIQUE
);

-- estado de usuario
CREATE TABLE estado_usuario (
    id_estado_usuario SMALLINT PRIMARY KEY,
    descripcion VARCHAR(30) NOT NULL UNIQUE
);

-- tipo de telefono
CREATE TABLE tipo_telefono (
    id_tipo_telefono SMALLINT PRIMARY KEY,
    descripcion VARCHAR(30) NOT NULL UNIQUE
);

-- estado de telefono
CREATE TABLE estado_telefono (
    id_estado_telefono SMALLINT PRIMARY KEY,
    descripcion VARCHAR(20) NOT NULL UNIQUE
);

-- forma de pago
CREATE TABLE forma_pago (
    id_forma_pago SMALLINT PRIMARY KEY,
    descripcion VARCHAR(50) NOT NULL UNIQUE
);

-- nivel lsu (lengua de senas uruguaya)
CREATE TABLE nivel_lsu (
    id_nivel_lsu SMALLINT PRIMARY KEY,
    descripcion VARCHAR(50) NOT NULL UNIQUE
);

-- descuento cuota
CREATE TABLE descuento_cuota (
    id_descuento SMALLINT PRIMARY KEY,
    porcentaje SMALLINT NOT NULL CHECK (porcentaje >= 0 AND porcentaje <= 100),
    descripcion VARCHAR(100)
);

-- estado pago cuota
CREATE TABLE estado_pago_cuota (
    id_estado_pago_cuota SMALLINT PRIMARY KEY,
    descripcion VARCHAR(30) NOT NULL UNIQUE
);

-- estado pago reserva
CREATE TABLE estado_pago_reserva (
    id_estado_pago_reserva SMALLINT PRIMARY KEY,
    descripcion VARCHAR(30) NOT NULL UNIQUE
);

-- estado pago actividad
CREATE TABLE estado_pago_actividad (
    id_estado_pago_actividad SMALLINT PRIMARY KEY,
    descripcion VARCHAR(30) NOT NULL UNIQUE
);

-- =====================================================
-- secuencias
-- =====================================================

CREATE SEQUENCE seq_usuario START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_telefono START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_perfil START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_funcionalidad START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_categoria START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_cliente_asu START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_administrador START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_valor_cuota_base START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_cuota START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_pago_cuota START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_recurso START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_reserva START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_pago_reserva START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_tipo_actividad START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_actividad START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_subcomision START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_auditoria START WITH 1 INCREMENT BY 1;

-- =====================================================
-- tablas principales
-- =====================================================

-- perfil
CREATE TABLE perfil (
    id_perfil INTEGER PRIMARY KEY DEFAULT nextval('seq_perfil'),
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- funcionalidad
CREATE TABLE funcionalidad (
    id_funcionalidad INTEGER PRIMARY KEY DEFAULT nextval('seq_funcionalidad'),
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- perfil_funcionalidad (relacion n:m)
CREATE TABLE perfil_funcionalidad (
    id_perfil INTEGER NOT NULL,
    id_funcionalidad INTEGER NOT NULL,
    PRIMARY KEY (id_perfil, id_funcionalidad),
    CONSTRAINT fk_pf_perfil FOREIGN KEY (id_perfil) REFERENCES perfil(id_perfil),
    CONSTRAINT fk_pf_funcionalidad FOREIGN KEY (id_funcionalidad) REFERENCES funcionalidad(id_funcionalidad)
);

-- usuario
CREATE TABLE usuario (
    id_usuario INTEGER PRIMARY KEY DEFAULT nextval('seq_usuario'),
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    documento VARCHAR(20) NOT NULL UNIQUE,
    id_tipo_documento SMALLINT NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    correo_confirmado BOOLEAN NOT NULL DEFAULT FALSE,
    contrasena VARCHAR(255) NOT NULL,
    calle VARCHAR(100),
    nro_puerta VARCHAR(10),
    apto VARCHAR(10),
    id_estado_usuario SMALLINT NOT NULL DEFAULT 1,
    fec_nacimiento DATE,
    id_perfil INTEGER NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_tipo_doc FOREIGN KEY (id_tipo_documento) REFERENCES tipo_documento(id_tipo_documento),
    CONSTRAINT fk_usuario_estado FOREIGN KEY (id_estado_usuario) REFERENCES estado_usuario(id_estado_usuario),
    CONSTRAINT fk_usuario_perfil FOREIGN KEY (id_perfil) REFERENCES perfil(id_perfil)
);

-- telefono
CREATE TABLE telefono (
    id_telefono INTEGER PRIMARY KEY DEFAULT nextval('seq_telefono'),
    numero VARCHAR(20) NOT NULL,
    id_tipo_telefono SMALLINT NOT NULL,
    id_estado_telefono SMALLINT NOT NULL DEFAULT 1,
    id_usuario INTEGER NOT NULL,
    CONSTRAINT fk_telefono_tipo FOREIGN KEY (id_tipo_telefono) REFERENCES tipo_telefono(id_tipo_telefono),
    CONSTRAINT fk_telefono_estado FOREIGN KEY (id_estado_telefono) REFERENCES estado_telefono(id_estado_telefono),
    CONSTRAINT fk_telefono_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- categoria (de socios)
CREATE TABLE categoria (
    id_categoria INTEGER PRIMARY KEY DEFAULT nextval('seq_categoria'),
    descripcion VARCHAR(100) NOT NULL UNIQUE,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- administrador (especializacion de usuario)
CREATE TABLE administrador (
    id_administrador INTEGER PRIMARY KEY DEFAULT nextval('seq_administrador'),
    id_usuario INTEGER NOT NULL UNIQUE,
    cargo VARCHAR(100),
    fecha_alta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_admin_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- cliente_asu / socio (especializacion de usuario)
CREATE TABLE cliente_asu (
    nro_socio INTEGER PRIMARY KEY DEFAULT nextval('seq_cliente_asu'),
    id_usuario INTEGER NOT NULL UNIQUE,
    dificultad_auditiva BOOLEAN NOT NULL DEFAULT FALSE,
    id_nivel_lsu SMALLINT NOT NULL DEFAULT 4,
    id_categoria INTEGER NOT NULL,
    id_descuento SMALLINT NOT NULL DEFAULT 0,
    estado_socio BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_alta_socio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cliente_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_cliente_nivel_lsu FOREIGN KEY (id_nivel_lsu) REFERENCES nivel_lsu(id_nivel_lsu),
    CONSTRAINT fk_cliente_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria),
    CONSTRAINT fk_cliente_descuento FOREIGN KEY (id_descuento) REFERENCES descuento_cuota(id_descuento)
);

-- valor_cuota_base
CREATE TABLE valor_cuota_base (
    id_valor_cuota INTEGER PRIMARY KEY DEFAULT nextval('seq_valor_cuota_base'),
    costo_cuota DECIMAL(10,2) NOT NULL CHECK (costo_cuota >= 0),
    fecha_vigencia DATE NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- cuota
CREATE TABLE cuota (
    id_cuota INTEGER PRIMARY KEY DEFAULT nextval('seq_cuota'),
    valor_cuota DECIMAL(10,2) NOT NULL CHECK (valor_cuota >= 0),
    mes SMALLINT NOT NULL CHECK (mes >= 1 AND mes <= 12),
    anio SMALLINT NOT NULL CHECK (anio >= 2000),
    id_estado_pago_cuota SMALLINT NOT NULL DEFAULT 2,
    nro_socio INTEGER NOT NULL,
    id_cuota_base INTEGER NOT NULL,
    fecha_generacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_cuota_socio_periodo UNIQUE (nro_socio, mes, anio),
    CONSTRAINT fk_cuota_estado FOREIGN KEY (id_estado_pago_cuota) REFERENCES estado_pago_cuota(id_estado_pago_cuota),
    CONSTRAINT fk_cuota_socio FOREIGN KEY (nro_socio) REFERENCES cliente_asu(nro_socio),
    CONSTRAINT fk_cuota_base FOREIGN KEY (id_cuota_base) REFERENCES valor_cuota_base(id_valor_cuota)
);

-- pago_cuota
CREATE TABLE pago_cuota (
    id_pago_cuota INTEGER PRIMARY KEY DEFAULT nextval('seq_pago_cuota'),
    id_cuota INTEGER NOT NULL,
    monto DECIMAL(10,2) NOT NULL CHECK (monto > 0),
    fecha_pago TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_forma_pago SMALLINT NOT NULL,
    observaciones VARCHAR(200),
    CONSTRAINT fk_pago_cuota_cuota FOREIGN KEY (id_cuota) REFERENCES cuota(id_cuota),
    CONSTRAINT fk_pago_cuota_forma FOREIGN KEY (id_forma_pago) REFERENCES forma_pago(id_forma_pago)
);

-- recurso (espacios/salones)
CREATE TABLE recurso (
    id_recurso INTEGER PRIMARY KEY DEFAULT nextval('seq_recurso'),
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    cap_max INTEGER NOT NULL CHECK (cap_max > 0),
    costo_hora DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (costo_hora >= 0),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- reserva
CREATE TABLE reserva (
    id_reserva INTEGER PRIMARY KEY DEFAULT nextval('seq_reserva'),
    datos_contacto VARCHAR(255),
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    cant_personas INTEGER NOT NULL CHECK (cant_personas > 0),
    monto_total DECIMAL(10,2) NOT NULL CHECK (monto_total >= 0),
    duracion TIME NOT NULL,
    id_estado_pago_reserva SMALLINT NOT NULL DEFAULT 3,
    id_usuario INTEGER NOT NULL,
    id_recurso INTEGER NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_reserva_estado FOREIGN KEY (id_estado_pago_reserva) REFERENCES estado_pago_reserva(id_estado_pago_reserva),
    CONSTRAINT fk_reserva_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_reserva_recurso FOREIGN KEY (id_recurso) REFERENCES recurso(id_recurso)
);

-- pago_reserva
CREATE TABLE pago_reserva (
    id_pago_reserva INTEGER PRIMARY KEY DEFAULT nextval('seq_pago_reserva'),
    id_reserva INTEGER NOT NULL UNIQUE,
    monto DECIMAL(10,2) NOT NULL CHECK (monto > 0),
    fecha_pago TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_forma_pago SMALLINT NOT NULL,
    observaciones VARCHAR(200),
    CONSTRAINT fk_pago_reserva_reserva FOREIGN KEY (id_reserva) REFERENCES reserva(id_reserva),
    CONSTRAINT fk_pago_reserva_forma FOREIGN KEY (id_forma_pago) REFERENCES forma_pago(id_forma_pago)
);

-- tipo_actividad
CREATE TABLE tipo_actividad (
    id_tipo_actividad INTEGER PRIMARY KEY DEFAULT nextval('seq_tipo_actividad'),
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- actividad
CREATE TABLE actividad (
    id_actividad INTEGER PRIMARY KEY DEFAULT nextval('seq_actividad'),
    nombre VARCHAR(100) NOT NULL,
    fecha_hora_actividad TIMESTAMP NOT NULL,
    id_recurso INTEGER,
    duracion INTERVAL,
    costo_ticket DECIMAL(10,2) DEFAULT 0,
    observaciones TEXT,
    descripcion TEXT,
    fecha_hora_inicio_insc TIMESTAMP,
    plazo_inscripcion INTERVAL,
    requiere_inscripcion BOOLEAN NOT NULL DEFAULT FALSE,
    id_tipo_actividad INTEGER NOT NULL,
    id_administrador INTEGER NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_actividad_recurso FOREIGN KEY (id_recurso) REFERENCES recurso(id_recurso),
    CONSTRAINT fk_actividad_tipo FOREIGN KEY (id_tipo_actividad) REFERENCES tipo_actividad(id_tipo_actividad),
    CONSTRAINT fk_actividad_admin FOREIGN KEY (id_administrador) REFERENCES administrador(id_administrador)
);

-- inscripcion_actividad
CREATE TABLE inscripcion_actividad (
    id_actividad INTEGER NOT NULL,
    id_usuario INTEGER NOT NULL,
    fecha_inscripcion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_cancelacion TIMESTAMP,
    PRIMARY KEY (id_actividad, id_usuario),
    CONSTRAINT fk_inscripcion_actividad FOREIGN KEY (id_actividad) REFERENCES actividad(id_actividad),
    CONSTRAINT fk_inscripcion_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- pago_actividad
CREATE TABLE pago_actividad (
    id_actividad INTEGER NOT NULL,
    id_usuario INTEGER NOT NULL,
    monto DECIMAL(10,2) NOT NULL CHECK (monto > 0),
    fecha_pago TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_forma_pago SMALLINT NOT NULL,
    id_estado_pago_actividad SMALLINT NOT NULL DEFAULT 1,
    PRIMARY KEY (id_actividad, id_usuario),
    CONSTRAINT fk_pago_act_actividad FOREIGN KEY (id_actividad) REFERENCES actividad(id_actividad),
    CONSTRAINT fk_pago_act_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_pago_act_forma FOREIGN KEY (id_forma_pago) REFERENCES forma_pago(id_forma_pago),
    CONSTRAINT fk_pago_act_estado FOREIGN KEY (id_estado_pago_actividad) REFERENCES estado_pago_actividad(id_estado_pago_actividad)
);

-- subcomision
CREATE TABLE subcomision (
    id_subcomision INTEGER PRIMARY KEY DEFAULT nextval('seq_subcomision'),
    descripcion VARCHAR(100) NOT NULL UNIQUE,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- auditoria
CREATE TABLE auditoria (
    id_auditoria INTEGER PRIMARY KEY DEFAULT nextval('seq_auditoria'),
    fecha_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_usuario INTEGER NOT NULL,
    id_funcionalidad INTEGER NOT NULL,
    operacion VARCHAR(50),
    detalle TEXT,
    ip_cliente VARCHAR(45),
    CONSTRAINT fk_auditoria_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_auditoria_funcionalidad FOREIGN KEY (id_funcionalidad) REFERENCES funcionalidad(id_funcionalidad)
);

-- =====================================================
-- indices
-- =====================================================

CREATE INDEX idx_usuario_documento ON usuario(documento);
CREATE INDEX idx_usuario_correo ON usuario(correo);
CREATE INDEX idx_telefono_usuario ON telefono(id_usuario);
CREATE INDEX idx_cliente_usuario ON cliente_asu(id_usuario);
CREATE INDEX idx_cuota_socio ON cuota(nro_socio);
CREATE INDEX idx_cuota_periodo ON cuota(anio, mes);
CREATE INDEX idx_vcb_fecha ON valor_cuota_base(fecha_vigencia);
CREATE INDEX idx_reserva_fecha ON reserva(fecha);
CREATE INDEX idx_reserva_usuario ON reserva(id_usuario);
CREATE INDEX idx_reserva_recurso ON reserva(id_recurso);
CREATE INDEX idx_actividad_fecha ON actividad(fecha_hora_actividad);
CREATE INDEX idx_actividad_tipo ON actividad(id_tipo_actividad);
CREATE INDEX idx_auditoria_fecha ON auditoria(fecha_hora);
CREATE INDEX idx_auditoria_usuario ON auditoria(id_usuario);

-- =====================================================
-- vistas
-- =====================================================

CREATE OR REPLACE VIEW vw_usuarios_completo AS
SELECT u.id_usuario, u.nombre, u.apellido, u.documento,
       td.descripcion as tipo_documento, u.correo, u.correo_confirmado,
       eu.descripcion as estado, p.nombre as perfil, u.fec_nacimiento
FROM usuario u
JOIN tipo_documento td ON u.id_tipo_documento = td.id_tipo_documento
JOIN estado_usuario eu ON u.id_estado_usuario = eu.id_estado_usuario
JOIN perfil p ON u.id_perfil = p.id_perfil;

CREATE OR REPLACE VIEW vw_socios_completo AS
SELECT c.nro_socio, u.nombre, u.apellido, u.documento, u.correo,
       cat.descripcion as categoria, nl.descripcion as nivel_lsu,
       dc.porcentaje as descuento, c.estado_socio
FROM cliente_asu c
JOIN usuario u ON c.id_usuario = u.id_usuario
JOIN categoria cat ON c.id_categoria = cat.id_categoria
JOIN nivel_lsu nl ON c.id_nivel_lsu = nl.id_nivel_lsu
JOIN descuento_cuota dc ON c.id_descuento = dc.id_descuento;

CREATE OR REPLACE VIEW vw_cuotas_pendientes AS
SELECT c.id_cuota, c.nro_socio, u.nombre, u.apellido, c.mes, c.anio,
       c.valor_cuota, epc.descripcion as estado
FROM cuota c
JOIN cliente_asu ca ON c.nro_socio = ca.nro_socio
JOIN usuario u ON ca.id_usuario = u.id_usuario
JOIN estado_pago_cuota epc ON c.id_estado_pago_cuota = epc.id_estado_pago_cuota
WHERE c.id_estado_pago_cuota != 1;

CREATE OR REPLACE VIEW vw_reservas_hoy AS
SELECT r.id_reserva, r.fecha, r.hora, r.duracion, r.cant_personas,
       rec.nombre as recurso, u.nombre || ' ' || u.apellido as usuario,
       epr.descripcion as estado_pago
FROM reserva r
JOIN recurso rec ON r.id_recurso = rec.id_recurso
JOIN usuario u ON r.id_usuario = u.id_usuario
JOIN estado_pago_reserva epr ON r.id_estado_pago_reserva = epr.id_estado_pago_reserva
WHERE r.fecha = CURRENT_DATE AND r.activa = TRUE;

-- =====================================================
-- trigger de auditoria (deshabilitado por defecto)
-- =====================================================

CREATE OR REPLACE FUNCTION fn_auditoria_usuario()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO auditoria (id_usuario, id_funcionalidad, operacion, detalle)
        VALUES (NEW.id_usuario, 1, 'INSERT', 'Nuevo usuario: ' || NEW.correo);
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO auditoria (id_usuario, id_funcionalidad, operacion, detalle)
        VALUES (NEW.id_usuario, 1, 'UPDATE', 'Actualizado usuario: ' || NEW.correo);
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO auditoria (id_usuario, id_funcionalidad, operacion, detalle)
        VALUES (OLD.id_usuario, 1, 'DELETE', 'Eliminado usuario: ' || OLD.correo);
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_auditoria_usuario
AFTER INSERT OR UPDATE OR DELETE ON usuario
FOR EACH ROW EXECUTE FUNCTION fn_auditoria_usuario();

-- deshabilitar trigger por defecto
ALTER TABLE usuario DISABLE TRIGGER trg_auditoria_usuario;
