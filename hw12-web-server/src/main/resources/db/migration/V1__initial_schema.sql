create sequence address_SEQ start with 1 increment by 1;

create table address
(
    id   bigint not null primary key,
    street varchar(255)
);

create sequence client_SEQ start with 1 increment by 1;

create table client
(
    id   bigint not null primary key,
    name varchar(50),
    address_id bigint,
    constraint fk_address
            foreign key(address_id)
                references address(id)
);

create sequence phone_SEQ start with 1 increment by 1;

create table phone
(
    id   bigint not null primary key,
    number varchar(25),
    client_id bigint,
    constraint fk_client
        foreign key(client_id)
            references client(id)
);