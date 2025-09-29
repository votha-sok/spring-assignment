-- Banking System Database Creation Script
-- PostgreSQL

-- Drop existing tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS public.accounts CASCADE;
DROP TABLE IF EXISTS public.user_roles CASCADE;
DROP TABLE IF EXISTS public.role_feature CASCADE;
DROP TABLE IF EXISTS public.feature_permissions CASCADE;
DROP TABLE IF EXISTS public.users CASCADE;
DROP TABLE IF EXISTS public.roles CASCADE;
DROP TABLE IF EXISTS public.features CASCADE;
DROP TABLE IF EXISTS public.permissions CASCADE;
DROP TABLE IF EXISTS public.transactions CASCADE;

-- Drop sequences if they exist
DROP SEQUENCE IF EXISTS public.accounts_id_seq;
DROP SEQUENCE IF EXISTS public.feature_permissions_id_seq;
DROP SEQUENCE IF EXISTS public.features_id_seq;
DROP SEQUENCE IF EXISTS public.permissions_id_seq;
DROP SEQUENCE IF EXISTS public.role_feature_id_seq;
DROP SEQUENCE IF EXISTS public.roles_seq;
DROP SEQUENCE IF EXISTS public.user_roles_id_seq;
DROP SEQUENCE IF EXISTS public.users_id_seq;

-- Create sequences
CREATE SEQUENCE public.accounts_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE public.feature_permissions_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE public.features_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE public.permissions_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE public.role_feature_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE public.roles_seq INCREMENT 50 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE public.user_roles_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE public.users_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;

-- Create tables
CREATE TABLE public.users (
                              id int8 NOT NULL DEFAULT nextval('users_id_seq'::regclass),
                              admin bool NULL,
                              created_date timestamp(6) NOT NULL,
                              email varchar(255) NOT NULL,
                              last_name varchar(255) NULL,
                              first_name varchar(255) NULL,
                              password varchar(255) NOT NULL,
                              phone varchar(255) NULL,
                              status bool NULL,
                              updated_date timestamp(6) NULL,
                              user_name varchar(255) NOT NULL,
                              CONSTRAINT users_pkey PRIMARY KEY (id),
                              CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
                              CONSTRAINT ukdu5v5sr43g5bfnji4vb8hg5s3 UNIQUE (phone)
);

CREATE TABLE public.roles (
                              id int8 NOT NULL DEFAULT nextval('roles_seq'::regclass),
                              created_date timestamp(6) NOT NULL,
                              description varchar(255) NULL,
                              name varchar(255) NULL,
                              status bool NULL,
                              updated_date timestamp(6) NULL,
                              CONSTRAINT roles_pkey PRIMARY KEY (id)
);

CREATE TABLE public.user_roles (
                                   id int8 NOT NULL DEFAULT nextval('user_roles_id_seq'::regclass),
                                   status bool NULL,
                                   role_id int8 NULL,
                                   user_id int8 NULL,
                                   CONSTRAINT user_roles_pkey PRIMARY KEY (id)
);

CREATE TABLE public.features (
                                 id int8 NOT NULL DEFAULT nextval('features_id_seq'::regclass),
                                 created_date timestamp(6) NOT NULL,
                                 icon varchar(255) NULL,
                                 menu_order int4 NULL,
                                 parent_id int8 NULL,
                                 router_link varchar(255)[] NULL,
                                 status bool NULL,
                                 title varchar(255) NULL,
                                 updated_date timestamp(6) NULL,
                                 CONSTRAINT features_pkey PRIMARY KEY (id)
);

CREATE TABLE public.permissions (
                                    id int8 NOT NULL DEFAULT nextval('permissions_id_seq'::regclass),
                                    created_date timestamp(6) NOT NULL,
                                    function_name varchar(255) NULL,
                                    function_order int4 NULL,
                                    status bool NULL,
                                    updated_date timestamp(6) NULL,
                                    CONSTRAINT permissions_pkey PRIMARY KEY (id)
);

CREATE TABLE public.feature_permissions (
                                            id int8 NOT NULL DEFAULT nextval('feature_permissions_id_seq'::regclass),
                                            status bool NULL,
                                            feature_id int8 NULL,
                                            permission_id int8 NULL,
                                            CONSTRAINT feature_permissions_pkey PRIMARY KEY (id)
);

