-- liquibase formatted sql

-- changeset ismail:1717261864253-1
CREATE TABLE public."user" (id VARCHAR(255) NOT NULL, created_at TIMESTAMP(6) WITHOUT TIME ZONE, email VARCHAR(255) NOT NULL, firstname VARCHAR(255) NOT NULL, lastname VARCHAR(255), role VARCHAR(255) NOT NULL, updated_at TIMESTAMP(6) WITHOUT TIME ZONE, CONSTRAINT "userPK" PRIMARY KEY (id));

-- changeset ismail:1717261864253-2
ALTER TABLE public."user" ADD CONSTRAINT UC_USEREMAIL_COL UNIQUE (email);

