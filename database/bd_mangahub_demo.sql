-- MangaHub: base de demostración generada a partir de las 8 entidades JPA.
-- MySQL 8.0.16 o posterior. Datos ficticios para desarrollo y presentación.
-- Ejecutar el archivo completo en MySQL Workbench antes de iniciar Spring.
-- No elimina bases ni tablas. Se puede repetir sobre esta misma base demo.

CREATE DATABASE IF NOT EXISTS bd_mangahub_demo
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bd_mangahub_demo;
SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS tbl_usuario (
    id_usuario INT NOT NULL AUTO_INCREMENT,
    nombres VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(255) NOT NULL,
    activo BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id_usuario),
    CONSTRAINT uq_usuario_username UNIQUE (username)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS tbl_cliente (
    id_cliente INT NOT NULL AUTO_INCREMENT,
    dni VARCHAR(255) NOT NULL,
    nombres VARCHAR(255) NOT NULL,
    apellidos VARCHAR(255) NOT NULL,
    telefono VARCHAR(255),
    correo VARCHAR(255),
    activo BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id_cliente),
    CONSTRAINT uq_cliente_dni UNIQUE (dni),
    CONSTRAINT ck_cliente_dni CHECK (dni REGEXP '^[0-9]{8}$')
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS tbl_proveedor (
    id_proveedor INT NOT NULL AUTO_INCREMENT,
    ruc VARCHAR(255) NOT NULL,
    razon_social VARCHAR(255) NOT NULL,
    telefono VARCHAR(255),
    correo VARCHAR(255),
    activo BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id_proveedor),
    CONSTRAINT uq_proveedor_ruc UNIQUE (ruc),
    CONSTRAINT ck_proveedor_ruc CHECK (ruc REGEXP '^[0-9]{11}$')
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS tbl_editorial (
    id_editorial INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    pais VARCHAR(255) NOT NULL,
    PRIMARY KEY (id_editorial),
    CONSTRAINT uq_editorial_nombre UNIQUE (nombre)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS tbl_genero (
    id_genero INT NOT NULL AUTO_INCREMENT,
    descripcion VARCHAR(255) NOT NULL,
    PRIMARY KEY (id_genero),
    CONSTRAINT uq_genero_descripcion UNIQUE (descripcion)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS tbl_manga (
    id_manga INT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    num_tomo INT NOT NULL,
    autor VARCHAR(255) NOT NULL,
    precio DECIMAL(38,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    activo BIT(1) NOT NULL DEFAULT b'1',
    id_editorial INT NOT NULL,
    id_genero INT NOT NULL,
    id_proveedor INT NOT NULL,
    PRIMARY KEY (id_manga),
    CONSTRAINT uq_manga_titulo_tomo UNIQUE (titulo, num_tomo),
    CONSTRAINT ck_manga_tomo CHECK (num_tomo > 0),
    CONSTRAINT ck_manga_precio CHECK (precio >= 0),
    CONSTRAINT ck_manga_stock CHECK (stock >= 0),
    CONSTRAINT fk_manga_editorial FOREIGN KEY (id_editorial)
        REFERENCES tbl_editorial (id_editorial),
    CONSTRAINT fk_manga_genero FOREIGN KEY (id_genero)
        REFERENCES tbl_genero (id_genero),
    CONSTRAINT fk_manga_proveedor FOREIGN KEY (id_proveedor)
        REFERENCES tbl_proveedor (id_proveedor)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS tbl_venta (
    id_venta INT NOT NULL AUTO_INCREMENT,
    fecha_emision DATETIME(6) NOT NULL,
    id_cliente INT NOT NULL,
    id_usuario INT NOT NULL,
    monto_total DECIMAL(38,2) NOT NULL,
    activo BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id_venta),
    CONSTRAINT ck_venta_monto CHECK (monto_total >= 0),
    CONSTRAINT fk_venta_cliente FOREIGN KEY (id_cliente)
        REFERENCES tbl_cliente (id_cliente),
    CONSTRAINT fk_venta_usuario FOREIGN KEY (id_usuario)
        REFERENCES tbl_usuario (id_usuario)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS tbl_detalle_venta (
    id_detalle INT NOT NULL AUTO_INCREMENT,
    id_venta INT NOT NULL,
    id_manga INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(38,2) NOT NULL,
    subtotal DECIMAL(38,2) NOT NULL,
    PRIMARY KEY (id_detalle),
    CONSTRAINT uq_detalle_venta_manga UNIQUE (id_venta, id_manga),
    CONSTRAINT ck_detalle_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_detalle_precio CHECK (precio_unitario >= 0),
    CONSTRAINT ck_detalle_subtotal CHECK (subtotal = cantidad * precio_unitario),
    CONSTRAINT fk_detalle_venta FOREIGN KEY (id_venta)
        REFERENCES tbl_venta (id_venta) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_manga FOREIGN KEY (id_manga)
        REFERENCES tbl_manga (id_manga)
) ENGINE=InnoDB;

-- DATOS DE PRUEBA
-- El login existente compara username y password directamente.
-- Estas contraseñas son públicas y se incluyen únicamente para la demo local.
START TRANSACTION;

-- Usuarios de demostración: admin / MangaDemo123!, jose / VentasDemo123!

INSERT INTO tbl_usuario (nombres, username, password, rol, activo)
SELECT 'Administrador Demo', 'admin', 'MangaDemo123!', 'ADMIN', b'1'
WHERE NOT EXISTS (SELECT 1 FROM tbl_usuario WHERE username = 'admin');

INSERT INTO tbl_usuario (nombres, username, password, rol, activo)
SELECT 'José Demo', 'jose', 'VentasDemo123!', 'VENDEDOR', b'1'
WHERE NOT EXISTS (SELECT 1 FROM tbl_usuario WHERE username = 'jose');

INSERT INTO tbl_usuario (nombres, username, password, rol, activo)
SELECT 'Usuario Inactivo Demo', 'inactivo', 'Desactivado123!', 'VENDEDOR', b'0'
WHERE NOT EXISTS (SELECT 1 FROM tbl_usuario WHERE username = 'inactivo');

-- Clientes: nombres, documentos y contactos ficticios.

INSERT INTO tbl_cliente (dni, nombres, apellidos, telefono, correo, activo)
SELECT '70000001', 'Carla', 'Huamán', '900000001', 'cliente01@example.com', b'1'
WHERE NOT EXISTS (SELECT 1 FROM tbl_cliente WHERE dni = '70000001');

INSERT INTO tbl_cliente (dni, nombres, apellidos, telefono, correo, activo)
SELECT '70000002', 'Diego', 'Salas', '900000002', 'cliente02@example.com', b'1'
WHERE NOT EXISTS (SELECT 1 FROM tbl_cliente WHERE dni = '70000002');

INSERT INTO tbl_cliente (dni, nombres, apellidos, telefono, correo, activo)
SELECT '70000003', 'Lucía', 'Torres', '900000003', 'cliente03@example.com', b'1'
WHERE NOT EXISTS (SELECT 1 FROM tbl_cliente WHERE dni = '70000003');

INSERT INTO tbl_cliente (dni, nombres, apellidos, telefono, correo, activo)
SELECT '70000004', 'Mateo', 'Rojas', '900000004', 'cliente04@example.com', b'1'
WHERE NOT EXISTS (SELECT 1 FROM tbl_cliente WHERE dni = '70000004');

INSERT INTO tbl_cliente (dni, nombres, apellidos, telefono, correo, activo)
SELECT '70000005', 'Valeria', 'Ramos', '900000005', 'cliente05@example.com', b'1'
WHERE NOT EXISTS (SELECT 1 FROM tbl_cliente WHERE dni = '70000005');

INSERT INTO tbl_cliente (dni, nombres, apellidos, telefono, correo, activo)
SELECT '70000006', 'Bruno', 'Díaz', '900000006', 'cliente06@example.com', b'1'
WHERE NOT EXISTS (SELECT 1 FROM tbl_cliente WHERE dni = '70000006');

INSERT INTO tbl_cliente (dni, nombres, apellidos, telefono, correo, activo)
SELECT '70000007', 'Ana', 'Vega', '900000007', 'cliente07@example.com', b'1'
WHERE NOT EXISTS (SELECT 1 FROM tbl_cliente WHERE dni = '70000007');

INSERT INTO tbl_cliente (dni, nombres, apellidos, telefono, correo, activo)
SELECT '70000008', 'Luis', 'Flores', '900000008', 'cliente08@example.com', b'0'
WHERE NOT EXISTS (SELECT 1 FROM tbl_cliente WHERE dni = '70000008');

-- Proveedores: RUC ficticios de 11 dígitos, sin validación ante SUNAT.

INSERT INTO tbl_proveedor (ruc, razon_social, telefono, correo, activo)
SELECT '20111111111', 'Distribuidora Sakura Demo', '910000001', 'proveedor01@example.com', b'1'
WHERE NOT EXISTS (SELECT 1 FROM tbl_proveedor WHERE ruc = '20111111111');

INSERT INTO tbl_proveedor (ruc, razon_social, telefono, correo, activo)
SELECT '20222222222', 'Comercial Otaku Demo', '910000002', 'proveedor02@example.com', b'1'
WHERE NOT EXISTS (SELECT 1 FROM tbl_proveedor WHERE ruc = '20222222222');

INSERT INTO tbl_proveedor (ruc, razon_social, telefono, correo, activo)
SELECT '20333333333', 'Manga Express Demo', '910000003', 'proveedor03@example.com', b'1'
WHERE NOT EXISTS (SELECT 1 FROM tbl_proveedor WHERE ruc = '20333333333');

INSERT INTO tbl_proveedor (ruc, razon_social, telefono, correo, activo)
SELECT '20444444444', 'Importadora Neko Demo', '910000004', 'proveedor04@example.com', b'1'
WHERE NOT EXISTS (SELECT 1 FROM tbl_proveedor WHERE ruc = '20444444444');

INSERT INTO tbl_proveedor (ruc, razon_social, telefono, correo, activo)
SELECT '20555555555', 'Distribuidora Andina Demo', '910000005', 'proveedor05@example.com', b'1'
WHERE NOT EXISTS (SELECT 1 FROM tbl_proveedor WHERE ruc = '20555555555');

INSERT INTO tbl_proveedor (ruc, razon_social, telefono, correo, activo)
SELECT '20666666666', 'Proveedor Inactivo Demo', '910000006', 'proveedor06@example.com', b'0'
WHERE NOT EXISTS (SELECT 1 FROM tbl_proveedor WHERE ruc = '20666666666');

-- Editoriales y géneros para los combos del catálogo.

INSERT INTO tbl_editorial (nombre, pais)
SELECT 'Panini Manga', 'México'
WHERE NOT EXISTS (SELECT 1 FROM tbl_editorial WHERE nombre = 'Panini Manga');

INSERT INTO tbl_editorial (nombre, pais)
SELECT 'Ivrea', 'Argentina'
WHERE NOT EXISTS (SELECT 1 FROM tbl_editorial WHERE nombre = 'Ivrea');

INSERT INTO tbl_editorial (nombre, pais)
SELECT 'Norma Editorial', 'España'
WHERE NOT EXISTS (SELECT 1 FROM tbl_editorial WHERE nombre = 'Norma Editorial');

INSERT INTO tbl_editorial (nombre, pais)
SELECT 'Distrito Manga', 'España'
WHERE NOT EXISTS (SELECT 1 FROM tbl_editorial WHERE nombre = 'Distrito Manga');

INSERT INTO tbl_genero (descripcion)
SELECT 'Shonen'
WHERE NOT EXISTS (SELECT 1 FROM tbl_genero WHERE descripcion = 'Shonen');

INSERT INTO tbl_genero (descripcion)
SELECT 'Seinen'
WHERE NOT EXISTS (SELECT 1 FROM tbl_genero WHERE descripcion = 'Seinen');

INSERT INTO tbl_genero (descripcion)
SELECT 'Comedia'
WHERE NOT EXISTS (SELECT 1 FROM tbl_genero WHERE descripcion = 'Comedia');

INSERT INTO tbl_genero (descripcion)
SELECT 'Deportes'
WHERE NOT EXISTS (SELECT 1 FROM tbl_genero WHERE descripcion = 'Deportes');

INSERT INTO tbl_genero (descripcion)
SELECT 'Fantasía'
WHERE NOT EXISTS (SELECT 1 FROM tbl_genero WHERE descripcion = 'Fantasía');

INSERT INTO tbl_genero (descripcion)
SELECT 'Suspenso'
WHERE NOT EXISTS (SELECT 1 FROM tbl_genero WHERE descripcion = 'Suspenso');

-- Catálogo ilustrativo: precios, stock y relaciones comerciales son datos de prueba.

INSERT INTO tbl_manga (titulo, num_tomo, autor, precio, stock, activo, id_editorial, id_genero, id_proveedor)
SELECT 'Naruto', 1, 'Masashi Kishimoto', 39.90, 18, b'1', (SELECT id_editorial FROM tbl_editorial WHERE nombre = 'Panini Manga'), (SELECT id_genero FROM tbl_genero WHERE descripcion = 'Shonen'), (SELECT id_proveedor FROM tbl_proveedor WHERE ruc = '20111111111')
WHERE NOT EXISTS (SELECT 1 FROM tbl_manga WHERE titulo = 'Naruto' AND num_tomo = 1);

INSERT INTO tbl_manga (titulo, num_tomo, autor, precio, stock, activo, id_editorial, id_genero, id_proveedor)
SELECT 'Naruto', 2, 'Masashi Kishimoto', 39.90, 14, b'1', (SELECT id_editorial FROM tbl_editorial WHERE nombre = 'Panini Manga'), (SELECT id_genero FROM tbl_genero WHERE descripcion = 'Shonen'), (SELECT id_proveedor FROM tbl_proveedor WHERE ruc = '20111111111')
WHERE NOT EXISTS (SELECT 1 FROM tbl_manga WHERE titulo = 'Naruto' AND num_tomo = 2);

INSERT INTO tbl_manga (titulo, num_tomo, autor, precio, stock, activo, id_editorial, id_genero, id_proveedor)
SELECT 'One Piece', 1, 'Eiichiro Oda', 42.90, 20, b'1', (SELECT id_editorial FROM tbl_editorial WHERE nombre = 'Ivrea'), (SELECT id_genero FROM tbl_genero WHERE descripcion = 'Shonen'), (SELECT id_proveedor FROM tbl_proveedor WHERE ruc = '20222222222')
WHERE NOT EXISTS (SELECT 1 FROM tbl_manga WHERE titulo = 'One Piece' AND num_tomo = 1);

INSERT INTO tbl_manga (titulo, num_tomo, autor, precio, stock, activo, id_editorial, id_genero, id_proveedor)
SELECT 'One Piece', 2, 'Eiichiro Oda', 42.90, 16, b'1', (SELECT id_editorial FROM tbl_editorial WHERE nombre = 'Ivrea'), (SELECT id_genero FROM tbl_genero WHERE descripcion = 'Shonen'), (SELECT id_proveedor FROM tbl_proveedor WHERE ruc = '20222222222')
WHERE NOT EXISTS (SELECT 1 FROM tbl_manga WHERE titulo = 'One Piece' AND num_tomo = 2);

INSERT INTO tbl_manga (titulo, num_tomo, autor, precio, stock, activo, id_editorial, id_genero, id_proveedor)
SELECT 'Demon Slayer', 1, 'Koyoharu Gotouge', 44.90, 18, b'1', (SELECT id_editorial FROM tbl_editorial WHERE nombre = 'Norma Editorial'), (SELECT id_genero FROM tbl_genero WHERE descripcion = 'Shonen'), (SELECT id_proveedor FROM tbl_proveedor WHERE ruc = '20333333333')
WHERE NOT EXISTS (SELECT 1 FROM tbl_manga WHERE titulo = 'Demon Slayer' AND num_tomo = 1);

INSERT INTO tbl_manga (titulo, num_tomo, autor, precio, stock, activo, id_editorial, id_genero, id_proveedor)
SELECT 'Death Note', 1, 'Tsugumi Ohba', 49.90, 12, b'1', (SELECT id_editorial FROM tbl_editorial WHERE nombre = 'Norma Editorial'), (SELECT id_genero FROM tbl_genero WHERE descripcion = 'Suspenso'), (SELECT id_proveedor FROM tbl_proveedor WHERE ruc = '20333333333')
WHERE NOT EXISTS (SELECT 1 FROM tbl_manga WHERE titulo = 'Death Note' AND num_tomo = 1);

INSERT INTO tbl_manga (titulo, num_tomo, autor, precio, stock, activo, id_editorial, id_genero, id_proveedor)
SELECT 'Spy x Family', 1, 'Tatsuya Endo', 45.90, 15, b'1', (SELECT id_editorial FROM tbl_editorial WHERE nombre = 'Ivrea'), (SELECT id_genero FROM tbl_genero WHERE descripcion = 'Comedia'), (SELECT id_proveedor FROM tbl_proveedor WHERE ruc = '20444444444')
WHERE NOT EXISTS (SELECT 1 FROM tbl_manga WHERE titulo = 'Spy x Family' AND num_tomo = 1);

INSERT INTO tbl_manga (titulo, num_tomo, autor, precio, stock, activo, id_editorial, id_genero, id_proveedor)
SELECT 'Chainsaw Man', 1, 'Tatsuki Fujimoto', 46.90, 10, b'1', (SELECT id_editorial FROM tbl_editorial WHERE nombre = 'Norma Editorial'), (SELECT id_genero FROM tbl_genero WHERE descripcion = 'Shonen'), (SELECT id_proveedor FROM tbl_proveedor WHERE ruc = '20444444444')
WHERE NOT EXISTS (SELECT 1 FROM tbl_manga WHERE titulo = 'Chainsaw Man' AND num_tomo = 1);

INSERT INTO tbl_manga (titulo, num_tomo, autor, precio, stock, activo, id_editorial, id_genero, id_proveedor)
SELECT 'Jujutsu Kaisen', 1, 'Gege Akutami', 43.90, 14, b'1', (SELECT id_editorial FROM tbl_editorial WHERE nombre = 'Panini Manga'), (SELECT id_genero FROM tbl_genero WHERE descripcion = 'Shonen'), (SELECT id_proveedor FROM tbl_proveedor WHERE ruc = '20111111111')
WHERE NOT EXISTS (SELECT 1 FROM tbl_manga WHERE titulo = 'Jujutsu Kaisen' AND num_tomo = 1);

INSERT INTO tbl_manga (titulo, num_tomo, autor, precio, stock, activo, id_editorial, id_genero, id_proveedor)
SELECT 'Attack on Titan', 1, 'Hajime Isayama', 47.90, 12, b'1', (SELECT id_editorial FROM tbl_editorial WHERE nombre = 'Norma Editorial'), (SELECT id_genero FROM tbl_genero WHERE descripcion = 'Shonen'), (SELECT id_proveedor FROM tbl_proveedor WHERE ruc = '20555555555')
WHERE NOT EXISTS (SELECT 1 FROM tbl_manga WHERE titulo = 'Attack on Titan' AND num_tomo = 1);

INSERT INTO tbl_manga (titulo, num_tomo, autor, precio, stock, activo, id_editorial, id_genero, id_proveedor)
SELECT 'Berserk', 1, 'Kentaro Miura', 59.90, 9, b'1', (SELECT id_editorial FROM tbl_editorial WHERE nombre = 'Panini Manga'), (SELECT id_genero FROM tbl_genero WHERE descripcion = 'Seinen'), (SELECT id_proveedor FROM tbl_proveedor WHERE ruc = '20555555555')
WHERE NOT EXISTS (SELECT 1 FROM tbl_manga WHERE titulo = 'Berserk' AND num_tomo = 1);

INSERT INTO tbl_manga (titulo, num_tomo, autor, precio, stock, activo, id_editorial, id_genero, id_proveedor)
SELECT 'Blue Lock', 1, 'Muneyuki Kaneshiro', 48.90, 11, b'1', (SELECT id_editorial FROM tbl_editorial WHERE nombre = 'Distrito Manga'), (SELECT id_genero FROM tbl_genero WHERE descripcion = 'Deportes'), (SELECT id_proveedor FROM tbl_proveedor WHERE ruc = '20222222222')
WHERE NOT EXISTS (SELECT 1 FROM tbl_manga WHERE titulo = 'Blue Lock' AND num_tomo = 1);

INSERT INTO tbl_manga (titulo, num_tomo, autor, precio, stock, activo, id_editorial, id_genero, id_proveedor)
SELECT 'My Hero Academia', 1, 'Kohei Horikoshi', 41.90, 8, b'1', (SELECT id_editorial FROM tbl_editorial WHERE nombre = 'Ivrea'), (SELECT id_genero FROM tbl_genero WHERE descripcion = 'Shonen'), (SELECT id_proveedor FROM tbl_proveedor WHERE ruc = '20444444444')
WHERE NOT EXISTS (SELECT 1 FROM tbl_manga WHERE titulo = 'My Hero Academia' AND num_tomo = 1);

INSERT INTO tbl_manga (titulo, num_tomo, autor, precio, stock, activo, id_editorial, id_genero, id_proveedor)
SELECT 'Frieren', 1, 'Kanehito Yamada', 52.90, 6, b'1', (SELECT id_editorial FROM tbl_editorial WHERE nombre = 'Distrito Manga'), (SELECT id_genero FROM tbl_genero WHERE descripcion = 'Fantasía'), (SELECT id_proveedor FROM tbl_proveedor WHERE ruc = '20555555555')
WHERE NOT EXISTS (SELECT 1 FROM tbl_manga WHERE titulo = 'Frieren' AND num_tomo = 1);

INSERT INTO tbl_manga (titulo, num_tomo, autor, precio, stock, activo, id_editorial, id_genero, id_proveedor)
SELECT 'Tokyo Ghoul', 1, 'Sui Ishida', 54.90, 0, b'0', (SELECT id_editorial FROM tbl_editorial WHERE nombre = 'Norma Editorial'), (SELECT id_genero FROM tbl_genero WHERE descripcion = 'Seinen'), (SELECT id_proveedor FROM tbl_proveedor WHERE ruc = '20666666666')
WHERE NOT EXISTS (SELECT 1 FROM tbl_manga WHERE titulo = 'Tokyo Ghoul' AND num_tomo = 1);

-- Ventas y detalles. El stock anterior es el saldo de demostración ya cargado.
-- Los importes de cada venta coinciden con la suma de sus detalles.

INSERT INTO tbl_venta (fecha_emision, id_cliente, id_usuario, monto_total, activo)
SELECT '2026-10-15 10:00:00', (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000001'), (SELECT id_usuario FROM tbl_usuario WHERE username = 'admin'), 122.70, b'1'
WHERE NOT EXISTS (SELECT 1 FROM tbl_venta WHERE fecha_emision = '2026-10-15 10:00:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000001') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'admin'));

INSERT INTO tbl_detalle_venta (id_venta, id_manga, cantidad, precio_unitario, subtotal)
SELECT (SELECT id_venta FROM tbl_venta WHERE fecha_emision = '2026-10-15 10:00:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000001') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'admin') LIMIT 1), (SELECT id_manga FROM tbl_manga WHERE titulo = 'Naruto' AND num_tomo = 1), 2, 39.90, 79.80
WHERE NOT EXISTS (SELECT 1 FROM tbl_detalle_venta WHERE id_venta = (SELECT id_venta FROM tbl_venta WHERE fecha_emision = '2026-10-15 10:00:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000001') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'admin') LIMIT 1) AND id_manga = (SELECT id_manga FROM tbl_manga WHERE titulo = 'Naruto' AND num_tomo = 1));

