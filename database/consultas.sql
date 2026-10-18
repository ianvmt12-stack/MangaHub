USE bd_mangahub_demo;

-- 1. Proveedores y estado de activación.
SELECT id_proveedor, ruc, razon_social, telefono, correo,
       IF(activo = b'1', 'Activo', 'Inactivo') AS estado
FROM tbl_proveedor
ORDER BY id_proveedor DESC;

-- 2. Catálogo con los datos usados por los combos de Spring.
SELECT m.id_manga, m.titulo, m.num_tomo, m.autor, m.precio, m.stock,
       e.nombre AS editorial, g.descripcion AS genero,
       p.razon_social AS proveedor
FROM tbl_manga m
JOIN tbl_editorial e ON e.id_editorial = m.id_editorial
JOIN tbl_genero g ON g.id_genero = m.id_genero
JOIN tbl_proveedor p ON p.id_proveedor = m.id_proveedor
ORDER BY m.titulo, m.num_tomo;

-- 3. Ventas relacionadas con cliente y vendedor.
SELECT v.id_venta, v.fecha_emision,
       CONCAT(c.nombres, ' ', c.apellidos) AS cliente,
       u.nombres AS vendedor, v.monto_total,
       IF(v.activo = b'1', 'Activa', 'Inactiva') AS estado
FROM tbl_venta v
JOIN tbl_cliente c ON c.id_cliente = v.id_cliente
JOIN tbl_usuario u ON u.id_usuario = v.id_usuario
ORDER BY v.fecha_emision DESC;

-- 4. Detalle de los productos vendidos y subtotales.
SELECT d.id_venta, m.titulo, m.num_tomo, d.cantidad,
       d.precio_unitario, d.subtotal
FROM tbl_detalle_venta d
JOIN tbl_manga m ON m.id_manga = d.id_manga
ORDER BY d.id_venta, d.id_detalle;

-- 5. Total de ventas activas: 368.20 en la carga inicial.
SELECT COALESCE(SUM(monto_total), 0) AS total_ventas_activas
FROM tbl_venta
WHERE activo = b'1';

-- 6. Control de integridad: debe devolver CERO filas.
SELECT v.id_venta, v.monto_total,
       COALESCE(SUM(d.subtotal), 0) AS suma_detalles
FROM tbl_venta v
LEFT JOIN tbl_detalle_venta d ON d.id_venta = v.id_venta
GROUP BY v.id_venta, v.monto_total
HAVING v.monto_total <> COALESCE(SUM(d.subtotal), 0);

-- 7. Consultar usuarios sin mostrar sus contraseñas.
SELECT id_usuario, nombres, username, rol,
       IF(activo = b'1', 'Activo', 'Inactivo') AS estado
FROM tbl_usuario
ORDER BY id_usuario;
