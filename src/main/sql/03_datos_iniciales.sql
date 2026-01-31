-- datos_iniciales.sql - datos de prueba
-- proyecto asur - asociacion de sordos del uruguay

SET search_path TO proyecto;

-- tipos de documento
INSERT INTO tipo_documento (id_tipo_documento, descripcion) VALUES
(1, 'Cedula de Identidad'),
(2, 'Pasaporte'),
(3, 'DNI Extranjero'),
(4, 'Otro');

-- estados de usuario (segun documento: Sin validar, Activo, Inactivo)
INSERT INTO estado_usuario (id_estado_usuario, descripcion) VALUES
(1, 'Sin validar'),
(2, 'Activo'),
(3, 'Inactivo');

-- tipos de telefono
INSERT INTO tipo_telefono (id_tipo_telefono, descripcion) VALUES
(1, 'Celular'),
(2, 'Fijo'),
(3, 'Trabajo'),
(4, 'Otro');

-- estados de telefono
INSERT INTO estado_telefono (id_estado_telefono, descripcion) VALUES
(1, 'Activo'),
(2, 'Inactivo');

-- formas de pago
INSERT INTO forma_pago (id_forma_pago, descripcion) VALUES
(1, 'Efectivo'),
(2, 'Transferencia Bancaria'),
(3, 'Tarjeta de Debito'),
(4, 'Tarjeta de Credito'),
(5, 'MercadoPago'),
(6, 'PedidosYa'),
(7, 'Otro');

-- niveles lsu
INSERT INTO nivel_lsu (id_nivel_lsu, descripcion) VALUES
(1, 'Nativo'),
(2, 'Avanzado'),
(3, 'Intermedio'),
(4, 'No aplica');

-- descuentos cuota
INSERT INTO descuento_cuota (id_descuento, porcentaje, descripcion) VALUES
(0, 0, 'Sin descuento'),
(1, 5, 'Descuento 5%'),
(2, 10, 'Descuento 10%'),
(3, 15, 'Descuento 15%'),
(4, 20, 'Descuento 20%'),
(5, 25, 'Descuento 25%'),
(6, 30, 'Descuento 30%'),
(7, 35, 'Descuento 35%'),
(8, 40, 'Descuento 40%'),
(9, 45, 'Descuento 45%'),
(10, 50, 'Descuento 50%'),
(11, 55, 'Descuento 55%'),
(12, 60, 'Descuento 60%'),
(13, 65, 'Descuento 65%'),
(14, 70, 'Descuento 70%'),
(15, 75, 'Descuento 75%'),
(16, 80, 'Descuento 80%'),
(17, 85, 'Descuento 85%'),
(18, 90, 'Descuento 90%'),
(19, 95, 'Descuento 95%'),
(20, 100, 'Exento');

-- estados pago cuota
INSERT INTO estado_pago_cuota (id_estado_pago_cuota, descripcion) VALUES
(1, 'Pagada'),
(2, 'Pendiente'),
(3, 'Vencida');

-- estados pago reserva
INSERT INTO estado_pago_reserva (id_estado_pago_reserva, descripcion) VALUES
(1, 'Pagada'),
(2, 'Parcial'),
(3, 'Pendiente');

-- estados pago actividad
INSERT INTO estado_pago_actividad (id_estado_pago_actividad, descripcion) VALUES
(1, 'Pagada'),
(2, 'Parcial'),
(3, 'Pendiente');

-- perfiles
INSERT INTO perfil (nombre, descripcion, activo) VALUES
('Administrador', 'Acceso total al sistema', TRUE),
('Socio', 'Usuario socio de ASUR', TRUE),
('Auxiliar', 'Usuario auxiliar administrativo', TRUE),
('Invitado', 'Usuario sin privilegios especiales', TRUE);

