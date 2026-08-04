package com.hei.exo.service;

import com.hei.exo.dto.request.CreateReservationRequest;
import com.hei.exo.dto.request.UpdateReservationStatusRequest;
import com.hei.exo.dto.response.ReservationResponse;
import com.hei.exo.exception.ConflictException;
import com.hei.exo.exception.ForbiddenException;
import com.hei.exo.exception.NotFoundException;
import com.hei.exo.mapper.ReservationMapper;
import com.hei.exo.model.Reservation;
import com.hei.exo.model.ReservationStatus;
import com.hei.exo.model.User;
import com.hei.exo.model.UserRole;
import com.hei.exo.repository.ReservationRepository;
import com.hei.exo.repository.SeatRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final SeatRepository seatRepository;
  private final ReservationMapper reservationMapper;

  public List<ReservationResponse> getAll() {
    return reservationRepository.findAll().stream().map(reservationMapper::toResponse).toList();
  }

  public ReservationResponse getById(UUID id) {
    return reservationMapper.toResponse(findReservationById(id));
  }

  public ReservationResponse getByIdForCurrentUser(UUID id, User currentUser) {
    Reservation reservation = findReservationById(id);
    authorizeReservationRead(reservation, currentUser);
    return reservationMapper.toResponse(reservation);
  }

  public ReservationResponse create(CreateReservationRequest request, User currentUser) {
    var seat =
        seatRepository
            .findById(request.seatId())
            .orElseThrow(() -> new NotFoundException("Seat not found"));

    if (reservationRepository.existsByProjectionIdAndSeat_Id(
        request.projectionId(), request.seatId())) {
      throw new ConflictException("Seat already reserved for this projection");
    }

    var reservation = reservationMapper.toModel(request, currentUser, seat);
    var savedReservation = reservationRepository.save(reservation);

    return reservationMapper.toResponse(savedReservation);
  }

  public ReservationResponse updateStatus(
      UUID id, UpdateReservationStatusRequest request, User currentUser) {
    Reservation reservation = findReservationById(id);

    authorizeReservationUpdate(reservation, request.status(), currentUser);

    reservation.setStatus(request.status());
    Reservation savedReservation = reservationRepository.save(reservation);

    return reservationMapper.toResponse(savedReservation);
  }

  private Reservation findReservationById(UUID id) {
    return reservationRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Reservation not found"));
  }

  private void authorizeReservationRead(Reservation reservation, User currentUser) {
    if (currentUser.getRole() == UserRole.CLIENT && !isOwner(reservation, currentUser)) {
      throw new ForbiddenException("Cannot access another user's reservation");
    }
  }

  private void authorizeReservationUpdate(
      Reservation reservation, ReservationStatus requestedStatus, User currentUser) {
    UserRole role = currentUser.getRole();

    if (role == UserRole.CLIENT) {
      if (!isOwner(reservation, currentUser)) {
        throw new ForbiddenException("Cannot update another user's reservation");
      }

      if (requestedStatus != ReservationStatus.CANCELED) {
        throw new ForbiddenException("Client can only cancel a reservation");
      }
    }

    if (role == UserRole.MANAGER && requestedStatus == ReservationStatus.SUCCESS) {
      throw new ForbiddenException("Only employees can validate a reservation");
    }
  }

  private boolean isOwner(Reservation reservation, User currentUser) {
    return reservation.getUser().getId().equals(currentUser.getId());
  }
}
