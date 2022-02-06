
-- change t_user 
ALTER TABLE t_user ADD CONSTRAINT t_user_t_entity_fk FOREIGN KEY (entity_id) REFERENCES t_entity(id);
-- end change t_user

-- change t_channel 
ALTER TABLE t_channel ADD CONSTRAINT t_channel_t_user_fk1 FOREIGN KEY (create_by) REFERENCES t_user(id);
ALTER TABLE t_channel ADD CONSTRAINT t_channel_t_user_fk2 FOREIGN KEY (last_modify_by) REFERENCES t_user(id);
-- end change t_channel

-- set up t_counterparty_type_seq

DROP SEQUENCE IF EXISTS t_counterparty_type_seq;

CREATE SEQUENCE t_counterparty_type_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;
	
-- end set up t_counterparty_type_seq

-- set up t_counterparty_type

DROP TABLE IF EXISTS t_counterparty_type;

CREATE TABLE t_counterparty_type (
	id numeric(38) NOT NULL DEFAULT nextval('t_counterparty_type_seq'),
	name varchar(100) NOT NULL,
	code varchar(100) NOT NULL,
	CONSTRAINT t_counterparty_type_pk PRIMARY KEY (id)
);

-- end set up t_counterparty_type

-- set up t_counterparty_category_seq

DROP SEQUENCE IF EXISTS t_counterparty_category_seq;

CREATE SEQUENCE t_counterparty_category_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_counterparty_category_seq

-- set up t_counterparty_category

DROP TABLE IF EXISTS t_counterparty_category;

CREATE TABLE t_counterparty_category (
	id numeric(38) NOT NULL DEFAULT nextval('t_counterparty_category_seq'),
	ms_segment_desc varchar(100) NOT NULL,
	mx_segment_code varchar(100) NULL,
	segment_code varchar(100) NULL,
	CONSTRAINT t_counterparty_category_pk PRIMARY KEY (id)
);

-- end set up t_counterparty_category

-- set up t_counterparty_seq

DROP SEQUENCE IF EXISTS t_counterparty_seq;

CREATE SEQUENCE t_counterparty_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;
	
-- end set up t_counterparty_seq

-- set up t_counterparty

DROP TABLE IF EXISTS t_counterparty;

CREATE TABLE t_counterparty (
	id numeric(38) NOT NULL DEFAULT nextval('t_counterparty_seq'),
	name varchar(100) NOT NULL,
	code varchar(100) NOT NULL,
	full_name varchar(100) NOT NULL,
	mx_label varchar(100) NULL,
	mx_name varchar(100) NULL,
	mx_cif varchar(100) NULL,
	mx_status varchar(100) NULL,
	mx_cls_m varchar(100) NULL,
	comments varchar(500) NULL,
	counterparty_type_id numeric(38) NOT NULL,
	counterparty_category_id numeric(38) NOT NULL,
	entity_id numeric(38) NOT NULL,
	create_timestamp timestamp NOT NULL,
	create_by numeric(38) NOT NULL,
	last_modify_timestamp timestamp NOT NULL,
	last_modify_by numeric(38) NOT NULL,
	enable varchar(1) NOT NULL DEFAULT '1',
	skipsales varchar(1) NOT NULL DEFAULT '1',
	skipecom varchar(1) NOT NULL DEFAULT '1',
	ecomid text NULL,
	ecomentity numeric(38) NULL,
	CONSTRAINT t_counterparty_pk PRIMARY KEY (id),
	CONSTRAINT t_counterparty_uk UNIQUE (code),
	CONSTRAINT t_counterparty_t_counterparty_type_fk FOREIGN KEY (counterparty_type_id) REFERENCES t_counterparty_type(id),
	CONSTRAINT t_counterparty_t_counterparty_category_fk FOREIGN KEY (counterparty_category_id) REFERENCES t_counterparty_category(id),
	CONSTRAINT t_counterparty_t_entity_fk FOREIGN KEY (entity_id) REFERENCES t_entity(id)
);

-- end set up t_counterparty

-- set up t_product_type_seq

