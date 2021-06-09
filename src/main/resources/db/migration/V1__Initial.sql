CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create type STATE as enum('ADDED', 'IN_CHECK', 'APPROVED', 'ACTIVE');

CREATE TABLE employees (
                           id uuid DEFAULT uuid_generate_v4(),
                           name VARCHAR NOT NULL,
                           email VARCHAR NOT NULL,
                           state STATE,
                           CONSTRAINT pk_customer PRIMARY KEY (id)
);


INSERT INTO employees (name, email, state) VALUES ('ahata', 'ahata1231@gmail.com', 'ADDED');
INSERT INTO employees (name, email, state) VALUES ('ahata2', 'ahata1232@gmail.com', 'ADDED');
INSERT INTO employees (name, email, state) VALUES ('ahata3', 'ahata1233@gmail.com', 'ADDED');