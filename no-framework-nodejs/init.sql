create table lecture
(
    id serial not null
        constraint pk_lecture_id
            primary key,
    created_at timestamp with time zone default now() not null,
    updated_at timestamp with time zone default now() not null,
    deleted_at timestamp with time zone,
    name varchar(500) not null,
    description text not null,
    category varchar(500) not null,
    price int not null,
    student_count int not null,
    publish_status varchar(50) not null,
    instructor_id int not null
);

alter table lecture owner to test;


create table student_lecture_map
(
    id serial not null
        constraint pk_student_lecture_map_id
            primary key,
    created_at timestamp with time zone default now() not null,
    updated_at timestamp with time zone default now() not null,
    deleted_at timestamp with time zone,
    student_id int not null,
    lecture_id int not null
);

alter table student_lecture_map owner to test;

create table student
(
    id serial not null
        constraint pk_student_id
            primary key,
    created_at timestamp with time zone default now() not null,
    updated_at timestamp with time zone default now() not null,
    deleted_at timestamp with time zone,
    name varchar(500) not null,
    email varchar(500) not null
);

alter table student owner to test;


insert into lecture(name, description, category, price, student_count, publish_status, instructor_id)
values ('test', 'test강좌', 'WEB', 1000, 10, 'PUBLIC', 1);

insert into student (name, email) values ('jojoldu', 'jojoldu@inflearn.com');
