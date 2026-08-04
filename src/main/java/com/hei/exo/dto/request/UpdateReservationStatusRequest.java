package com.hei.exo.dto.request;

import com.hei.exo.model.ReservationStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateReservationStatusRequest(@NotNull ReservationStatus status) {}
