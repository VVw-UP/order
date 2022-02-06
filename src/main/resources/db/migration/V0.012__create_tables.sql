create table t_holiday
(
    id                    numeric(38)  not null
        constraint t_holiday_pk
            primary key,
    ccy_id                numeric(38)
        constraint t_holiday_fk1
            references t_currency,
    holiday_date          date                                                   not null,
    create_timestamp      timestamp(6),
    create_by             numeric(38)                                            not null,
    last_modify_timestamp timestamp(6),
    last_modify_by        numeric(38),
    enable                char        default '1'::bpchar                        not null,
    ccy                   varchar(100),
    constraint t_holiday_uk
        unique (ccy_id, holiday_date)
);
create table t_trade_date_history
(
    id                    numeric(38),
    currency_id           numeric(38),
    currenct_trade_date   date,
    last_modify_timestamp timestamp(6)
);