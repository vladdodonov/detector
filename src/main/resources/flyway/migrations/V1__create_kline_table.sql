create table kline(
id bigint not null primary key,
event_type varchar(5),
event_time timestamp,
symbol varchar(15),
open_time timestamp,
open numeric(40,20),
high numeric(40,20),
low numeric(40,20),
close numeric(40,20),
volume numeric(40,20),
close_time timestamp,
interval_id varchar(10),
first_trade_id bigint,
last_trade_id bigint,
quote_asset_volume numeric(40,20),
number_of_trades bigint,
taker_buy_base_asset_volume numeric(40,20),
taker_buy_quote_asset_volume numeric(40,20),
is_bar_final bool
);

create sequence seq_kline
minvalue 1
maxvalue 9999999999999999
start with 1
increment by 1;
alter table kline alter column id set default nextval('seq_kline');

create index kline$idx$open_time on kline using brin (open_time);
