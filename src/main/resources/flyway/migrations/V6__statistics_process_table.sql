create table statistics_process(
                           id bigint not null primary key,
                           event_time timestamp,
                           symbol varchar(20),
                           ratio numeric(40,20),
                           high numeric(40,20),
                           low numeric(40,20)
);

create sequence seq_statistics_process
    minvalue 1
    maxvalue 9999999999999999
    start with 1
    increment by 1;
alter table statistics_process alter column id set default nextval('seq_statistics_process');