DROP SEQUENCE IF EXISTS t_product_type_seq;

CREATE SEQUENCE t_product_type_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_product_type_seq

-- set up t_product_type_seq

DROP SEQUENCE IF EXISTS t_product_type_seq;

CREATE SEQUENCE t_product_type_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_product_type_seq

-- set up t_product_type

DROP TABLE IF EXISTS t_product_type;

CREATE TABLE t_product_type (
	id numeric(38) NOT NULL DEFAULT nextval('t_product_type_seq'),
	code varchar(1024) NOT NULL,
	name varchar(100) NOT NULL,
	CONSTRAINT t_product_type_pk PRIMARY KEY (id),
	CONSTRAINT t_product_type_uk UNIQUE (code)
);

-- end set up t_product_type

-- set up t_order_seq

DROP SEQUENCE IF EXISTS t_order_seq;

CREATE SEQUENCE t_order_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_order_seq

-- set up t_order

DROP TABLE IF EXISTS t_order;

CREATE TABLE t_order (
	id numeric(38) NOT NULL DEFAULT nextval('t_order_seq'),
	uniq_id varchar(100) NOT NULL, 
	product_type_id numeric(38) NOT NULL,
	order_status_id numeric(38) NOT NULL,
	order_type_id numeric(38) NOT NULL,
	counterparty_id numeric(38) NULL,
	client_id numeric(38) NOT NULL,
	create_by numeric(38) NOT NULL,
	create_timestamp timestamp NOT NULL,
	last_modify_by numeric(38) NOT NULL,
	last_modify_timestamp timestamp NOT NULL,
	on_behalf_of numeric(38) NOT NULL,
	bos_sale_id numeric(38) NULL,
	channel_id numeric(38) NOT NULL,
	CONSTRAINT t_order_pk PRIMARY KEY (id),
	CONSTRAINT t_order_uk UNIQUE (uniq_id),
	CONSTRAINT t_order_t_product_type_fk FOREIGN KEY (product_type_id) REFERENCES t_product_type(id),
	CONSTRAINT t_order_t_order_type_fk FOREIGN KEY (order_type_id) REFERENCES t_order_type(id),
	CONSTRAINT t_order_t_order_status_fk FOREIGN KEY (order_status_id) REFERENCES t_order_status(id),
	CONSTRAINT t_order_t_channel_fk FOREIGN KEY (channel_id) REFERENCES t_channel(id),
	CONSTRAINT t_order_t_client_fk FOREIGN KEY (client_id) REFERENCES t_client(id),
	CONSTRAINT t_order_t_user_fk1 FOREIGN KEY (create_by) REFERENCES t_user(id),
	CONSTRAINT t_order_t_user_fk2 FOREIGN KEY (last_modify_by) REFERENCES t_user(id),
	CONSTRAINT t_order_t_user_fk3 FOREIGN KEY (on_behalf_of) REFERENCES t_user(id),
	CONSTRAINT t_order_t_user_fk4 FOREIGN KEY (bos_sale_id) REFERENCES t_user(id),
	CONSTRAINT t_order_t_counterparty_fk FOREIGN KEY (counterparty_id) REFERENCES t_counterparty(id)
);

-- end set up t_order

-- set up t_trade_status_seq

DROP SEQUENCE IF EXISTS t_trade_status_seq;

CREATE SEQUENCE t_trade_status_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;
	
-- end set up t_trade_status_seq

-- set up t_trade_status

DROP TABLE IF EXISTS t_trade_status;

CREATE TABLE t_trade_status (
	id numeric(38) NOT NULL DEFAULT nextval('t_trade_status_seq'),
	name varchar(100) NOT NULL,
	code varchar(100) NOT NULL,
	CONSTRAINT t_trade_status_pk PRIMARY KEY (id),
	CONSTRAINT t_trade_status_uk UNIQUE (code)
);

-- end set up t_trade_status

-- set up t_currency_seq definition

DROP SEQUENCE IF EXISTS t_currency_seq;

CREATE SEQUENCE t_currency_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;
	
-- set up t_currency_seq definition

