create table statistics(
                           id bigint not null primary key,
                           event_time timestamp,
                           symbol varchar(15),
                           price_change numeric(40,20),
                           price_change_percent numeric(40,20),
                           weighted_avg_price numeric(40,20),
                           prev_close_price numeric(40,20),
                           last_price numeric(40,20),
                           last_qty numeric(40,20),
                           bid_price numeric(40,20),
                           bid_qty numeric(40,20),
                           ask_price numeric(40,20),
                           ask_qty numeric(40,20),
                           open_price numeric(40,20),
                           high_price numeric(40,20),
                           low_price numeric(40,20),
                           volume numeric(40,20),
                           quote_volume numeric(40,20),
                           open_time timestamp,
                           close_time timestamp,
                           first_trade_id bigint,
                           last_trade_id bigint,
                           count bigint
);

create sequence seq_statistics
    minvalue 1
    maxvalue 9999999999999999
    start with 1
    increment by 1;
alter table statistics alter column id set default nextval('seq_statistics');
