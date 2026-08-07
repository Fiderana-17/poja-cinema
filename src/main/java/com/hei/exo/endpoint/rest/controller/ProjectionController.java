package com.hei.exo.endpoint.rest.controller;

import com.hei.exo.dto.request.ProjectionRequest;
import com.hei.exo.dto.response.ProjectionResponse;
import com.hei.exo.model.ProjectionModel;
import com.hei.exo.service.ProjectionService;
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
public class ProjectionController {

  private final ProjectionService service;

  @GetMapping("/projections")
  public List<ProjectionResponse> getAll() {
    return service.getAll().stream().map(this::toResponse).toList();
  }

  @GetMapping("/projections/{id}")
  public ProjectionResponse getById(@PathVariable UUID id) {
    return toResponse(service.getById(id));
  }

  @PutMapping("/projections/{id}")
  @PreAuthorize("hasRole('MANAGER')")
  public ProjectionResponse update(
      @PathVariable UUID id, @Valid @RequestBody ProjectionRequest request) {
    ProjectionModel model =
        new ProjectionModel(
            id, request.movieId(), request.roomId(), request.datetime(), request.seatPrice());
    return toResponse(service.update(id, model));
  }

  private ProjectionResponse toResponse(ProjectionModel model) {
    return new ProjectionResponse(
        model.id(), model.movieId(), model.roomId(), model.datetime(), model.seatPrice());
  }
}
