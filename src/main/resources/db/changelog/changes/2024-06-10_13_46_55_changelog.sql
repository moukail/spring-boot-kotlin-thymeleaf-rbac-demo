-- liquibase formatted sql

-- changeset ismail:1718020016483-1
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
create EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO public.permission (id, name) VALUES (gen_random_uuid(), 'dashboard');

INSERT INTO public.permission (id, name) VALUES (gen_random_uuid(), 'user_list');
INSERT INTO public.permission (id, name) VALUES (gen_random_uuid(), 'user_details');
INSERT INTO public.permission (id, name) VALUES (gen_random_uuid(), 'user_add');
INSERT INTO public.permission (id, name) VALUES (gen_random_uuid(), 'user_edit');
INSERT INTO public.permission (id, name) VALUES (gen_random_uuid(), 'user_delete');

INSERT INTO public.permission (id, name) VALUES (gen_random_uuid(), 'role_list');
INSERT INTO public.permission (id, name) VALUES (gen_random_uuid(), 'role_add');
INSERT INTO public.permission (id, name) VALUES (gen_random_uuid(), 'role_edit');
INSERT INTO public.permission (id, name) VALUES (gen_random_uuid(), 'role_delete');

INSERT INTO public.role (id, name) VALUES (gen_random_uuid(), 'ROLE_USER');
INSERT INTO public.role (id, name) VALUES (gen_random_uuid(), 'ROLE_MANAGER');
INSERT INTO public.role (id, name) VALUES (gen_random_uuid(), 'ROLE_ADMIN');

INSERT INTO public.role_permissions (role_id, permissions_id) VALUES ((SELECT id FROM role WHERE name = 'ROLE_ADMIN'), (SELECT id FROM permission WHERE name = 'user_list'))
INSERT INTO public.role_permissions (role_id, permissions_id) VALUES ((SELECT id FROM role WHERE name = 'ROLE_ADMIN'), (SELECT id FROM permission WHERE name = 'user_details'))
INSERT INTO public.role_permissions (role_id, permissions_id) VALUES ((SELECT id FROM role WHERE name = 'ROLE_ADMIN'), (SELECT id FROM permission WHERE name = 'user_add'))
INSERT INTO public.role_permissions (role_id, permissions_id) VALUES ((SELECT id FROM role WHERE name = 'ROLE_ADMIN'), (SELECT id FROM permission WHERE name = 'user_edit'))
INSERT INTO public.role_permissions (role_id, permissions_id) VALUES ((SELECT id FROM role WHERE name = 'ROLE_ADMIN'), (SELECT id FROM permission WHERE name = 'user_delete'))

INSERT INTO public.role_permissions (role_id, permissions_id) VALUES ((SELECT id FROM role WHERE name = 'ROLE_ADMIN'), (SELECT id FROM permission WHERE name = 'role_list'))
INSERT INTO public.role_permissions (role_id, permissions_id) VALUES ((SELECT id FROM role WHERE name = 'ROLE_ADMIN'), (SELECT id FROM permission WHERE name = 'role_add'))
INSERT INTO public.role_permissions (role_id, permissions_id) VALUES ((SELECT id FROM role WHERE name = 'ROLE_ADMIN'), (SELECT id FROM permission WHERE name = 'role_edit'))
INSERT INTO public.role_permissions (role_id, permissions_id) VALUES ((SELECT id FROM role WHERE name = 'ROLE_ADMIN'), (SELECT id FROM permission WHERE name = 'role_delete'))

insert into public."user" (id, email, password, firstname, lastname, role_id, created_at)
VALUES (gen_random_uuid(), 'admin1@test.com', crypt('pass_1234', gen_salt('bf')), 'Admin', 'Admin', (SELECT id FROM role WHERE name = 'ROLE_ADMIN'), now())
