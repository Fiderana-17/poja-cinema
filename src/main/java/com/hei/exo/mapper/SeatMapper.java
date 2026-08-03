package com.hei.exo.mapper;

import com.hei.exo.dto.request.CreateSeatRequest;
import com.hei.exo.dto.response.SeatResponse;
import com.hei.exo.model.Room;
import com.hei.exo.model.Seat;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {

  public Seat toModel(CreateSeatRequest request, Room room) {
    Seat seat = new Seat();
    seat.setNumber(request.number());
    seat.setRoom(room);
    return seat;
  }

  public SeatResponse toResponse(Seat seat) {
    return new SeatResponse(seat.getId(), seat.getNumber(), seat.getRoom().getId());
  }
}