INSERT INTO tbl_detalle_venta (id_venta, id_manga, cantidad, precio_unitario, subtotal)
SELECT (SELECT id_venta FROM tbl_venta WHERE fecha_emision = '2026-10-15 10:00:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000001') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'admin') LIMIT 1), (SELECT id_manga FROM tbl_manga WHERE titulo = 'One Piece' AND num_tomo = 1), 1, 42.90, 42.90
WHERE NOT EXISTS (SELECT 1 FROM tbl_detalle_venta WHERE id_venta = (SELECT id_venta FROM tbl_venta WHERE fecha_emision = '2026-10-15 10:00:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000001') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'admin') LIMIT 1) AND id_manga = (SELECT id_manga FROM tbl_manga WHERE titulo = 'One Piece' AND num_tomo = 1));

INSERT INTO tbl_venta (fecha_emision, id_cliente, id_usuario, monto_total, activo)
SELECT '2026-10-16 11:00:00', (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000002'), (SELECT id_usuario FROM tbl_usuario WHERE username = 'jose'), 136.70, b'1'
WHERE NOT EXISTS (SELECT 1 FROM tbl_venta WHERE fecha_emision = '2026-10-16 11:00:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000002') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'jose'));

INSERT INTO tbl_detalle_venta (id_venta, id_manga, cantidad, precio_unitario, subtotal)
SELECT (SELECT id_venta FROM tbl_venta WHERE fecha_emision = '2026-10-16 11:00:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000002') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'jose') LIMIT 1), (SELECT id_manga FROM tbl_manga WHERE titulo = 'Demon Slayer' AND num_tomo = 1), 1, 44.90, 44.90
WHERE NOT EXISTS (SELECT 1 FROM tbl_detalle_venta WHERE id_venta = (SELECT id_venta FROM tbl_venta WHERE fecha_emision = '2026-10-16 11:00:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000002') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'jose') LIMIT 1) AND id_manga = (SELECT id_manga FROM tbl_manga WHERE titulo = 'Demon Slayer' AND num_tomo = 1));

