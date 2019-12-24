--liquibase formatted sql
--changeset salerno:7
insert into UserData (username, password) values ('vaibhav','123456');
insert into UserData ( username, password) values ('naveen','123456');
