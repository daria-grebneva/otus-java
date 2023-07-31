create table investors
(
    id   bigint not null primary key,
    name varchar(50)
);

create table stocks
(
    id   bigint not null primary key,
    investor_id bigint references investors (id),
    code varchar(50),
    price int
);