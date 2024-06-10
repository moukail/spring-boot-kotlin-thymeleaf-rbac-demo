-- liquibase formatted sql

-- changeset ismail:1717519625203-1
CREATE TABLE public.permission (id VARCHAR(255) NOT NULL, name VARCHAR(255) NOT NULL, CONSTRAINT "permissionPK" PRIMARY KEY (id));

-- changeset ismail:1717521083790-1
ALTER TABLE public.permission ADD CONSTRAINT UC_PERMISSIONNAME_COL UNIQUE (name);
