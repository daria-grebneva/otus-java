-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

create table address
(
    id   bigserial not null primary key,
    street varchar(255)
);

create table phone
(
    id   bigserial not null primary key,
    number varchar(50)
);


drop table if exists client cascade;

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
--create sequence client_SEQ start with 1 increment by 1;

create table client
(
    id   bigint not null primary key,
    name varchar(50),
    address_id bigint
--    client_phone phone ARRAY
);
--
--alter table if exists client
--add constraint client_pkey_t
--       foreign key (address_id)
--       references address;