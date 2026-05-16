-- changeset your_name:2
CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          price DECIMAL(10, 2) NOT NULL,
                          active BOOLEAN DEFAULT TRUE,
                          category_id BIGINT,
                          CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories (id)
);

INSERT INTO products (name, price, category_id) VALUES ('Маргарита', 2500.00, 1);