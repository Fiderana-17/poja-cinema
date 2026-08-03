package com.hei.exo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateSeatRequest(@NotBlank String number, @NotNull UUID roomId) {}
