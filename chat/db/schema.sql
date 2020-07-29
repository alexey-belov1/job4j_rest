drop table message;
drop table person_room;
drop table room;
drop table person;
drop table role;

create table role (
    id serial primary key,
    name varchar(200) not null
);

create table person (
    id serial primary key,
    name varchar(200) not null,
    password varchar(200) not null,
    role_id int references role(id) on delete set null
);

create table room (
    id serial primary key,
    name varchar(200) not null
);

create table person_room (
    person_id int references person(id) not null,
    room_id int references room(id) not null,
    unique (person_id, room_id)
);

create table message (
    id serial primary key,
    text text not null,
    person_id int references person(id) not null,
    room_id int references room(id) not null
);