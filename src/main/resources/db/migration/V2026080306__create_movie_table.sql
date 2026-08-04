create table if not exists movie (
    id          uuid         not null primary key,
    title       varchar(255) not null,
    description text         not null,
    duration    bigint       not null
);

create table if not exists movie_genres (
    movie_id uuid        not null,
    genre    varchar(30) not null,
    primary key (movie_id, genre),
    constraint fk_movie_genres_movie
        foreign key (movie_id)
            references movie(id)
);
