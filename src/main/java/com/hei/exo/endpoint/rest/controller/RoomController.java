package com.hei.exo.endpoint.rest.controller;

import com.hei.exo.dto.request.RoomRequest;
import com.hei.exo.dto.response.RoomResponse;
import com.hei.exo.model.RoomModel;
import com.hei.exo.service.RoomService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RoomController {

  private final RoomService service;

  @GetMapping("/rooms")
  public List<RoomResponse> getAll() {
    return service.getAll().stream().map(this::toResponse).toList();
  }

  @GetMapping("/rooms/{id}")
  public RoomResponse getById(@PathVariable UUID id) {
    RoomModel model = service.getById(id);
    if (model == null) {
      throw new RuntimeException("Room not found with id: " + id);
    }
    return toResponse(model);
  }

  @PostMapping("/rooms")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('MANAGER')")
  public RoomResponse create(@Valid @RequestBody RoomRequest request) {
    RoomModel model = new RoomModel(null, request.number(), request.capacity());
    return toResponse(service.save(model));
  }

  @PutMapping("/rooms/{id}")
  @PreAuthorize("hasRole('MANAGER')")
  public RoomResponse update(@PathVariable UUID id, @Valid @RequestBody RoomRequest request) {
    RoomModel model = new RoomModel(id, request.number(), request.capacity());
    return toResponse(service.update(id, model));
  }

  private RoomResponse toResponse(RoomModel model) {
    return new RoomResponse(model.id(), model.number(), model.capacity());
  }
}
