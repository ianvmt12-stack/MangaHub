# Base de datos de demostración de MangaHub

El script `bd_mangahub_demo.sql` crea una base compatible con las ocho entidades
JPA del proyecto y agrega datos ficticios para probar el sistema. Se construyó
a partir del código del equipo. Los precios, saldos y relaciones comerciales
del catálogo son ejemplos para desarrollo.

## Importar en MySQL Workbench

1. Conectarse al servidor local de MySQL 8.0.16 o posterior.
2. Abrir **File > Open SQL Script** y seleccionar `database/bd_mangahub_demo.sql`.
3. Ejecutar el archivo completo con **Execute All**, sin seleccionar solo una parte.
4. Actualizar **Schemas**. Debe aparecer `bd_mangahub_demo` con ocho tablas.
5. Comprobar los conteos que muestra el último SELECT del script.

El script no contiene DROP ni TRUNCATE. Crea la base y las tablas si no existen;
al repetirlo omite los ejemplos que ya están registrados, sin sobrescribir sus
datos. Es una inicialización de la base demo; no realiza migraciones de otra base
con una estructura diferente. Los identificadores se resuelven mediante RUC,
DNI, usuario y otros datos de referencia, sin asumir IDs fijos.

## Conectar con Spring Tools Suite

El archivo `src/main/resources/application.properties` apunta por defecto a:

```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/bd_mangahub_demo?useSSL=false&serverTimezone=UTC}
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD:}
spring.jpa.hibernate.ddl-auto=validate
```

La configuración principal utiliza `root` y una contraseña vacía si no defines
`DB_PASSWORD`. Si tu instalación tiene contraseña, configura esa variable con tu clave
en **Run > Run Configurations > Spring Boot App > Environment > New**. También
puedes configurar `DB_USER` y `DB_URL` para tu conexión. Esas credenciales son de
MySQL; los usuarios del formulario web se muestran en la sección siguiente.

Importar en STS como **Maven > Existing Maven Projects**, marcando únicamente el
`pom.xml` principal (no el de `bin`). Ejecutar `ProyectoMangahubApplication` con
**Run As > Spring Boot App** y abrir `http://localhost:8080/login`, salvo que se
haya cambiado el puerto.

`ddl-auto=validate` comprueba las tablas existentes: el SQL se importa antes de
iniciar la aplicación. Spring no crea la base ni carga estos datos automáticamente.

## Usuarios de prueba

| Usuario | Contraseña de prueba | Rol | Estado |
| --- | --- | --- | --- |
| admin | MangaDemo123! | ADMIN | Activo |
| jose | VentasDemo123! | VENDEDOR | Activo |
| inactivo | Desactivado123! | VENDEDOR | Inactivo; el login lo rechaza |

El login actual compara directamente usuario y contraseña. Por esa razón, los
ejemplos usan texto simple; son credenciales públicas destinadas a la demo local.
Las etiquetas de rol corresponden a los datos de ejemplo y se muestran en la
sesión; este script no agrega controles de permisos a las pantallas.

La configuración utiliza cookies para la sesión, de modo que la redirección
del login llega al menú sin añadir identificadores de sesión a la URL.

## Tablas y relaciones

| Tabla | Clave primaria | Relaciones | Filas iniciales |
| --- | --- | --- | ---: |
| tbl_usuario | id_usuario | Referenciada por ventas | 3 |
| tbl_cliente | id_cliente | Referenciada por ventas | 8 |
| tbl_proveedor | id_proveedor | Referenciada por mangas | 6 |
| tbl_editorial | id_editorial | Referenciada por mangas | 4 |
| tbl_genero | id_genero | Referenciada por mangas | 6 |
| tbl_manga | id_manga | Editorial, género y proveedor | 15 |
| tbl_venta | id_venta | Cliente y usuario | 4 |
| tbl_detalle_venta | id_detalle | Venta y manga | 8 |

Se usan claves foráneas, valores monetarios DECIMAL y estados BIT compatibles con
las entidades. El RUC, el DNI y el nombre de usuario tienen restricciones de
unicidad. El RUC se valida por formato de 11 dígitos, sin consulta a SUNAT.
También se impiden stocks negativos, cantidades de venta no positivas y
subtotales distintos de cantidad por precio unitario.

Las ventas y sus detalles permiten verificar las relaciones ya modeladas. Las
pantallas existentes son login, inicio, clientes, mangas y proveedores. Esta
entrega no incorpora un formulario de ventas. El stock cargado es un saldo de
ejemplo; el SQL no instala disparadores que descuenten existencias.

## Consultas de apoyo

`consultas.sql` contiene siete consultas: proveedores, catálogo relacionado,
ventas por cliente y vendedor, detalles, suma de ventas activas, comprobación de
totales y listado de usuarios sin contraseñas. En la carga inicial, las tres
ventas activas suman **368.20** y la cuarta venta está inactiva.
