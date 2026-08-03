package com.hei.exo.endpoint.rest.controller;

import com.hei.exo.dto.request.CreateUserRequest;
import com.hei.exo.dto.response.UserResponse;
import com.hei.exo.service.UserService;
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
public class UserController {

  private final UserService userService;

  @GetMapping("/users")
  public List<UserResponse> getAll() {
    return userService.getAll();
  }

  @GetMapping("/users/{id}")
  public UserResponse getById(@PathVariable UUID id) {
    return userService.getById(id);
  }

  @PostMapping("/users")
  public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
    return userService.create(request);
  }
}
