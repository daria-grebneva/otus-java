create table address
(
    id   bigserial not null primary key,
    street varchar(255)
);

create sequence client_SEQ start with 1 increment by 1;

create table client
(
    id   bigint not null primary key,
    name varchar(50),
    address_id bigserial,
    constraint fk_address
            foreign key(address_id)
                references address(id)
);

create table phone
(
    id   bigserial not null primary key,
    number varchar(25),
    client_id bigint,
    constraint fk_client
        foreign key(client_id)
            references client(id)
);
