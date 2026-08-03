package com.hei.exo.endpoint.rest.controller;

import com.hei.exo.dto.request.CreateReservationRequest;
import com.hei.exo.dto.response.ReservationResponse;
import com.hei.exo.service.ReservationService;
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
public class ReservationController {

  private final ReservationService reservationService;

  @GetMapping("/reservations")
  public List<ReservationResponse> getAll() {
    return reservationService.getAll();
  }

  @GetMapping("/reservations/{id}")
  public ReservationResponse getById(@PathVariable UUID id) {
    return reservationService.getById(id);
  }

  @PostMapping("/reservations")
  public ReservationResponse create(@Valid @RequestBody CreateReservationRequest request) {
    return reservationService.create(request);
  }
}
