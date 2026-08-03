package com.hei.exo.mapper;

import com.hei.exo.dto.request.CreateReservationRequest;
import com.hei.exo.dto.response.ReservationResponse;
import com.hei.exo.model.Reservation;
import com.hei.exo.model.ReservationStatus;
import com.hei.exo.model.Seat;
import com.hei.exo.model.User;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

  public Reservation toModel(CreateReservationRequest request, User user, Seat seat) {
    Reservation reservation = new Reservation();
    reservation.setUser(user);
    reservation.setSeat(seat);
    reservation.setProjectionId(request.projectionId());
    reservation.setStatus(ReservationStatus.PENDING);
    return reservation;
  }

  public ReservationResponse toResponse(Reservation reservation) {
    return new ReservationResponse(
        reservation.getId(),
        reservation.getUser().getId(),
        reservation.getSeat().getId(),
        reservation.getProjectionId(),
        reservation.getStatus());
  }
}