-- set up t_currency

DROP TABLE IF EXISTS t_currency;

CREATE TABLE t_currency (
	id numeric(38) NOT NULL DEFAULT nextval('t_currency_seq'),
	code varchar(100) NOT NULL,
	name varchar(100) NOT NULL,
	spot_days numeric(10) NOT NULL,
	create_timestamp timestamp NOT NULL,
	create_by numeric(38) NOT NULL,
	last_modify_timestamp timestamp NOT NULL,
	last_modify_by numeric(38) NOT NULL,
	enable boolean NOT NULL DEFAULT TRUE,
	CONSTRAINT t_currency_pk PRIMARY KEY (id),
	CONSTRAINT t_currency_code_uk UNIQUE (code),
	CONSTRAINT t_currency_t_user_fk1 FOREIGN KEY (create_by) REFERENCES t_user(id),
	CONSTRAINT t_currency_t_user_fk2 FOREIGN KEY (last_modify_by) REFERENCES t_user(id)
);

-- end set up t_currency

-- set up t_currency_pair_seq

DROP SEQUENCE IF EXISTS t_currency_pair_seq;

CREATE SEQUENCE t_currency_pair_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_currency_pair_seq

-- set up t_currency_pair

DROP TABLE IF EXISTS t_currency_pair;

CREATE TABLE t_currency_pair (
	id numeric(38) NOT NULL DEFAULT nextval('t_currency_pair_seq'),
	ccy_pair varchar(100) NOT NULL,
	base_ccy_id numeric(38) NOT NULL,
	term_ccy_id numeric(38) NOT NULL,
	spot_pips numeric(18, 8) NOT NULL,
	forward_pips numeric(18, 8) NOT NULL,
	rate_stale_time numeric(38) NULL,
	no_fly_zone_pips numeric(18) NOT NULL,
	temp_pips_very_hot numeric(18) NOT NULL,
	temp_pips_hot numeric(18) NOT NULL,
	temp_pips_warm numeric(18) NOT NULL,
	temp_pips_cold numeric(18) NOT NULL,
	temp_pips_inactive numeric(18) NOT NULL,
	create_timestamp timestamp NOT NULL,
	create_by numeric(38) NOT NULL,
	last_modify_timestamp timestamp NOT NULL,
	last_modify_by numeric(38) NOT NULL,
	enable varchar(1) NOT NULL DEFAULT '1',
	CONSTRAINT t_currency_pair_pk PRIMARY KEY (id),
	CONSTRAINT t_currency_pair_uk UNIQUE (ccy_pair),
	CONSTRAINT t_currency_pair_t_currency_fk1 FOREIGN KEY (base_ccy_id) REFERENCES t_currency(id),
	CONSTRAINT t_currency_pair_t_currency_fk2 FOREIGN KEY (term_ccy_id) REFERENCES t_currency(id),
	CONSTRAINT t_currency_pair_t_user_fk1 FOREIGN KEY (create_by) REFERENCES t_user(id),
	CONSTRAINT t_currency_pair_t_user_fk2 FOREIGN KEY (last_modify_by) REFERENCES t_user(id)
);

-- end set up t_currency_pair

-- set up t_timezone_seq

DROP SEQUENCE IF EXISTS t_timezone_seq;

CREATE SEQUENCE t_timezone_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;
	
-- end set up t_timezone_seq	

-- set up t_timezone

DROP TABLE IF EXISTS t_timezone;

CREATE TABLE t_timezone (
	id numeric(38) NOT NULL DEFAULT nextval('t_timezone_seq'),
	code varchar(100) NOT NULL,
	CONSTRAINT t_timezone_pk PRIMARY KEY (id),
	CONSTRAINT t_timezone_uk UNIQUE (code)
);

-- end set up t_timezone

-- set up t_time_in_force_seq

DROP SEQUENCE IF EXISTS t_time_in_force_seq;

CREATE SEQUENCE t_time_in_force_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_time_in_force_seq

-- set up t_time_in_force

DROP TABLE IF EXISTS t_time_in_force;

