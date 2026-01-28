-- 1. Criar o Banco de Dados

SELECT 'CREATE DATABASE "cad-livros"'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'cad-livros')\gexec

    \c "cad-livros"

-- 2. Criar as tabelas
CREATE TABLE IF NOT EXISTS tb_role (
                                       uuid UUID PRIMARY KEY,
                                       authority VARCHAR(50) UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS tb_user (
                                       uuid UUID PRIMARY KEY,
                                       first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS tb_user_role (
                                            user_id UUID REFERENCES tb_user(uuid) ON DELETE CASCADE,
    role_id UUID REFERENCES tb_role(uuid) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
    );

-- 3. Inserção de Dados
INSERT INTO tb_role (uuid, authority) VALUES
('05b8548e-123a-4a96-a441-818dd502e1b5', 'ADMIN'),
('9bb973fd-818a-4319-b5d1-47af6573e080', 'USER');

INSERT INTO tb_user (uuid, first_name, last_name, email, password) VALUES
('f76a43de-8c95-4efe-9a17-ea62a7388c27', 'Admin', 'User', 'admin@email.com', '$2a$12$XqyEKMWsRPuTAmY/qJ97ke9tbXhIy9kFUt.xD0CVFnJACaNFyt.z2'),
('a11b22c3-d44e-55f6-a77b-88c99d00e1b5', 'Comum', 'User', 'user@email.com', '$2a$12$XqyEKMWsRPuTAmY/qJ97ke9tbXhIy9kFUt.xD0CVFnJACaNFyt.z2');

-- 4. Vincular Usuário às Roles
INSERT INTO tb_user_role (user_id, role_id) VALUES
    ('f76a43de-8c95-4efe-9a17-ea62a7388c27', '05b8548e-123a-4a96-a441-818dd502e1b5');

DO $$
BEGIN
    RAISE NOTICE 'Banco de dados e tabelas criados com sucesso.';
END $$;