-- funcionalidades
INSERT INTO funcionalidad (nombre, descripcion, activo) VALUES
('RF001-01', 'Registro de Usuario', TRUE),
('RF001-02', 'Listado de Usuarios', TRUE),
('RF001-03', 'Modificacion de Usuario', TRUE),
('RF001-04', 'Baja de Usuario', TRUE),
('RF001-05', 'Login de Usuario', TRUE),
('RF001-06', 'Modificacion de datos propios del Usuario', TRUE),
('RF002-01', 'Ingreso de Perfil', TRUE),
('RF002-02', 'Listado de Perfiles', TRUE),
('RF002-03', 'Modificacion de Perfil', TRUE),
('RF002-04', 'Baja de Perfil', TRUE),
('RF003-01', 'Alta de funcionalidad', TRUE),
('RF003-02', 'Listado de Funcionalidades', TRUE),
('RF003-03', 'Modificacion de Funcionalidad', TRUE),
('RF003-04', 'Baja de funcionalidad', TRUE),
('RF003-05', 'Acceso de funcionalidad', TRUE),
('RF003-06', 'Modificacion de Acceso de funcionalidad', TRUE),
('RF004-01', 'Auditoria de Usuarios', TRUE),
('RF004-02', 'Reporte de Auditoria de usuarios', TRUE),
('RF005-01', 'Ingreso de actividad', TRUE),
('RF005-02', 'Listado de actividades', TRUE),
('RF005-03', 'Modificacion de actividad', TRUE),
('RF005-04', 'Baja de actividad', TRUE),
('RF005-05', 'Listado de Actividades para inscribirse', TRUE),
('RF005-06', 'Inscripcion en Actividades', TRUE),
('RF005-07', 'Cancelacion de Inscripcion en Actividades', TRUE),
('RF005-08', 'Reporte de inscripciones-cancelaciones a actividades por Fechas', TRUE),
('RF005-09', 'Reporte de inscripciones-cancelaciones a actividades por tipo de actividad', TRUE),
('RF006-01', 'Ingreso de Espacios', TRUE),
('RF006-02', 'Listado de Espacios', TRUE),
('RF006-03', 'Modificacion de Espacio', TRUE),
('RF006-04', 'Baja de Espacio', TRUE),
('RF006-05', 'Reserva de Espacio', TRUE),
('RF006-06', 'Cancelacion de Reserva de Espacio', TRUE),
('RF006-07', 'Reporte de reservas-cancelaciones de espacios por Fechas', TRUE),
('RF006-08', 'Reporte de reservas-cancelaciones de espacios por Espacio', TRUE),
('RF007-01', 'Ingreso de Tipo de Actividad', TRUE),
('RF007-02', 'Listado de Tipos de Actividades', TRUE),
('RF007-03', 'Modificacion de Tipo de Actividad', TRUE),
('RF007-04', 'Baja de Tipo de Actividad', TRUE),
('RF008-01', 'Ingreso de Pago', TRUE),
('RF008-02', 'Listado de Pagos', TRUE),
('RF008-03', 'Modificacion de Pago', TRUE);

-- perfil_funcionalidad (administrador tiene todo)
INSERT INTO perfil_funcionalidad (id_perfil, id_funcionalidad)
SELECT 1, id_funcionalidad FROM funcionalidad;

-- perfil_funcionalidad (socio tiene acceso limitado)
INSERT INTO perfil_funcionalidad (id_perfil, id_funcionalidad) VALUES
(2, 5), (2, 6), (2, 20), (2, 23), (2, 24), (2, 25), (2, 29), (2, 32), (2, 33);

-- perfil_funcionalidad (auxiliar)
INSERT INTO perfil_funcionalidad (id_perfil, id_funcionalidad) VALUES
(3, 2), (3, 5), (3, 6), (3, 8), (3, 12), (3, 20), (3, 29), (3, 37);

-- categorias de socios
INSERT INTO categoria (descripcion, activo) VALUES
('Socio Regular', TRUE),
('Socio Fundador', TRUE),
('Socio Honorario', TRUE),
('Socio Estudiantil', TRUE),
('Socio Familiar', TRUE);