INSERT INTO tbl_detalle_venta (id_venta, id_manga, cantidad, precio_unitario, subtotal)
SELECT (SELECT id_venta FROM tbl_venta WHERE fecha_emision = '2026-10-16 11:00:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000002') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'jose') LIMIT 1), (SELECT id_manga FROM tbl_manga WHERE titulo = 'Spy x Family' AND num_tomo = 1), 2, 45.90, 91.80
WHERE NOT EXISTS (SELECT 1 FROM tbl_detalle_venta WHERE id_venta = (SELECT id_venta FROM tbl_venta WHERE fecha_emision = '2026-10-16 11:00:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000002') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'jose') LIMIT 1) AND id_manga = (SELECT id_manga FROM tbl_manga WHERE titulo = 'Spy x Family' AND num_tomo = 1));

INSERT INTO tbl_venta (fecha_emision, id_cliente, id_usuario, monto_total, activo)
SELECT '2026-10-17 15:30:00', (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000003'), (SELECT id_usuario FROM tbl_usuario WHERE username = 'jose'), 108.80, b'1'
WHERE NOT EXISTS (SELECT 1 FROM tbl_venta WHERE fecha_emision = '2026-10-17 15:30:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000003') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'jose'));

INSERT INTO tbl_detalle_venta (id_venta, id_manga, cantidad, precio_unitario, subtotal)
SELECT (SELECT id_venta FROM tbl_venta WHERE fecha_emision = '2026-10-17 15:30:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000003') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'jose') LIMIT 1), (SELECT id_manga FROM tbl_manga WHERE titulo = 'Berserk' AND num_tomo = 1), 1, 59.90, 59.90
WHERE NOT EXISTS (SELECT 1 FROM tbl_detalle_venta WHERE id_venta = (SELECT id_venta FROM tbl_venta WHERE fecha_emision = '2026-10-17 15:30:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000003') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'jose') LIMIT 1) AND id_manga = (SELECT id_manga FROM tbl_manga WHERE titulo = 'Berserk' AND num_tomo = 1));

