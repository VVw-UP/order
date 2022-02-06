
-- set up t_client_seq

DROP SEQUENCE IF EXISTS t_client_seq;

CREATE SEQUENCE t_client_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_client_seq

-- set up t_client

DROP TABLE IF EXISTS t_client;

CREATE TABLE t_client (
	id numeric(38) NOT NULL DEFAULT nextval('t_client_seq'),
	name varchar(100) NOT NULL,
	CONSTRAINT t_client_pk PRIMARY KEY (id)
);

-- end set up t_user

-- set up t_user_seq

DROP SEQUENCE IF EXISTS t_user_seq;

CREATE SEQUENCE t_user_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_user_seq

-- set up t_entity_seq

DROP SEQUENCE IF EXISTS t_entity_seq;

CREATE SEQUENCE t_entity_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_entity_seq

-- set up t_entity

DROP TABLE IF EXISTS t_entity;

CREATE TABLE t_entity (
	id numeric(38) NOT NULL DEFAULT nextval('t_entity_seq'),
	code varchar(100) NOT NULL,
	name varchar(100) NOT NULL,
	mx_orderowner_id numeric(38) NULL,
	mx_riskowner_id numeric(38) NULL,
	enable boolean NOT NULL DEFAULT TRUE,
	create_timestamp timestamp NOT NULL,
	create_by numeric(38) NOT NULL,
	last_modify_timestamp timestamp NOT NULL,
	last_modify_by numeric(38) NOT NULL,
	CONSTRAINT t_entity_pk PRIMARY KEY (id),
	CONSTRAINT t_entity_uk UNIQUE (code)
);

-- end set up t_entity

-- set up t_user

DROP TABLE IF EXISTS t_user;

CREATE TABLE t_user (
	id numeric(38) NOT NULL DEFAULT nextval('t_user_seq'),
	first_name varchar(100) NOT NULL,
	last_name varchar(100) NOT NULL,
	enable BOOLEAN NOT NULL DEFAULT TRUE,
	email varchar(100) NULL,
	email_notification BOOLEAN NOT NULL DEFAULT FALSE,
	sms_notification BOOLEAN NOT NULL DEFAULT FALSE,
	mobile varchar(100) NULL,
	create_timestamp timestamp NOT NULL,
	create_by numeric(38) NOT NULL,
	last_modify_timestamp timestamp NOT NULL,
	last_modify_by numeric(38) NOT NULL,
	mx_orderowner_id numeric(38) NULL,
	mx_riskowner_id numeric(38) NULL,
	entity_id numeric(38) NOT NULL,
	skipsales BOOLEAN NOT NULL DEFAULT TRUE,
	skipecom BOOLEAN NOT NULL DEFAULT TRUE,
	sale_id text NULL,
	murex_name text NULL,
	murex_group text NULL,
	ecomid text NULL,
	ecomentity numeric(38) NULL,
	CONSTRAINT t_user_pk PRIMARY KEY (id),
	CONSTRAINT t_user_uk_fn_ln UNIQUE (first_name, last_name)
);

-- end set up t_user

-- set up t_channel_seq

DROP SEQUENCE IF EXISTS t_channel_seq;

CREATE SEQUENCE t_channel_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_channel_seq

-- set up t_channel

DROP TABLE IF EXISTS t_channel;

create table t_channel
(
  id numeric(38) NOT NULL DEFAULT nextval('t_channel_seq'),
  code varchar(100) not null,
  name varchar(100) not null,
  enable char(1) default '1' not null,
  create_timestamp timestamp  not null,
  create_by numeric(38) not null,
  last_modify_timestamp timestamp not null,
  last_modify_by numeric(38) not null,
  CONSTRAINT t_channel_pk PRIMARY KEY (id)
);

-- end set up t_channel

-- set up t_order_status_seq

DROP SEQUENCE IF EXISTS t_order_status_seq;

CREATE SEQUENCE t_order_status_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_order_status_seq

-- set up t_order_type

DROP TABLE IF EXISTS t_order_status;

CREATE TABLE t_order_status
(
	id numeric(38) NOT NULL DEFAULT nextval('t_order_status_seq'),
	name varchar(100) not null,
	code varchar(100) not null,
	CONSTRAINT t_order_status_pk PRIMARY KEY (id)
);

-- end set up t_order_type

-- set up t_order_type_seq

DROP SEQUENCE IF EXISTS t_order_type_seq;

CREATE SEQUENCE t_order_type_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;
	
-- end set up t_order_type_seq

-- set up t_order_type

DROP TABLE IF EXISTS t_order;

CREATE TABLE t_order_type
(
  id numeric(38) NOT NULL DEFAULT nextval('t_order_type_seq'),
  code varchar(1024) NOT NULL,
  name varchar(100) NOT NULL,
  order_category varchar(100) NOT NULL DEFAULT 'Simple',
  CONSTRAINT t_order_type_pk PRIMARY KEY (id)
);

-- end set up t_order_type

