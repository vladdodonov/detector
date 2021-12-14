create table abnormal_trading
(
    id             bigint not null primary key,
    notice_type    varchar(100),
    symbol         varchar(100),
    event_type     varchar(100),
    volume         numeric(40, 20),
    price_change   numeric(40, 20),
    period         varchar(100),
    send_timestamp timestamp,
    base_asset     varchar(100),
    quota_asset    varchar(100)

);

create sequence seq_abnormal_trading
    minvalue 1
    maxvalue 9999999999999999
    start with 1
    increment by 1;
alter table abnormal_trading
    alter column id set default nextval('seq_abnormal_trading');
