package com.hei.exo.service;

import com.hei.exo.dto.request.CreateReservationRequest;
import com.hei.exo.dto.response.ReservationResponse;
import com.hei.exo.exception.ConflictException;
import com.hei.exo.exception.NotFoundException;
import com.hei.exo.mapper.ReservationMapper;
import com.hei.exo.repository.ReservationRepository;
import com.hei.exo.repository.SeatRepository;
import com.hei.exo.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final UserRepository userRepository;
  private final SeatRepository seatRepository;
  private final ReservationMapper reservationMapper;

  public List<ReservationResponse> getAll() {
    return reservationRepository.findAll().stream().map(reservationMapper::toResponse).toList();
  }

  public ReservationResponse getById(UUID id) {
    return reservationRepository
        .findById(id)
        .map(reservationMapper::toResponse)
        .orElseThrow(() -> new NotFoundException("Reservation not found"));
  }

  public ReservationResponse create(CreateReservationRequest request) {
    var user =
        userRepository
            .findById(request.userId())
            .orElseThrow(() -> new NotFoundException("User not found"));

    var seat =
        seatRepository
            .findById(request.seatId())
            .orElseThrow(() -> new NotFoundException("Seat not found"));

    if (reservationRepository.existsByProjectionIdAndSeat_Id(
        request.projectionId(), request.seatId())) {
      throw new ConflictException("Seat already reserved for this projection");
    }

    var reservation = reservationMapper.toModel(request, user, seat);
    var savedReservation = reservationRepository.save(reservation);

    return reservationMapper.toResponse(savedReservation);
  }
}
