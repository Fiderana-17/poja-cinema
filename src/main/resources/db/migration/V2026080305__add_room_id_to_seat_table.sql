ALTER TABLE seat
    ADD COLUMN room_id UUID NOT NULL;

ALTER TABLE seat
    ADD CONSTRAINT fk_seat_room
        FOREIGN KEY (room_id)
            REFERENCES room(id)
            ON DELETE CASCADE;