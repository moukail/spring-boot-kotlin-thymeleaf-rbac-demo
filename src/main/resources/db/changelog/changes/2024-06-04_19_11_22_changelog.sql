-- liquibase formatted sql

-- changeset ismail:1717519625203-2
CREATE TABLE public.role (id VARCHAR(255) NOT NULL, name VARCHAR(255) NOT NULL, CONSTRAINT "rolePK" PRIMARY KEY (id));

-- changeset ismail:1717521083790-2
ALTER TABLE public.role ADD CONSTRAINT UC_ROLENAME_COL UNIQUE (name);

-- changeset ismail:1718019795137-1
ALTER TABLE public.role ADD created_at TIMESTAMP(6) WITHOUT TIME ZONE;

-- changeset ismail:1718019795137-3
ALTER TABLE public.role ADD updated_at TIMESTAMP(6) WITHOUT TIME ZONE;

-- changeset ismail:1717519625203-3
CREATE TABLE public.role_permissions (role_id VARCHAR(255) NOT NULL, permissions_id VARCHAR(255) NOT NULL);

-- changeset ismail:1717519625203-5
ALTER TABLE public.role_permissions ADD CONSTRAINT "FKclluu29apreb6osx6ogt4qe16" FOREIGN KEY (permissions_id) REFERENCES public.permission (id);

-- changeset ismail:1717519625203-6
ALTER TABLE public.role_permissions ADD CONSTRAINT "FKib3eov0359c5o9s0a913mtyvv" FOREIGN KEY (roles_id) REFERENCES public.role (id);