-- tipos de actividad
INSERT INTO tipo_actividad (nombre, descripcion, activo) VALUES
('Social', 'Actividades de caracter social', TRUE),
('Cultural', 'Actividades culturales y artisticas', TRUE),
('Deportiva', 'Actividades deportivas', TRUE),
('Educativa', 'Talleres y cursos', TRUE),
('Recreativa', 'Actividades de recreacion', TRUE);

-- subcomisiones
INSERT INTO subcomision (descripcion, activo) VALUES
('Comision Directiva', TRUE),
('Comision Deportes', TRUE),
('Comision Cultura', TRUE),
('Comision Social', TRUE),
('Comision Juventud', TRUE);

-- recursos/espacios (con precio diferenciado socio/no socio)
INSERT INTO recurso (nombre, descripcion, cap_max, costo_hora_socio, costo_hora_no_socio, fecha_vigencia_precios, activo) VALUES
('Salon Principal', 'Salon grande para eventos', 100, 400.00, 600.00, '2024-01-01', TRUE),
('Salon Secundario', 'Salon mediano multiuso', 50, 250.00, 400.00, '2024-01-01', TRUE),
('Sala de Reuniones', 'Sala pequena para reuniones', 15, 100.00, 200.00, '2024-01-01', TRUE),
('Cancha de Futbol', 'Cancha exterior', 22, 300.00, 500.00, '2024-01-01', TRUE),
('Patio Central', 'Espacio abierto', 200, 200.00, 350.00, '2024-01-01', TRUE);

-- valor cuota base
INSERT INTO valor_cuota_base (costo_cuota, fecha_vigencia, activo) VALUES
(350.00, '2024-01-01', FALSE),
(400.00, '2024-07-01', TRUE);

-- usuarios de prueba (password: asd123!@#)
-- hash bcrypt de "asd123!@#"
-- estado 2 = Activo
INSERT INTO usuario (nombre, apellido, documento, id_tipo_documento, correo, correo_confirmado, contrasena, calle, nro_puerta, id_estado_usuario, fec_nacimiento, id_perfil)
VALUES
('Admin', 'Sistema', '12345678', 1, 'admin@asur.uy', TRUE, '$2a$12$kUFNkJy2pMI7HbmCpAotkedqFaY3qh0qGGXGtIzzMPtfn41Y4Nymy', 'Hermanos Gil', '945', 2, '1990-01-01', 1),
('Maria', 'Rodriguez', '23456789', 1, 'maria@asur.uy', TRUE, '$2a$12$kUFNkJy2pMI7HbmCpAotkedqFaY3qh0qGGXGtIzzMPtfn41Y4Nymy', 'Av. 18 de Julio', '1234', 2, '1985-05-15', 2),
('Juan', 'Perez', '34567890', 1, 'juan@asur.uy', TRUE, '$2a$12$kUFNkJy2pMI7HbmCpAotkedqFaY3qh0qGGXGtIzzMPtfn41Y4Nymy', 'Bulevar Artigas', '567', 2, '1992-08-20', 3),
('Laura', 'Martinez', '45678901', 1, 'laura@asur.uy', FALSE, '$2a$12$kUFNkJy2pMI7HbmCpAotkedqFaY3qh0qGGXGtIzzMPtfn41Y4Nymy', 'Av. Brasil', '890', 1, '1988-03-10', 4);

-- telefonos
INSERT INTO telefono (numero, id_tipo_telefono, id_estado_telefono, id_usuario) VALUES
('099123456', 1, 1, 1),
('099234567', 1, 1, 2),
('24001234', 2, 1, 2),
('099345678', 1, 1, 3),
('099456789', 1, 1, 4);

-- administrador
INSERT INTO administrador (id_usuario, cargo) VALUES
(1, 'Administrador General');

-- socios
INSERT INTO cliente_asu (id_usuario, dificultad_auditiva, id_nivel_lsu, id_categoria, id_descuento, estado_socio) VALUES
(2, TRUE, 1, 1, 0, TRUE),
(4, FALSE, 4, 4, 2, TRUE);
