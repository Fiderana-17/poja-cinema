package com.hei.exo.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProjectionResponse(
    UUID id, UUID movieId, UUID roomId, Instant datetime, BigDecimal seatPrice) {}
