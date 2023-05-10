create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

create table address
(
    id   bigserial not null primary key,
    street varchar(255),
    client_id bigserial not null references client(id)
);

create table phone
(
    id   bigserial not null primary key,
    number varchar(25),
    client_id bigserial not null references client(id)
);