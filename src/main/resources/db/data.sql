create table USERS (
                       id int generated always as identity primary key ,
                       name varchar (100) not null ,
                       password varchar (100) not null ,
                       email varchar (100) not null unique ,
                       date date not null ,
                       likes int
);

create table PHOTO (
                       id int generated always as identity primary key ,
                       file_link varchar(2048) not null ,
                       date_time timestamp not null ,
                       likes int ,
                       user_id int references USERS(id) on delete cascade
);

create table CONTACT (
                         id int generated always as identity primary key ,
                         code varchar(32) not null ,
                         "value" varchar(256) not null ,
                         foreign key (id) references USERS (id) on delete  cascade
);

create table TAG(
                    id int generated always as identity primary key ,
                    tag varchar(100) not null ,
                    user_id int references USERS(id) on  delete cascade
);

alter table users add  column created_at timestamp,
                  add column updated_at timestamp,
                  add column created_who varchar;

alter table users add  column gender varchar(10)