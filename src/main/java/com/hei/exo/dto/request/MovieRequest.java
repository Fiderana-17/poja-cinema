package com.hei.exo.dto.request;

import com.hei.exo.model.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;

public record MovieRequest(
    @NotBlank String title,
    @NotEmpty Set<Genre> genres,
    @NotBlank String description,
    @NotNull Duration duration) {}