INSERT INTO tbl_detalle_venta (id_venta, id_manga, cantidad, precio_unitario, subtotal)
SELECT (SELECT id_venta FROM tbl_venta WHERE fecha_emision = '2026-10-17 15:30:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000003') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'jose') LIMIT 1), (SELECT id_manga FROM tbl_manga WHERE titulo = 'Blue Lock' AND num_tomo = 1), 1, 48.90, 48.90
WHERE NOT EXISTS (SELECT 1 FROM tbl_detalle_venta WHERE id_venta = (SELECT id_venta FROM tbl_venta WHERE fecha_emision = '2026-10-17 15:30:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000003') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'jose') LIMIT 1) AND id_manga = (SELECT id_manga FROM tbl_manga WHERE titulo = 'Blue Lock' AND num_tomo = 1));

INSERT INTO tbl_venta (fecha_emision, id_cliente, id_usuario, monto_total, activo)
SELECT '2026-10-17 17:00:00', (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000004'), (SELECT id_usuario FROM tbl_usuario WHERE username = 'admin'), 89.80, b'0'
WHERE NOT EXISTS (SELECT 1 FROM tbl_venta WHERE fecha_emision = '2026-10-17 17:00:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000004') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'admin'));

INSERT INTO tbl_detalle_venta (id_venta, id_manga, cantidad, precio_unitario, subtotal)
SELECT (SELECT id_venta FROM tbl_venta WHERE fecha_emision = '2026-10-17 17:00:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000004') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'admin') LIMIT 1), (SELECT id_manga FROM tbl_manga WHERE titulo = 'Naruto' AND num_tomo = 2), 1, 39.90, 39.90
WHERE NOT EXISTS (SELECT 1 FROM tbl_detalle_venta WHERE id_venta = (SELECT id_venta FROM tbl_venta WHERE fecha_emision = '2026-10-17 17:00:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000004') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'admin') LIMIT 1) AND id_manga = (SELECT id_manga FROM tbl_manga WHERE titulo = 'Naruto' AND num_tomo = 2));

