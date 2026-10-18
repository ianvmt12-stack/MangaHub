# MangaHub

Sistema web de inventario y ventas para el proyecto de Lenguaje de Programación II.
Utiliza Java 17, Spring Boot, Spring MVC, Thymeleaf, Spring Data JPA y MySQL.

## Ejecutar el proyecto

1. Instalar un JDK 17 y abrir el proyecto como un proyecto Maven.
2. Ejecutar `database/bd_mangahub_demo.sql` completo en MySQL Workbench.
   Incluye las ocho tablas y datos ficticios. Ver la [guía de base de datos](database/README.md).
3. Configurar el usuario y la contraseña de MySQL en las variables `DB_USER` y
   `DB_PASSWORD`, o ajustar `src/main/resources/application.properties`.
4. Ejecutar `ProyectoMangahubApplication` desde el IDE o `./mvnw spring-boot:run`
   (`mvnw.cmd spring-boot:run` en Windows).
5. Abrir `http://localhost:8080/login` (o el puerto configurado).
   Usuario demo: **admin**. Contraseña demo: **MangaDemo123!**.

## Gestión de proveedores

El módulo reutiliza la entidad `Proveedor`, su tabla `tbl_proveedor` y su relación
existente con los mangas. Está disponible desde **Gestionar proveedores** en el
menú de inicio.

Permite listar por ID descendente, buscar por RUC o razón social, registrar, editar,
activar y desactivar proveedores. La búsqueda admite coincidencias parciales;
**Limpiar** vuelve al listado completo.

| Método | Ruta | Función |
| --- | --- | --- |
| GET | `/proveedores` | Listado; búsqueda opcional con `?q=texto` |
| GET | `/proveedores/nuevo` | Formulario de registro |
| POST | `/proveedores/guardar` | Registrar un proveedor activo |
| GET | `/proveedores/editar/{id}` | Formulario de edición |
| POST | `/proveedores/actualizar/{id}` | Actualizar los datos de contacto |
| POST | `/proveedores/desactivar/{id}` | Desactivar sin eliminar |
| POST | `/proveedores/activar/{id}` | Reactivar |

El servicio valida el formato del RUC (11 dígitos), la razón social obligatoria,
el formato del correo cuando se ingresa y la existencia de otro proveedor con el
mismo RUC. Esta validación de formato no consulta SUNAT. Teléfono y correo son
opcionales. Los campos se guardan sin espacios externos. La edición conserva el
estado actual y permite mantener el mismo RUC del proveedor editado.

La desactivación no borra registros ni altera los mangas asociados. Mantiene el
patrón de activación de los módulos existentes; no cambia los combos de mangas.
Un ID inexistente responde con HTTP 404. El formulario conserva los datos cuando
hay un error de validación.

## Comprobar proveedores

Pruebas unitarias sin conexión a MySQL:

```bash
./mvnw -Dtest=ProveedorServiceTest,ProveedorControllerTest test
```

Comprobación manual con la base del equipo:

1. Registrar un proveedor y comprobar su estado **Activo**.
2. Buscarlo por una parte de la razón social y por su RUC; probar **Limpiar**.
3. Intentar registrar el mismo RUC y comprobar el mensaje de validación.
4. Editar su contacto conservando el RUC y comprobar que se actualiza el mismo ID.
5. Desactivarlo, editarlo y comprobar que continúa **Inactivo**; luego reactivarlo.
6. Comprobar que sus mangas asociados siguen disponibles.
