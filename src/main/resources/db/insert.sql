create table USERS (
                       id int generated always as identity primary key ,
                       name varchar (100) not null ,
                       password varchar (100) not null ,
                       email varchar (100) not null unique ,
                       date date not null ,
                       likes int
);

create table role (
                      id int generated always as identity primary key ,
                      name varchar (100) not null
);
create table user_role(
                          user_id int not null,
                          role int not null ,
                          constraint UK_USER_ROLE unique (USER_ID, ROLE),
                          constraint FK_USER_ROLE foreign key (USER_ID) references USERS (ID) on delete cascade
);
insert into user_role (user_id, role) VALUES (1,1);
insert into user_role (user_id, role) VALUES (2,1);
insert into user_role (user_id, role) VALUES (3,1);
insert into user_role (user_id, role) VALUES (4,1);
insert into user_role (user_id, role) VALUES (6,1);
insert into role (name) values ('ROLE_USER');
insert into role (name) values ('ROLE_ADMIN');

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
                         user_id int references  USERS (id) on delete  cascade
);

create table TAG(
                    id int generated always as identity primary key ,
                    tag varchar(100) not null ,
                    user_id int references USERS(id) on  delete cascade
);

alter table users add  column created_at timestamp,
                  add column updated_at timestamp,
                  add column created_who varchar;

alter table users add column gender varchar(10);
alter table users add column description varchar(500);
drop table contact;


insert into users (name, password, email,likes, date) VALUES ('Vlad','1111','vlad@mail.com',0,'1995-01-15');
insert into users (name, password, email,likes, date) VALUES ('Stas','1111','stas@mail.com',0,'1995-11-21');
insert into users (name, password, email,likes, date) VALUES ('Maliha','1111','maliha@mail.com',0,'1997-03-01');
insert into users (name, password, email,likes, date) VALUES ('Tanya','1111','tanya@mail.com',0,'1999-05-17');
insert into users (name, password, email,likes, date) VALUES ('Lili','1111','lili@mail.com',0,'2000-08-11');
insert into users (name, password, email,likes, date) VALUES ('Max','1111','max@mail.com',0,'1993-01-05');
insert into users (name, password, email,likes, date) VALUES ('Dasha','1111','dasha@mail.com',0,'2001-11-11');

insert into photo (file_link, date_time, user_id) values ('/photos/man/vlad/vlad1.jpg',CURRENT_TIMESTAMP,1);
insert into photo (file_link, date_time, user_id) values ('/photos/man/stas/stas1.webp',CURRENT_TIMESTAMP,2);
insert into photo (file_link, date_time, user_id) values ('/photos/woman/malina/malina1.webp',CURRENT_TIMESTAMP,3);
insert into photo (file_link, date_time, user_id) values ('/photos/woman/tanya/tanya1.webp',CURRENT_TIMESTAMP,4);
insert into photo (file_link, date_time, user_id) values ('/photos/woman/lili/lili1.webp',CURRENT_TIMESTAMP,6);
insert into photo (file_link, date_time, user_id) values ('/photos/man/max/max1.webp',CURRENT_TIMESTAMP,7);
insert into photo (file_link, date_time, user_id) values ('/photos/woman/dasha/dasha1.webp',CURRENT_TIMESTAMP,8);
insert into photo (file_link, date_time, user_id) values ('/photos/man/vlad/vlad2.jpg',CURRENT_TIMESTAMP,1);

insert into tag (tag, user_id) VALUES ('sport',1);
insert into tag (tag, user_id) VALUES ('moto',1);
insert into tag (tag, user_id) VALUES ('chicken',1);
insert into tag (tag, user_id) VALUES ('games',1);
insert into tag (tag, user_id) VALUES ('games',2);
insert into tag (tag, user_id) VALUES ('chat',2);
insert into tag (tag, user_id) VALUES ('chat',3);
insert into tag (tag, user_id) VALUES ('chat',4);

insert into tag (tag, user_id) VALUES ('chat',6);
insert into tag (tag, user_id) VALUES ('chat',7);
insert into tag (tag, user_id) VALUES ('chat',8);

insert into contact (code, value,user_id) VALUES ('telegram','razdolbye',1);
