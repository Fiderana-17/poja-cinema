package com.hei.exo.dto.response;

import com.hei.exo.model.Genre;
import java.time.Duration;
import java.util.Set;
import java.util.UUID;

public record MovieResponse(
    UUID id, String title, Set<Genre> genres, String description, Duration duration) {}
