insert into address (street, id) values ('Sovetskaya 116', nextval('address_seq'));
insert into client (id, name, address_id) values (nextval('client_seq'), 'Daria', currval('address_seq'));
insert into phone (client_id, number, id) values (currval('client_seq'), '+79111234565', nextval('phone_seq'));

insert into address (street, id) values ('Sovetskaya 16', nextval('address_seq'));
insert into client (id, name, address_id) values (nextval('client_seq'), 'Maria', currval('address_seq'));
insert into phone (client_id, number, id) values (currval('client_seq'), '+79231234515', nextval('phone_seq'));