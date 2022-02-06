-- t_idempotent_guarantee Table
DROP SEQUENCE IF EXISTS t_idempotent_guarantee_seq;

create SEQUENCE t_idempotent_guarantee_seq
Increment 1
start 1
minvalue 1
maxvalue 99999999
cache 1;

CREATE TABLE t_idempotent_guarantee (
  id numeric(38,0) NOT NULL DEFAULT nextval('t_idempotent_guarantee_seq'::regclass),
  component_name varchar(100) NOT NULL,
  idempotent_value numeric(2,0) NOT NULL,
  update_time timestamp(0) DEFAULT now(),
  CONSTRAINT t_idempotent_guarantee_pk PRIMARY KEY (id)
);

COMMENT ON TABLE t_idempotent_guarantee IS '幂等性保证表';
COMMENT ON COLUMN t_idempotent_guarantee.id IS '主键，自增';
COMMENT ON COLUMN t_idempotent_guarantee.component_name IS '组件名称';
COMMENT ON COLUMN t_idempotent_guarantee.idempotent_value IS '幂等值，修改为1成功的服务执行任务';
COMMENT ON COLUMN t_idempotent_guarantee.update_time IS '修改时间';