CREATE TABLE t_time_in_force (
	id numeric(38) NOT NULL DEFAULT nextval('t_time_in_force_seq'),
	name varchar(100) NOT NULL,
	code varchar(100) NOT NULL,
	CONSTRAINT t_time_in_force_pk PRIMARY KEY (id),
	CONSTRAINT t_time_in_force_uk UNIQUE (code)
);

-- end set up t_time_in_force

-- set up t_watch_type_seq

DROP SEQUENCE IF EXISTS t_watch_type_seq;

CREATE SEQUENCE t_watch_type_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_watch_type_seq

-- set up t_watch_type

DROP TABLE IF EXISTS t_watch_type;

CREATE TABLE t_watch_type (
	id numeric(38) NOT NULL DEFAULT nextval('t_watch_type_seq'),
	name varchar(100) NOT NULL,
	code varchar(100) NOT NULL,
	CONSTRAINT t_watch_type_pk PRIMARY KEY (id),
	CONSTRAINT t_watch_type_uk UNIQUE (code)
);

-- end set up t_watch_type

-- set up t_country_seq

DROP SEQUENCE IF EXISTS t_country_seq;

CREATE SEQUENCE t_country_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_country_seq

-- set up t_country

DROP TABLE IF EXISTS t_country;

CREATE TABLE t_country (
	id numeric(38) NOT NULL DEFAULT nextval('t_country_seq'),
	name varchar(100) NOT NULL,
	code varchar(100) NOT NULL,
	CONSTRAINT t_country_pk PRIMARY KEY (id),
	CONSTRAINT t_country_uk UNIQUE (code)
);

-- end set up t_country

-- set up t_desk_seq

DROP SEQUENCE IF EXISTS t_desk_seq;

CREATE SEQUENCE t_desk_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_desk_seq

-- set up t_desk

DROP TABLE IF EXISTS t_desk;

CREATE TABLE t_desk (
	id numeric(38) NOT NULL DEFAULT nextval('t_desk_seq'),
	code varchar(100) NOT NULL,
	name varchar(100) NOT NULL,
	enable varchar(1) NOT NULL DEFAULT '1',
	create_timestamp timestamp NOT NULL,
	create_by numeric(38) NOT NULL,
	last_modify_timestamp timestamp NOT NULL,
	last_modify_by numeric(38) NOT NULL,
	CONSTRAINT t_desk_pk PRIMARY KEY (id),
	CONSTRAINT t_desk_uk UNIQUE (code),
	CONSTRAINT t_desk_t_user_fk1 FOREIGN KEY (last_modify_by) REFERENCES t_user(id),
	CONSTRAINT t_desk_t_user_fk2 FOREIGN KEY (create_by) REFERENCES t_user(id)
	
);

-- end set up t_desk

-- set up t_rate_source_seq

DROP SEQUENCE IF EXISTS t_rate_source_seq;

CREATE SEQUENCE t_rate_source_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_rate_source_seq

-- set up t_rate_source

DROP TABLE IF EXISTS t_rate_source;

CREATE TABLE t_rate_source (
	id numeric(38) NOT NULL DEFAULT nextval('t_rate_source_seq'),
	name varchar(100) NOT NULL,
	code varchar(100) NOT NULL,
	ordr numeric(2) NOT NULL,
	enable varchar(1) NOT NULL DEFAULT '1',
	config_details text NULL,
	CONSTRAINT t_rate_source_pk PRIMARY KEY (id),
	CONSTRAINT t_rate_source_uk UNIQUE (code)
);

-- end set up t_rate_source

-- set up t_tenor_seq

DROP SEQUENCE IF EXISTS t_tenor_seq;

CREATE SEQUENCE t_tenor_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;
	
-- end set up t_tenor_seq
	
-- set up t_tenor

DROP TABLE IF EXISTS t_tenor;