CREATE TABLE public.role_feature (
                                     id int8 NOT NULL DEFAULT nextval('role_feature_id_seq'::regclass),
                                     status bool NULL,
                                     feature_id int8 NULL,
                                     role_id int8 NULL,
                                     CONSTRAINT role_feature_pkey PRIMARY KEY (id)
);

CREATE TABLE public.accounts (
                                 id int8 NOT NULL DEFAULT nextval('accounts_id_seq'::regclass),
                                 account_holder_email varchar(255) NULL,
                                 account_holder_name varchar(255) NULL,
                                 account_holder_phone varchar(255) NULL,
                                 account_number varchar(255) NOT NULL,
                                 account_type varchar(255) NOT NULL,
                                 balance numeric(38,2) NOT NULL,
                                 created_date timestamp(6) NOT NULL,
                                 national_id varchar(255) NULL,
                                 status bool NOT NULL,
                                 updated_date timestamp(6) NULL,
                                 created_by int8 NOT NULL,
                                 CONSTRAINT accounts_pkey PRIMARY KEY (id),
                                 CONSTRAINT uk6kplolsdtr3slnvx97xsy2kc8 UNIQUE (account_number),
                                 CONSTRAINT accounts_account_type_check CHECK (account_type::text = ANY (ARRAY['SAVINGS'::character varying, 'CHECKING'::character varying, 'BUSINESS'::character varying]::text[]))
    );

-- Add foreign key constraints
ALTER TABLE public.user_roles ADD CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id) REFERENCES public.roles(id);
ALTER TABLE public.user_roles ADD CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id) REFERENCES public.users(id);

ALTER TABLE public.feature_permissions ADD CONSTRAINT fkmjpyi3ke3m877d3punkc0d93y FOREIGN KEY (feature_id) REFERENCES public.features(id);
ALTER TABLE public.feature_permissions ADD CONSTRAINT fkpoaamrboluafhf5cqcg8bocmv FOREIGN KEY (permission_id) REFERENCES public.permissions(id);

ALTER TABLE public.role_feature ADD CONSTRAINT fk8njwcqj06muv415hamcupl4p9 FOREIGN KEY (feature_id) REFERENCES public.features(id);
ALTER TABLE public.role_feature ADD CONSTRAINT fksbu1gppsx1cuy7ip0yb2b92id FOREIGN KEY (role_id) REFERENCES public.roles(id);

ALTER TABLE public.accounts ADD CONSTRAINT fk5sgyirkr3gauvf052hm7x5jix FOREIGN KEY (created_by) REFERENCES public.users(id);

-- Insert sample data

-- Insert users
INSERT INTO public.users (id, admin, created_date, email, last_name, first_name, password, phone, status, updated_date, user_name)
VALUES (1, true, '2025-09-12 10:43:42.61304', 'vothasok@example.com', NULL, NULL, '{bcrypt}$2a$10$QKGovw3qs3Nj2lWYC26hneT1r/Qcin2h.6I5w.uN0L/5/kKvHd8GC', '015600022', true, '2025-09-12 10:43:42.61304', 'votha');

-- Insert roles
INSERT INTO public.roles (id, created_date, description, name, status, updated_date)
VALUES (1, '2025-09-12 17:13:28.167379', 'Admin role use for admin', 'Admin', true, '2025-09-12 17:13:28.167379');

INSERT INTO public.roles (id, created_date, description, name, status, updated_date)
VALUES (2, '2025-09-12 17:13:36.698434', 'User role use for admin', 'User', true, '2025-09-12 17:13:36.698434');

-- Insert user_roles
INSERT INTO public.user_roles (id, status, role_id, user_id)
VALUES (1, true, 1, 1);

-- Insert features
INSERT INTO public.features (id, created_date, icon, menu_order, parent_id, router_link, status, title, updated_date)
VALUES (1, '2025-09-12 17:05:11.518738', NULL, 10, NULL, NULL, true, 'Setting', '2025-09-12 17:05:11.518738');

INSERT INTO public.features (id, created_date, icon, menu_order, parent_id, router_link, status, title, updated_date)
VALUES (2, '2025-09-12 17:05:45.186496', NULL, 10, 1, '{setting/user}', true, 'User', '2025-09-12 17:05:45.186496');

