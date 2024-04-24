CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE product
(
    id           UUID NOT NULL DEFAULT uuid_generate_v1mc(),
    code         TEXT  CHECK (char_length(code) <= 100)   NOT NULL,
    product_name TEXT  CHECK (char_length(product_name) <= 100) NOT NULL,
    description text CHECK (char_length(description) <= 250) NULL,
    CONSTRAINT product_pkey PRIMARY KEY (id)
);

