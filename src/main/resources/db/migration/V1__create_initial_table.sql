CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE import_product
(
    id           UUID NOT NULL DEFAULT uuid_generate_v1mc(),
    status       TEXT  CHECK (char_length(status) <= 10)   NOT NULL DEFAULT 'PROCESSING',
    CONSTRAINT import_status_pkey PRIMARY KEY (id)
);



CREATE TABLE product
(
    id           UUID NOT NULL DEFAULT uuid_generate_v1mc(),
    import_product_id UUID,
    code         TEXT  CHECK (char_length(code) <= 100)   NOT NULL,
    product_name TEXT  CHECK (char_length(product_name) <= 100) NOT NULL,
    status       TEXT  CHECK (char_length(code) <= 10)   NOT NULL DEFAULT 'PROCESSING',
    description text CHECK (char_length(description) <= 250) NULL,
    CONSTRAINT product_pkey PRIMARY KEY (id),
    CONSTRAINT import_product_fkey FOREIGN KEY (import_product_id) REFERENCES  public.import_product(id)
);

CREATE TABLE product_inventory
(
    id           UUID NOT NULL DEFAULT uuid_generate_v1mc(),
    code         TEXT  CHECK (char_length(code) <= 100)   NOT NULL,
    product_name TEXT  CHECK (char_length(product_name) <= 100) NOT NULL,
    description text CHECK (char_length(description) <= 250) NULL,
    CONSTRAINT product_inventory_pkey PRIMARY KEY (id)
);

CREATE INDEX product_id_idx
    ON public.product
    USING btree
    (import_product_id);

CREATE TABLE IF NOT EXISTS charge
(
    id                 UUID                     NOT NULL,
    client_id           BIGINT                   NOT NULL,
    due_date           DATE                     NOT NULL,
    negotiation_number BIGINT                   NOT NULL,
    fee                NUMERIC(10, 2)           NOT NULL,
    total_amount       NUMERIC(10, 2)           NOT NULL,
    active             BOOL                     NOT NULL DEFAULT true,
    created_at         TIMESTAMP with time zone NOT NULL DEFAULT now(),
    updated_at         TIMESTAMP with time zone NOT NULL DEFAULT now(),
    deleted_at         TIMESTAMP with time zone NULL,
    CONSTRAINT charge_pkey PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS analysis_result
(
    id                 UUID                     NOT NULL,
    client_id          BIGINT                   NOT NULL,
    status             TEXT                     NOT NULL CHECK (char_length(status) <= 11),
    created_at         TIMESTAMP with time zone NOT NULL DEFAULT now(),
    updated_at         TIMESTAMP with time zone NOT NULL DEFAULT now(),
    deleted_at         TIMESTAMP with time zone NULL,
    CONSTRAINT analysis_result_pkey PRIMARY KEY (id)
);

