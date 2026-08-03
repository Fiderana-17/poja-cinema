package com.hei.exo.service;

import com.hei.exo.dto.request.CreateUserRequest;
import com.hei.exo.dto.response.UserResponse;
import com.hei.exo.mapper.UserMapper;
import com.hei.exo.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  public List<UserResponse> getAll() {
    return userRepository.findAll().stream().map(userMapper::toResponse).toList();
  }

  public UserResponse getById(UUID id) {
    return userRepository
        .findById(id)
        .map(userMapper::toResponse)
        .orElseThrow(() -> new RuntimeException("User not found"));
  }

  public UserResponse create(CreateUserRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new RuntimeException("Email already exists");
    }

    String passwordHash = passwordEncoder.encode(request.password());
    var user = userMapper.toModel(request, passwordHash);
    var savedUser = userRepository.save(user);

    return userMapper.toResponse(savedUser);
  }
}
