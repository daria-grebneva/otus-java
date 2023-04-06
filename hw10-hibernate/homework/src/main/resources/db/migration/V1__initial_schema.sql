-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)

create table address
(
    id   bigint not null primary key,
    street varchar(255)
);

create table phone
(
    id   bigint not null primary key,
    number varchar(50)
);

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence client_SEQ start with 1 increment by 1;

create table client
(
    id   bigint not null primary key,
    name varchar(50),
    address_id bigint not null,

    CONSTRAINT fk_address
          FOREIGN KEY(address_id)
    	  REFERENCES address(id)
);