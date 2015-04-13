-- drop table anime;
-- drop table genre;


create table genre (
    id int not null generated always as identity (start with 1, increment by 1),
    name varchar(50) not null unique,
    description varchar(250) not null,
    constraint pk_genre_id primary key (id)
);

create table anime (
    id int not null generated always as identity (start with 1, increment by 1),
    title varchar(50) not null,
    aired date not null,
    dubbed boolean not null,
    rating real not null,
    genre_id int not null,
    constraint pk_anime_id primary key (id),
    constraint fk_anime2genre_id foreign key (genre_id) references genre (id) on delete cascade
);
