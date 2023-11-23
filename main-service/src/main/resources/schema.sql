DROP TABLE IF EXISTS categories CASCADE;


CREATE TABLE IF NOT EXISTS categories
(
    category_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    name        VARCHAR(50) NOT NULL UNIQUE
);