-- necessary
-- trader_spread Table
DROP SEQUENCE IF EXISTS trader_spread_seq;

create SEQUENCE trader_spread_seq
Increment 1
start 1
minvalue 1
maxvalue 99999999
cache 1;

CREATE TABLE trader_spread (
  id numeric(38,0) NOT NULL DEFAULT nextval('trader_spread_seq'),
  user_id numeric(38,0),
  ccy_par varchar(100) NOT NULL,
  spread  numeric(38,5),
  murex_booking_type numeric(38,0),
  create_timestamp timestamp(6) NOT NULL DEFAULT now(),
  CONSTRAINT trader_spread_pk PRIMARY KEY (id)
);
DROP INDEX if exists trader_spread_user_id_index;
CREATE INDEX trader_spread_user_id_index ON trader_spread (user_id);
--
--
-- spread Table
DROP SEQUENCE IF EXISTS spread_seq;

create SEQUENCE spread_seq
Increment 1
start 1
minvalue 1
maxvalue 99999999
cache 1;

CREATE TABLE spread (
  id numeric(38,0) NOT NULL DEFAULT nextval('spread_seq'),
  ccy_par varchar(100) not null,
  spread_from numeric(38,0),
  spread_to numeric(38,0),
  spread_point numeric(38,5),
  user_id numeric(38,0),
  create_timestamp timestamp(6) NOT NULL DEFAULT now(),
  CONSTRAINT spread_pk PRIMARY KEY (id)
)
;
--
DROP INDEX if exists spread_user_id_index;
CREATE INDEX spread_user_id_index ON spread (user_id);


-- customer_segment Table
DROP SEQUENCE IF EXISTS customer_segment_seq;

create SEQUENCE customer_segment_seq
Increment 1
start 1
minvalue 1
maxvalue 99999999
cache 1;

CREATE TABLE customer_segment (
  id numeric(38,0) NOT NULL DEFAULT nextval('customer_segment_seq'),
  user_id numeric(38,0),
  customer_name varchar(100) NOT NULL,
  entity_id numeric(38,0),
  segment_code varchar(100) NOT NULL,
  segment_description varchar(100) NOT NULL,
  segment_type_id numeric(38,0),
  sales_id varchar(100) NOT NULL DEFAULT ''::character varying,
  code_mx varchar(100) ,
  create_timestamp timestamp(6) NOT NULL DEFAULT now(),
  CONSTRAINT customer_segment_pk PRIMARY KEY (id)
);

DROP INDEX if exists customer_segment_user_id_index;
CREATE INDEX customer_segment_user_id_index ON customer_segment (user_id);


-- global_dict Table
DROP SEQUENCE IF EXISTS global_dict_seq;

create SEQUENCE global_dict_seq
Increment 1
start 1
minvalue 1
maxvalue 99999999
cache 1;

CREATE TABLE global_dict (
  id numeric(38,0) NOT NULL DEFAULT nextval('global_dict_seq'),
	name varchar(100) NOT NULL,
	p_id numeric(38,0),
  full_path varchar(100) NOT NULL,
	create_timestamp timestamp(6) NOT NULL DEFAULT now(),
  CONSTRAINT global_dict_pk PRIMARY KEY (id)
);
--
DROP INDEX if exists global_dict_full_path_index;
CREATE UNIQUE INDEX global_dict_full_path_index on global_dict (full_path);

comment on table global_dict is '全局字典表';
comment on column global_dict.id is '主键，自增';
comment on column global_dict.name is 'code 翻译';
comment on column global_dict.p_id is '父id';
comment on column global_dict.full_path is '父id路径';
comment on column global_dict.create_timestamp is '创建时间';

-- expiry_time Table
DROP SEQUENCE IF EXISTS t_expiry_time_seq;

create SEQUENCE t_expiry_time_seq
    Increment 1
start 1
minvalue 1
maxvalue 99999999
cache 1;

CREATE TABLE t_expiry_time (
    id numeric (38,0) NOT NULL DEFAULT nextval('t_expiry_time_seq'),
    order_type_id numeric (38),
    days numeric (38),
    hours numeric (2),
    minutes numeric (2),
    create_timestamp timestamp(6) NOT NULL DEFAULT now(),
    CONSTRAINT t_expiry_time_pk PRIMARY KEY (id)
);

DROP INDEX if exists expiry_time_order_id_index;
CREATE INDEX expiry_time_order_id_index ON t_expiry_time (order_type_id);

comment on table t_expiry_time is '订单过期时间表';
comment on column t_expiry_time.id is '主键，自增';
comment on column t_expiry_time.order_type_id is '订单类型id';
comment on column t_expiry_time.days is '天';
comment on column t_expiry_time.hours is '时';
comment on column t_expiry_time.minutes is '分';
comment on column t_expiry_time.create_timestamp is '创建时间';