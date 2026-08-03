package com.hei.exo.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateReservationRequest(
    @NotNull UUID userId, @NotNull UUID seatId, @NotNull UUID projectionId) {}
