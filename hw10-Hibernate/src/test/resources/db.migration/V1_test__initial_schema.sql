create table users(id bigint(20) not null, name varchar2(255), age int(3));
create table Phones(id bigint(20) not null, user_id bigint(20), phone_number varchar2(255));
create table Addresses(id bigint(20) not null, user_id bigint(20), street varchar2(255));
create sequence hibernate_sequence start with 1 increment by 1;

