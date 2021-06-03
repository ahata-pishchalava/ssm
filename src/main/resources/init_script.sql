CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create type STATE as enum('ADDED', 'IN_CHECK', 'APPROVED', 'ACTIVE');


CREATE TABLE employees (
                           id uuid DEFAULT uuid_generate_v4(),
                           name VARCHAR NOT NULL,
                           email VARCHAR NOT NULL,
                           state STATE DEFAULT 'ADDED',
                           CONSTRAINT pk_customer PRIMARY KEY (id)
);
