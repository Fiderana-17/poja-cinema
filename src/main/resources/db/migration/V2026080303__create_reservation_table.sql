CREATE TABLE reservation (
                             id UUID PRIMARY KEY,
                             user_id UUID NOT NULL,
                             seat_id UUID NOT NULL,
                             projection_id UUID NOT NULL,
                             status VARCHAR(50) NOT NULL,

                             CONSTRAINT fk_reservation_user
                                 FOREIGN KEY (user_id)
                                     REFERENCES "user"(id),

                             CONSTRAINT fk_reservation_seat
                                 FOREIGN KEY (seat_id)
                                     REFERENCES seat(id),

                             CONSTRAINT uq_reservation_projection_seat
                                 UNIQUE (projection_id, seat_id)
);