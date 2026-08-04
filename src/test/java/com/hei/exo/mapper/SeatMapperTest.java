package com.hei.exo.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.hei.exo.dto.request.CreateSeatRequest;
import com.hei.exo.dto.response.SeatResponse;
import com.hei.exo.model.Room;
import com.hei.exo.model.Seat;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SeatMapperTest {

    private final SeatMapper mapper = new SeatMapper();

    @Test
    void toModel_shouldMapRequestToSeatWithRoom() {
        UUID roomId = UUID.randomUUID();
        Room room = new Room(roomId, "A1", 50);
        CreateSeatRequest request = new CreateSeatRequest("S1", roomId);

        Seat result = mapper.toModel(request, room);

        assertNull(result.getId());
        assertEquals("S1", result.getNumber());
        assertEquals(room, result.getRoom());
    }

    @Test
    void toResponse_shouldMapSeatToResponse() {
        UUID seatId = UUID.randomUUID();
        UUID roomId = UUID.randomUUID();
        Room room = new Room(roomId, "A1", 50);
        Seat seat = new Seat(seatId, "S1", room);

        SeatResponse result = mapper.toResponse(seat);

        assertEquals(seatId, result.id());
        assertEquals("S1", result.number());
        assertEquals(roomId, result.roomId());
    }
}