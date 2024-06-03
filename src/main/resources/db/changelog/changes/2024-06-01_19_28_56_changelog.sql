-- liquibase formatted sql

-- changeset ismail:1717262937570-1
ALTER TABLE public."user" ADD password VARCHAR(255) NOT NULL;

