create table shader_demo (
    id int not null generated always as identity,
    title varchar(50) not null unique,
    creator int not null,
    date_created date not null,
    code varchar(20000) not null,
    availability int not null
);
alter table shader_demo add constraint shader_demo_pk primary key (id);

create table account (
    id int not null generated always as identity,
    auth_detail int not null unique,
    username varchar(20) not null unique,
    date_created date not null
);
alter table account add constraint account_pk primary key (id);
alter table shader_demo add constraint shader_demo2account_fk foreign key (creator) references account (id) on delete cascade;

create table auth_detail (
    id int not null generated always as identity,
    hashcode char(20) not null,
    salt char(10) not null
);
alter table auth_detail add constraint auth_detail_pk primary key (id);
alter table account add constraint account2auth_detail_fk foreign key (auth_detail) references auth_detail (id)
    on delete cascade;

create table visit (
    account int not null,
    shader_demo int not null
);
alter table visit add constraint visit_pk primary key (account, shader_demo);
alter table visit add constraint visit2account_fk foreign key (account) references account (id) on delete cascade;
alter table visit add constraint visit2shader_demo_fk foreign key (shader_demo) references shader_demo (id) on delete cascade;

create table delightful (
    account int not null,
    shader_demo int not null
);
alter table delightful add constraint delightful_pk primary key (account, shader_demo);
alter table delightful add constraint delightful2account_fk foreign key (account) references account (id) on delete cascade;
alter table delightful add constraint delightful2shader_demo_fk foreign key (shader_demo) references shader_demo (id) on delete cascade;

create table tag (
    shader_demo int not null,
    tag_name varchar(30) not null
);
alter table tag add constraint tag_pk primary key (shader_demo, tag_name);
alter table tag add constraint tag2shader_demo_fk foreign key (shader_demo) references shader_demo(id)
    on delete cascade;