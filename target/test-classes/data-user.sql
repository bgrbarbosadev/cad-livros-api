DELETE FROM tb_user;
DELETE FROM tb_role;
DELETE FROM tb_user_role;

-- 3. Insere os dados nas tabelas independentes
INSERT INTO tb_role (uuid, authority) VALUES ('d1103e05-b12d-4022-9639-311e670962fd','ADMIN');
INSERT INTO tb_role (uuid, authority) VALUES ('cfcee1fc-51e6-43e4-8ffc-5c5f54e2f6e1','USER');

INSERT INTO tb_user (uuid, email, first_name, last_name, password)
VALUES ('31b87395-4cd2-44b4-b34c-23c025d87397', 'admin@mail.com', 'Admin', 'Admin', '$2a$12$8K/5PsoF0Xet1GkAShqBLeMdJ6.EtXTU6Tn.SZpridhx0H3w8H.2K');

INSERT INTO tb_user (uuid, email, first_name, last_name, password)
VALUES ('e774f0fa-ea64-45f0-80ff-77dd24258660', 'user@mail.com', 'User', 'User', '$2a$12$8K/5PsoF0Xet1GkAShqBLeMdJ6.EtXTU6Tn.SZpridhx0H3w8H.2K');

-- 4. Por fim, insere as relações
INSERT INTO tb_user_role (user_id, role_id) VALUES ('31b87395-4cd2-44b4-b34c-23c025d87397', 'd1103e05-b12d-4022-9639-311e670962fd');
INSERT INTO tb_user_role (user_id, role_id) VALUES ('e774f0fa-ea64-45f0-80ff-77dd24258660', 'cfcee1fc-51e6-43e4-8ffc-5c5f54e2f6e1');