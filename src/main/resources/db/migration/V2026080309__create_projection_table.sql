create table if not exists "projection" (
    id         uuid           not null primary key,
    movie_id   uuid           not null,
    room_id    uuid           not null,
    datetime   timestamptz    not null,
    seat_price numeric(10, 2) not null,
    constraint fk_projection_movie
        foreign key (movie_id)
            references movie(id),
    constraint fk_projection_room
        foreign key (room_id)
            references room(id)
);
