package com.hei.exo.dto.response;

import com.hei.exo.model.UserRole;
import java.time.LocalDate;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String firstName,
    String lastName,
    LocalDate birthdate,
    String email,
    String phone,
    UserRole role) {}
