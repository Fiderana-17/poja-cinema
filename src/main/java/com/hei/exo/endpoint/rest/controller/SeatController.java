package com.hei.exo.endpoint.rest.controller;

import com.hei.exo.dto.request.CreateSeatRequest;
import com.hei.exo.dto.response.SeatResponse;
import com.hei.exo.service.SeatService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SeatController {

  private final SeatService seatService;

  @GetMapping("/seats")
  public List<SeatResponse> getAll() {
    return seatService.getAll();
  }

  @GetMapping("/seats/{id}")
  public SeatResponse getById(@PathVariable UUID id) {
    return seatService.getById(id);
  }

  @PostMapping("/seats")
  public SeatResponse create(@Valid @RequestBody CreateSeatRequest request) {
    return seatService.create(request);
  }
}
