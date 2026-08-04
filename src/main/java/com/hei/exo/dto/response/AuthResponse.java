package com.hei.exo.dto.response;

import com.hei.exo.model.UserRole;
import java.util.UUID;

public record AuthResponse(String accessToken, String tokenType, UUID userId, UserRole role) {}
