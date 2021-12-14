create table pump_check
(
    id               bigint not null primary key,
    event_time       timestamp,
    symbol           varchar(20),
    abnormal_trading bigint,
    huge_volumes     bigint,
    start_timeframe  timestamp
);

create sequence seq_pump_check
    minvalue 1
    maxvalue 9999999999999999
    start with 1
    increment by 1;
alter table pump_check
    alter column id set default nextval('seq_pump_check');
