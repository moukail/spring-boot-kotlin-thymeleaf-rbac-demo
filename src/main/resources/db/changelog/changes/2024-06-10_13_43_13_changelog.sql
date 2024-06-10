-- liquibase formatted sql

-- changeset ismail:1717519625203-8
ALTER TABLE public."user" DROP COLUMN role;

-- changeset ismail:1717519625203-4
ALTER TABLE public."user" ADD role_id VARCHAR(255);

-- changeset ismail:1717519625203-7
ALTER TABLE public."user" ADD CONSTRAINT "FKn82ha3ccdebhokx3a8fgdqeyy" FOREIGN KEY (role_id) REFERENCES public.role (id);