INSERT INTO public.features (id, created_date, icon, menu_order, parent_id, router_link, status, title, updated_date)
VALUES (3, '2025-09-12 17:06:07.010608', NULL, 15, 1, '{setting/role}', true, 'Role', '2025-09-12 17:06:07.010608');

-- Insert permissions
INSERT INTO public.permissions (id, created_date, function_name, function_order, status, updated_date)
VALUES (1, '2025-09-12 17:06:50.211713', 'Create', 1, true, '2025-09-12 17:06:50.211713');

INSERT INTO public.permissions (id, created_date, function_name, function_order, status, updated_date)
VALUES (2, '2025-09-12 17:06:57.504835', 'View', 2, true, '2025-09-12 17:06:57.504835');

INSERT INTO public.permissions (id, created_date, function_name, function_order, status, updated_date)
VALUES (3, '2025-09-12 17:07:05.587372', 'Update', 3, true, '2025-09-12 17:07:05.587372');

INSERT INTO public.permissions (id, created_date, function_name, function_order, status, updated_date)
VALUES (4, '2025-09-12 17:07:13.971475', 'Delete', 4, true, '2025-09-12 17:07:13.971475');

-- Insert feature_permissions
INSERT INTO public.feature_permissions (id, status, feature_id, permission_id)
VALUES (1, true, 1, 2);

INSERT INTO public.feature_permissions (id, status, feature_id, permission_id)
VALUES (2, true, 1, 1);

INSERT INTO public.feature_permissions (id, status, feature_id, permission_id)
VALUES (3, true, 1, 4);

INSERT INTO public.feature_permissions (id, status, feature_id, permission_id)
VALUES (4, true, 1, 3);

INSERT INTO public.feature_permissions (id, status, feature_id, permission_id)
VALUES (5, true, 2, 2);

INSERT INTO public.feature_permissions (id, status, feature_id, permission_id)
VALUES (6, true, 2, 1);

INSERT INTO public.feature_permissions (id, status, feature_id, permission_id)
VALUES (7, true, 2, 4);

INSERT INTO public.feature_permissions (id, status, feature_id, permission_id)
VALUES (8, true, 2, 3);

INSERT INTO public.feature_permissions (id, status, feature_id, permission_id)
VALUES (9, true, 3, 2);

INSERT INTO public.feature_permissions (id, status, feature_id, permission_id)
VALUES (10, true, 3, 1);

INSERT INTO public.feature_permissions (id, status, feature_id, permission_id)
VALUES (11, true, 3, 4);

INSERT INTO public.feature_permissions (id, status, feature_id, permission_id)
VALUES (12, true, 3, 3);

-- Insert role_feature
INSERT INTO public.role_feature (id, status, feature_id, role_id)
VALUES (1, true, 2, 1);

INSERT INTO public.role_feature (id, status, feature_id, role_id)
VALUES (2, true, 3, 2);

INSERT INTO public.role_feature (id, status, feature_id, role_id)
VALUES (3, true, 3, 2);

INSERT INTO public.role_feature (id, status, feature_id, role_id)
VALUES (4, true, 3, 2);

INSERT INTO public.role_feature (id, status, feature_id, role_id)
VALUES (5, true, 3, 1);

-- Insert accounts
INSERT INTO public.accounts (id, account_holder_email, account_holder_name, account_holder_phone, account_number, account_type, balance, created_date, national_id, status, updated_date, created_by)
VALUES (1, 'votha7777@gmail.com', 'SOK VOTHA', '015600022', '9294275253', 'SAVINGS', 0.00, '2025-09-15 14:26:51.291256', '3890823', true, '2025-09-15 14:26:51.291256', 1);

-- Update sequence values
SELECT setval('public.accounts_id_seq', 1, true);
SELECT setval('public.feature_permissions_id_seq', 12, true);
SELECT setval('public.features_id_seq', 3, true);
SELECT setval('public.permissions_id_seq', 4, true);
SELECT setval('public.role_feature_id_seq', 5, true);
SELECT setval('public.roles_seq', 51, true);
SELECT setval('public.user_roles_id_seq', 1, true);
SELECT setval('public.users_id_seq', 2, true);

-- Display confirmation message
SELECT 'Database created and populated successfully!' AS message;