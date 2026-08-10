package com.hei.exo.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProjectionModel(
    UUID id, UUID movieId, UUID roomId, Instant datetime, BigDecimal seatPrice) {}
