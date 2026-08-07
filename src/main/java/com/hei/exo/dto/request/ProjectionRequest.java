package com.hei.exo.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProjectionRequest(
    @NotNull UUID movieId,
    @NotNull UUID roomId,
    @NotNull Instant datetime,
    @NotNull @DecimalMin("0.0") BigDecimal seatPrice) {}
