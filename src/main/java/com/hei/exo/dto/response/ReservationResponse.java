package com.hei.exo.dto.response;

import com.hei.exo.model.ReservationStatus;
import java.time.Instant;
import java.util.UUID;

public record ReservationResponse(
    UUID id,
    UUID userId,
    UUID seatId,
    UUID projectionId,
    Instant createdAt,
    ReservationStatus status) {}
