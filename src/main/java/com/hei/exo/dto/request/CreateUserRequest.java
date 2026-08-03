package com.hei.exo.dto.request;

import com.hei.exo.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateUserRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotNull LocalDate birthdate,
    @Email @NotBlank String email,
    @NotBlank String password,
    @NotBlank String phone,
    @NotNull UserRole role) {}
