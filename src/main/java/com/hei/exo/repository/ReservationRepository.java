package com.hei.exo.repository;

import com.hei.exo.model.Reservation;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
  boolean existsByProjectionIdAndSeat_Id(UUID projectionId, UUID seatId);
}
