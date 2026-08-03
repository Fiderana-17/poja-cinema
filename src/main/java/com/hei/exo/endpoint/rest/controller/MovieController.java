package com.hei.exo.endpoint.rest.controller;

import com.hei.exo.dto.request.MovieRequest;
import com.hei.exo.dto.response.MovieResponse;
import com.hei.exo.model.MovieModel;
import com.hei.exo.service.MovieService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MovieController {

  private final MovieService service;

  @GetMapping("/movies")
  public List<MovieResponse> getAll() {
    return service.getAll().stream().map(this::toResponse).toList();
  }

  @GetMapping("/movies/{id}")
  public MovieResponse getById(@PathVariable UUID id) {
    return toResponse(service.getById(id));
  }

  @PutMapping("/movies/{id}")
  @PreAuthorize("hasRole('MANAGER')")
  public MovieResponse update(@PathVariable UUID id, @Valid @RequestBody MovieRequest request) {
    MovieModel model =
        new MovieModel(
            id, request.title(), request.genres(), request.description(), request.duration());
    return toResponse(service.update(id, model));
  }

  private MovieResponse toResponse(MovieModel model) {
    return new MovieResponse(
        model.id(), model.title(), model.genres(), model.description(), model.duration());
  }
}
