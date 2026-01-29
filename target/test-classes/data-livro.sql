DELETE FROM tb_genero;

INSERT INTO tb_genero (id_genero, desc_genero)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'Ficção Científica');

INSERT INTO tb_genero (id_genero, desc_genero)
VALUES ('6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'Fantasia');
--------------------------------------------------------------------------------------------------------------

DELETE FROM tb_editora;

INSERT INTO tb_editora (id_editora, cnpj_editora, razao_social_editora, tel_editora)
VALUES (
           'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
           '12.345.678/0001-01',
           'Editora Horizonte Digital Ltda',
           '(11) 4002-8922'
       );

INSERT INTO tb_editora (id_editora, cnpj_editora, razao_social_editora, tel_editora)
VALUES (
           'b2d5f3a1-7c4e-4b92-bd1e-5f8e2c1a3d44',
           '98.765.432/0001-10',
           'Saber e Cultura Editores',
           '(21) 3344-5566'
       );
---------------------------------------------------------------------------------------------------------------
DELETE FROM tb_autor;

INSERT INTO tb_autor (id_autor, name_autor, biografia_autor, dt_nasc_autor, nacionalidade_autor, foto_autor)
VALUES ('d45f77d8-7254-4bfe-b2fb-206a7c3c27c9', 'José Silva', 'Autor contemporâneo.', '1980-05-12', 'Brasil', 'jose_silva.jpg');

INSERT INTO tb_autor (id_autor, name_autor, biografia_autor, dt_nasc_autor, nacionalidade_autor, foto_autor)
VALUES ('680cb3a4-8d79-430b-a839-e57afc746aba', 'Maria Oliveira', 'Romancista.', '1975-08-23', 'Portugal', 'maria_oliveira.jpg');

---------------------------------------------------------------------------------------------------------------
DELETE FROM tb_livro;

INSERT INTO tb_livro (id_livro, titulo_livro, isbn, ano_publicacao, edicao_livro, preco_livro, id_autor, id_genero, id_editora)
VALUES
('d69c2654-c170-4975-9379-e177e9faec25', 'A Arte da Programação', '978-0201896831', '1997-07-01', '1ª', 149.90, 'd45f77d8-7254-4bfe-b2fb-206a7c3c27c9', '6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
('8ece4648-59eb-4a64-b5ea-bc4d2a44fc62', 'Design de Software', '978-0132350884', '2008-08-01', '2ª', 129.50, 'd45f77d8-7254-4bfe-b2fb-206a7c3c27c9', '6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),
('f60e992f-2901-4125-80b4-5e9de462f2c9', 'Padrões de Arquitetura', '978-0321127426', '2002-10-15', '1ª', 179.00, '680cb3a4-8d79-430b-a839-e57afc746aba', '6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11');