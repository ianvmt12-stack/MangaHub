USE bd_mangahub;

SHOW CREATE TABLE tbl_manga;
SHOW CREATE TABLE tbl_editorial;
SHOW CREATE TABLE tbl_proveedor;
SHOW CREATE TABLE tbl_genero;

SELECT * FROM tbl_editorial;
SELECT * FROM tbl_manga;
SELECT * FROM tbl_proveedor;
SELECT * FROM tbl_genero;
SELECT id_manga, titulo, num_tomo, autor, precio, stock, activo,
       id_editorial, id_genero, id_proveedor
FROM tbl_manga;


USE bd_mangahub;

INSERT INTO tbl_editorial (nombre, pais)
VALUES
('Shueisha', 'Japón'),
('Kodansha', 'Japón'),
('Shogakukan', 'Japón'),
('Kadokawa', 'Japón'),
('Square Enix', 'Japón'),
('Editorial Kamite', 'México');

USE bd_mangahub;

INSERT INTO tbl_proveedor
(ruc, razon_social, telefono, correo, activo)
VALUES
('20611111111', 'Manga World Distribuciones SAC', '987654101', 'ventas@mangaworld.pe', 1),
('20622222222', 'Anime Perú Importaciones SAC', '987654102', 'contacto@animeperu.pe', 1),
('20633333333', 'Sakura Manga Distribuidora EIRL', '987654103', 'ventas@sakuramanga.pe', 1),
('20644444444', 'Nippon Books Perú SAC', '987654104', 'contacto@nipponbooks.pe', 1),
('20655555555', 'Tokyo Manga Distribuciones SAC', '987654105', 'ventas@tokyomanga.pe', 1),
('20666666666', 'Shonen Store Importaciones EIRL', '987654106', 'contacto@shonenstore.pe', 1);

USE bd_mangahub;

INSERT INTO tbl_genero (descripcion)
VALUES
('Acción'),
('Escolar'),
('Romance'),
('Terror'),
('Ciencia Ficción'),
('Deportes'),
('Misterio'),
('Slice of Life'),
('Isekai'),
('Psicológico');



USE bd_mangahub;

INSERT INTO tbl_manga
(titulo, num_tomo, autor, precio, stock, id_editorial, id_genero, id_proveedor, activo)
VALUES
('Naruto', 1, 'Masashi Kishimoto', 39.90, 25, 1, 1, 1, 1),
('One Piece', 1, 'Eiichiro Oda', 45.00, 30, 2, 7, 2, 1),
('Dragon Ball', 1, 'Akira Toriyama', 39.90, 20, 4, 1, 3, 1),
('Bleach', 1, 'Tite Kubo', 42.00, 18, 1, 1, 4, 1),
('Death Note', 1, 'Tsugumi Ohba', 49.90, 15, 3, 12, 5, 1),
('Demon Slayer', 1, 'Koyoharu Gotouge', 45.00, 22, 2, 1, 2, 1),
('Attack on Titan', 1, 'Hajime Isayama', 52.00, 16, 6, 2, 6, 1),
('Tokyo Ghoul', 1, 'Sui Ishida', 55.00, 14, 1, 4, 7, 1),
('Fullmetal Alchemist', 1, 'Hiromu Arakawa', 48.00, 20, 3, 7, 1, 1),
('Hunter x Hunter', 1, 'Yoshihiro Togashi', 45.00, 19, 5, 1, 8, 1),
('Black Clover', 1, 'Yuki Tabata', 40.00, 24, 2, 1, 3, 1),
('My Hero Academia', 1, 'Kohei Horikoshi', 42.00, 21, 2, 6, 4, 1),
('Haikyuu!!', 1, 'Haruichi Furudate', 44.90, 18, 1, 11, 5, 1),
('Slam Dunk', 1, 'Takehiko Inoue', 49.90, 12, 4, 11, 6, 1),
('Sailor Moon', 1, 'Naoko Takeuchi', 46.00, 14, 6, 3, 7, 1),
('Fruits Basket', 1, 'Natsuki Takaya', 42.00, 16, 3, 8, 8, 1),
('Horimiya', 1, 'HERO y Daisuke Hagiwara', 43.00, 17, 9, 8, 1, 1),
('Kaguya-sama Love is War', 1, 'Aka Akasaka', 45.00, 19, 5, 5, 2, 1),
('Oshi no Ko', 1, 'Aka Akasaka y Mengo Yokoyari', 49.90, 20, 5, 12, 3, 1),
('Vinland Saga', 1, 'Makoto Yukimura', 59.90, 13, 6, 2, 4, 1),
('Vagabond', 1, 'Takehiko Inoue', 65.00, 10, 7, 2, 5, 1),
('Monster', 1, 'Naoki Urasawa', 62.00, 12, 7, 15, 6, 1),
('20th Century Boys', 1, 'Naoki Urasawa', 58.00, 11, 7, 12, 7, 1),
('Solo Leveling', 1, 'Chugong y DUBU', 69.90, 25, 8, 6, 8, 1),
('The Promised Neverland', 1, 'Kaiu Shirai y Posuka Demizu', 45.00, 18, 5, 12, 1, 1);
