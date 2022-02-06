DROP SEQUENCE IF EXISTS t_export_record_seq;

create SEQUENCE t_export_record_seq
Increment 1
start 1
minvalue 1
maxvalue 99999999
cache 1;


CREATE TABLE "t_export_record" (
  "id" numeric(38,0) NOT NULL DEFAULT nextval('t_export_record_seq'::regclass),
  "file_type" int4 NOT NULL,
  "execut_type" int4 NOT NULL DEFAULT 0,
  "create_time" timestamp(6),
  CONSTRAINT "t_export_record_pk" PRIMARY KEY ("id")
)
;