CREATE TABLE t_tenor (
	id numeric(38) NOT NULL DEFAULT nextval('t_tenor_seq'),
	code varchar(1024) NOT NULL,
	name varchar(100) NOT NULL,
	rate_source_id numeric(38) NOT NULL,
	CONSTRAINT t_tenor_pk PRIMARY KEY (id),
	CONSTRAINT t_tenor_uk UNIQUE (code),
	CONSTRAINT t_tenor_t_rate_source_fk FOREIGN KEY (rate_source_id) REFERENCES t_rate_source(id)
);

-- end set up t_tenor

-- set up t_fix_price_source_seq

DROP SEQUENCE IF EXISTS t_fix_price_source_seq;

CREATE SEQUENCE t_fix_price_source_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_fix_price_source_seq

-- set up t_fix_price_source

DROP TABLE IF EXISTS t_fix_price_source;

CREATE TABLE t_fix_price_source (
	id numeric(38) NOT NULL DEFAULT nextval('t_fix_price_source_seq'),
	code varchar(100) NOT NULL,
	name varchar(100) NOT NULL,
	fix_time varchar(100) NOT NULL,
	enable varchar(1) NOT NULL DEFAULT '1',
	CONSTRAINT t_fix_price_source_pk PRIMARY KEY (id),
	CONSTRAINT t_fix_price_source_uk UNIQUE (code)
);

-- end set up t_fix_price_source

-- set up t_direction_seq

DROP SEQUENCE IF EXISTS t_direction_seq;

CREATE SEQUENCE t_direction_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_direction_seq

-- set up t_direction_source

DROP TABLE IF EXISTS t_direction;

CREATE TABLE t_direction (
	id numeric(38) NOT NULL DEFAULT nextval('t_direction_seq'),
	code varchar(100) NOT NULL,
	name varchar(100) NOT NULL,
	CONSTRAINT t_direction_pk PRIMARY KEY (id)
);

-- end set up t_direction

-- set up t_trade_seq

DROP SEQUENCE IF EXISTS t_trade_seq;

CREATE SEQUENCE t_trade_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_trade_seq

-- set up t_trade

DROP TABLE IF EXISTS t_trade;

CREATE TABLE t_trade (
	id NUMERIC(38) NOT NULL DEFAULT nextval('t_trade_seq'),
	order_id NUMERIC(38) NOT NULL,
	trade_status_id NUMERIC(38) NOT NULL,
	settlement_date DATE NULL,
	trade_date DATE NULL,
	watch_type_id NUMERIC(38) NOT NULL,
	ccy_pair_id NUMERIC(38) NOT NULL,
	dealt_ccy_id NUMERIC(38) NULL,
	direction_id NUMERIC(38) NULL,
	price NUMERIC(38, 8) NOT NULL,
	allow_partial_fill BOOLEAN NULL DEFAULT FALSE,
	time_in_force_id NUMERIC(38) NOT NULL,
	expire_timestamp TIMESTAMP NULL,
	expire_timezone_id NUMERIC(38) NULL,
	call_required BOOLEAN NULL DEFAULT FALSE,
	dealt_amt NUMERIC(38, 8) NULL,
	contra_amt NUMERIC(38, 8) NULL,
	create_timestamp timestamp NOT NULL,
	create_by numeric(38) NOT NULL,
	last_modify_timestamp timestamp NOT NULL,
	last_modify_by numeric(38) NOT NULL,
	CONSTRAINT t_trade_pk PRIMARY KEY (id),
	CONSTRAINT t_trade_t_order_fk FOREIGN KEY (order_id) REFERENCES t_order(id),
	CONSTRAINT t_trade_t_trade_status_fk FOREIGN KEY (trade_status_id) REFERENCES t_trade_status(id),
	CONSTRAINT t_trade_t_watch_type_fk FOREIGN KEY (watch_type_id) REFERENCES t_watch_type(id),
	CONSTRAINT t_trade_t_currency_pair_fk FOREIGN KEY (ccy_pair_id) REFERENCES t_currency_pair(id),
	CONSTRAINT t_trade_t_currency_fk FOREIGN KEY (dealt_ccy_id) REFERENCES t_currency(id),
	CONSTRAINT t_trade_t_direction_fk FOREIGN KEY (direction_id) REFERENCES t_direction(id),
	CONSTRAINT t_trade_t_time_in_force_fk FOREIGN KEY (time_in_force_id) REFERENCES t_time_in_force(id),
	CONSTRAINT t_trade_t_timezone_fk FOREIGN KEY (expire_timezone_id) REFERENCES t_timezone(id),
	CONSTRAINT t_trade_t_user_fk1 FOREIGN KEY (create_by) REFERENCES t_user(id),
	CONSTRAINT t_trade_t_user_fk2 FOREIGN KEY (last_modify_by) REFERENCES t_user(id)	
);

