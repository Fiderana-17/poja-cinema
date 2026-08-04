package com.hei.exo.endpoint.rest.controller;

import com.hei.exo.dto.request.CreateReservationRequest;
import com.hei.exo.dto.request.UpdateReservationStatusRequest;
import com.hei.exo.dto.response.ReservationResponse;
import com.hei.exo.security.CurrentUserService;
import com.hei.exo.service.ReservationService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ReservationController {

  private final ReservationService reservationService;
  private final CurrentUserService currentUserService;

  @GetMapping("/reservations")
  @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER')")
  public List<ReservationResponse> getAll() {
    return reservationService.getAll();
  }

  @GetMapping("/reservations/{id}")
  @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE', 'MANAGER')")
  public ReservationResponse getById(@PathVariable UUID id, Authentication authentication) {
    var currentUser = currentUserService.getAuthenticatedUser(authentication);
    return reservationService.getByIdForCurrentUser(id, currentUser);
  }

  @PostMapping("/reservations")
  @PreAuthorize("hasRole('CLIENT')")
  public ReservationResponse create(
      @Valid @RequestBody CreateReservationRequest request, Authentication authentication) {
    var currentUser = currentUserService.getAuthenticatedUser(authentication);
    return reservationService.create(request, currentUser);
  }

  @PutMapping("/reservations/{id}")
  @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE', 'MANAGER')")
  public ReservationResponse updateStatus(
      @PathVariable UUID id,
      @Valid @RequestBody UpdateReservationStatusRequest request,
      Authentication authentication) {
    var currentUser = currentUserService.getAuthenticatedUser(authentication);
    return reservationService.updateStatus(id, request, currentUser);
  }
}
