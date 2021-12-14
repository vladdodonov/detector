create table agg_trade
(
    id             bigint not null primary key,
    event_time     timestamp,
    symbol         varchar(100),
    price          numeric(40, 20),
    quantity       numeric(40, 20),
    trade_time     timestamp,
    is_buyer_maker bool

);

create sequence seq_agg_trade
    minvalue 1
    maxvalue 9999999999999999
    start with 1
    increment by 1;
alter table agg_trade
    alter column id set default nextval('seq_agg_trade');
