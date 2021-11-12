drop table if exists message
;

create table if not exists message
(
    id   serial primary key,
    data varchar(2048) not null
)
;
