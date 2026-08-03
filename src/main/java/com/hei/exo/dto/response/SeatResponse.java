package com.hei.exo.dto.response;

import java.util.UUID;

public record SeatResponse(UUID id, String number, UUID roomId) {}
