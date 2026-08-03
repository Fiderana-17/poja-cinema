package com.hei.exo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateSeatRequest(@NotBlank String number) {}