INSERT INTO tbl_detalle_venta (id_venta, id_manga, cantidad, precio_unitario, subtotal)
SELECT (SELECT id_venta FROM tbl_venta WHERE fecha_emision = '2026-10-17 17:00:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000004') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'admin') LIMIT 1), (SELECT id_manga FROM tbl_manga WHERE titulo = 'Death Note' AND num_tomo = 1), 1, 49.90, 49.90
WHERE NOT EXISTS (SELECT 1 FROM tbl_detalle_venta WHERE id_venta = (SELECT id_venta FROM tbl_venta WHERE fecha_emision = '2026-10-17 17:00:00' AND id_cliente = (SELECT id_cliente FROM tbl_cliente WHERE dni = '70000004') AND id_usuario = (SELECT id_usuario FROM tbl_usuario WHERE username = 'admin') LIMIT 1) AND id_manga = (SELECT id_manga FROM tbl_manga WHERE titulo = 'Death Note' AND num_tomo = 1));

COMMIT;

-- Comprobación: en una base nueva se esperan 3, 8, 6, 4, 6, 15, 4 y 8 filas.

SELECT 'usuarios' AS tabla, COUNT(*) AS registros FROM tbl_usuario
UNION ALL SELECT 'clientes', COUNT(*) FROM tbl_cliente
UNION ALL SELECT 'proveedores', COUNT(*) FROM tbl_proveedor
UNION ALL SELECT 'editoriales', COUNT(*) FROM tbl_editorial
UNION ALL SELECT 'generos', COUNT(*) FROM tbl_genero
UNION ALL SELECT 'mangas', COUNT(*) FROM tbl_manga
UNION ALL SELECT 'ventas', COUNT(*) FROM tbl_venta
UNION ALL SELECT 'detalles_venta', COUNT(*) FROM tbl_detalle_venta;
