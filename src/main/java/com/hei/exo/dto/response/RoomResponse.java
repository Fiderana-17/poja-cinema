package com.hei.exo.dto.response;

import java.util.UUID;

public record RoomResponse(UUID id, String number, int capacity) {}
