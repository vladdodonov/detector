create table agg_trade_process(
                           id bigint not null primary key,
                           symbol varchar(15),
                           event_time timestamp,
                           buy_q numeric(40,20),
                           sell_q numeric(40,20),
                           buy_ratio numeric(40,20),
                           is_pump bool
);

create sequence seq_agg_trade_process
    minvalue 1
    maxvalue 9999999999999999
    start with 1
    increment by 1;
alter table agg_trade_process alter column id set default nextval('seq_agg_trade_process');
