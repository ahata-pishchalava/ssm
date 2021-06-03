CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create type STATE as enum('ADDED', 'IN_CHECK', 'APPROVED', 'ACTIVE');


CREATE TABLE employees (
                           id uuid DEFAULT uuid_generate_v4(),
                           name VARCHAR NOT NULL,
                           email VARCHAR NOT NULL,
                           state STATE DEFAULT 'ADDED',
                           CONSTRAINT pk_customer PRIMARY KEY (id)
);


INSERT INTO employees (name, email, state) VALUES ('employee1', 'ahata1231@gmail.com', 'ADDED');
INSERT INTO employees (name, email, state) VALUES ('employee2', 'ahata1232@gmail.com', 'IN_CHECK');
INSERT INTO employees (name, email, state) VALUES ('employee3', 'ahata1233@gmail.com', 'APPROVED');
INSERT INTO employees (name, email, state) VALUES ('employee4', 'ahata1234@gmail.com', 'ACTIVE');