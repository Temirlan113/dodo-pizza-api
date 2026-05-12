-- changeset your_name:1
CREATE TABLE categories (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            description TEXT
);


INSERT INTO categories (name, description) VALUES ('Пицца', 'Самая вкусная пицца в Алматы');
INSERT INTO categories (name, description) VALUES ('Напитки', 'Освежающие напитки');