create table if not exists "room" (
    id          uuid        not null primary key,
    number      varchar(20) not null,
    capacity    integer     not null
);
