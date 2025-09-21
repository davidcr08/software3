-- Habilitar extensión para UUID si no existe
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Insertar usuario administrador inicial
INSERT INTO usuario (
    id,
    nombre,
    cedula,
    telefono,
    correoElectronico,
    contrasena,
    rol,
    estadoCuenta
) VALUES (
             gen_random_uuid(),
             'Admin Principal',
             '12345678',
             '3001234567',
             'admin@tienda.com',
             '$2a$10$t987iqKgWah83pnrrsBBuePK.eCDnaC8kzMchC4pqdxY0CEvRpBH6', -- contraseña: 1234567
             'ADMINISTRADOR',
             'ACTIVO'
         );
