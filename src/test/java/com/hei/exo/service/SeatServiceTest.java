package com.hei.exo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.hei.exo.dto.request.CreateSeatRequest;
import com.hei.exo.dto.response.SeatResponse;
import com.hei.exo.exception.NotFoundException;
import com.hei.exo.mapper.SeatMapper;
import com.hei.exo.model.Room;
import com.hei.exo.model.Seat;
import com.hei.exo.repository.RoomRepository;
import com.hei.exo.repository.SeatRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock private SeatRepository seatRepository;
    @Mock private RoomRepository roomRepository;
    @Mock private SeatMapper seatMapper;

    private SeatService service;
    private UUID seatId;
    private UUID roomId;
    private Room room;
    private Seat seat;
    private SeatResponse response;

    @BeforeEach
    void setUp() {
        service = new SeatService(seatRepository, roomRepository, seatMapper);
        seatId = UUID.randomUUID();
        roomId = UUID.randomUUID();
        room = new Room(roomId, "A1", 50);
        seat = new Seat(seatId, "S1", room);
        response = new SeatResponse(seatId, "S1", roomId);
    }

    @Test
    void getAll_shouldReturnAllSeats() {
        when(seatRepository.findAll()).thenReturn(List.of(seat));
        when(seatMapper.toResponse(seat)).thenReturn(response);

        List<SeatResponse> result = service.getAll();

        assertEquals(1, result.size());
        assertEquals(response, result.getFirst());
    }

    @Test
    void getById_shouldReturnSeatWhenFound() {
        when(seatRepository.findById(seatId)).thenReturn(Optional.of(seat));
        when(seatMapper.toResponse(seat)).thenReturn(response);

        SeatResponse result = service.getById(seatId);

        assertEquals(response, result);
    }

    @Test
    void getById_shouldThrowWhenSeatDoesNotExist() {
        when(seatRepository.findById(seatId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getById(seatId));
    }

    @Test
    void create_shouldCreateSeatWhenRoomExists() {
        CreateSeatRequest request = new CreateSeatRequest("S1", roomId);

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(seatMapper.toModel(request, room)).thenReturn(seat);
        when(seatRepository.save(seat)).thenReturn(seat);
        when(seatMapper.toResponse(seat)).thenReturn(response);

        SeatResponse result = service.create(request);

        assertEquals(response, result);
        verify(seatRepository).save(seat);
    }

    @Test
    void create_shouldThrowWhenRoomDoesNotExist() {
        CreateSeatRequest request = new CreateSeatRequest("S1", roomId);

        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.create(request));
        verify(seatRepository, never()).save(any());
    }
}