-- Postgresql schema for dataround_admin --
-- Before starting Spring Boot, please create the database dataround_admin first. --
CREATE TABLE IF NOT EXISTS public.user (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	"name" varchar NOT NULL,
	cellphone varchar NOT NULL,
	email varchar NOT NULL,
	passwd varchar NOT NULL,
	disabled bool NOT NULL,
    creator_id int8 NOT NULL,
	create_time timestamp with time zone NOT NULL,
	update_time timestamp with time zone NOT NULL
);
ALTER SEQUENCE user_id_seq RESTART WITH 10000;
COMMENT ON COLUMN public.user.name IS 'login field';
COMMENT ON COLUMN public.user.cellphone IS 'login field';
COMMENT ON COLUMN public.user.email IS 'login field';
INSERT INTO public.user("name", cellphone, email, passwd, disabled, creator_id, create_time, update_time) 
VALUES ('admin', '13800000000', 'admin@dataround.io', '38ae9456d40553d1213ea238c2f7a49f52762d19fc29939968d6e0a51a569dcb', false, 10000, now(), now()) 
ON CONFLICT (id) DO NOTHING;

CREATE TABLE IF NOT EXISTS public.project (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	"name" varchar NOT NULL,
	description varchar NOT NULL,
	creator_id int8 NOT NULL,
	create_time timestamp with time zone NOT NULL
);
ALTER SEQUENCE project_id_seq RESTART WITH 10000;
INSERT INTO public.project("name", description, creator_id, create_time) 
VALUES ('default', '', 10000, now()) 
ON CONFLICT (id) DO NOTHING;

CREATE TABLE IF NOT EXISTS public.project_user (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	user_id int8 NOT NULL,
	project_id int8 NOT NULL,
	is_admin bool NOT NULL default false,
	selected bool NOT NULL default false,
	create_time timestamp with time zone
);
ALTER SEQUENCE project_user_id_seq RESTART WITH 10000;

CREATE TABLE IF NOT EXISTS public.user_role (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	user_id int8 NOT NULL,
	role_id int8 NOT NULL,
	create_time timestamp with time zone NOT NULL
);
ALTER SEQUENCE user_role_id_seq RESTART WITH 10000;

CREATE TABLE IF NOT EXISTS public.role (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	"name" varchar NOT NULL,
	description varchar NOT NULL,
	create_time timestamp with time zone NOT NULL
);
ALTER SEQUENCE role_id_seq RESTART WITH 10000;

CREATE TABLE IF NOT EXISTS public.role_resource (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	role_id int8 NOT NULL,
	resource_id int8 NOT NULL,
	create_time timestamp with time zone NOT NULL
);
ALTER SEQUENCE role_resource_id_seq RESTART WITH 10000;

CREATE TABLE IF NOT EXISTS public.resource (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	pid int8 NOT NULL,
	"name" varchar NOT NULL,
	en_name varchar NOT NULL,
	path varchar NOT NULL,
	method varchar NULL,
	ui_key varchar NULL,
	description varchar NOT NULL,
	create_time timestamp with time zone NOT NULL
);
ALTER SEQUENCE resource_id_seq RESTART WITH 10000;