-- end set up t_trade

-- set up t_customer_segment_code_seq

DROP SEQUENCE IF EXISTS t_customer_segment_code_seq;

CREATE SEQUENCE t_customer_segment_code_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_customer_segment_code_seq

-- set up t_customer_segment_code

DROP TABLE IF EXISTS t_customer_segment_code;

CREATE TABLE t_customer_segment_code (
	id numeric(38) NOT NULL DEFAULT nextval('t_customer_segment_code_seq'),
	entity_id numeric(38) NOT NULL,
	segment_code varchar(1) NOT NULL,
	segment_description varchar(100) NOT NULL,
	segment_type varchar(3) NOT NULL,
	code_mx numeric(3) NOT NULL,
	sales_id numeric(38) NULL,
	CONSTRAINT t_customer_segment_code_pk PRIMARY KEY (id),
	CONSTRAINT t_customer_segment_code_t_entity_fk FOREIGN KEY (entity_id) REFERENCES t_entity(id),
	CONSTRAINT t_customer_segment_code_t_user_fk FOREIGN KEY (sales_id) REFERENCES t_user(id)
);

-- end set up t_customer_segment_code

-- set up t_user_channel_seq

DROP SEQUENCE IF EXISTS t_user_channel_seq;

CREATE SEQUENCE t_user_channel_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;
	
-- end set up t_user_channel_seq
	
-- set up t_tenor

DROP TABLE IF EXISTS t_user_channel;

CREATE TABLE t_user_channel (
	id numeric(38) NOT NULL DEFAULT nextval('t_user_channel_seq'),
	user_id numeric(38) NOT NULL,
	channel_id numeric(38) NOT NULL,
	CONSTRAINT t_user_channel_pk PRIMARY KEY (id),
	CONSTRAINT t_user_channel_uk UNIQUE (user_id,channel_id),
	CONSTRAINT t_user_channel_t_user_fk FOREIGN KEY (user_id) REFERENCES t_user(id),
	CONSTRAINT t_user_channel_t_channel_fk FOREIGN KEY (channel_id) REFERENCES t_channel(id)
);

-- end set up t_user_channel

-- set up t_cfs_properties_seq

DROP SEQUENCE IF EXISTS t_cfs_properties_seq;

CREATE SEQUENCE t_cfs_properties_seq
	INCREMENT BY 1
	MINVALUE 1
	START 1
	CACHE 1
	NO CYCLE;

-- end set up t_cfs_properties_seq

-- set up t_cfs_properties

DROP TABLE IF EXISTS t_cfs_properties;

CREATE TABLE t_cfs_properties (
	id NUMERIC(38) NOT NULL DEFAULT nextval('t_cfs_properties_seq'),
	order_id NUMERIC(38) NOT NULL,
	cif_number varchar(100) NOT NULL,
	customer_seg_id NUMERIC(38) NOT NULL,
        channel_code varchar(100) NOT NULL,
        product_code varchar(100) NOT NULL,
        all_in_price NUMERIC(38, 8) NOT NULL,
        fill_timestamp timestamp NULL,
	CONSTRAINT t_cfs_properties_pk PRIMARY KEY (id),
	CONSTRAINT t_cfs_properties_t_order_fk FOREIGN KEY (order_id) REFERENCES t_order(id),
	CONSTRAINT t_cfs_properties_t_customer_segment_code_fk FOREIGN KEY (customer_seg_id) REFERENCES t_customer_segment_code(id)
		
);

-- end set up t_cfs_properties

