package com.hei.exo.service;

import com.hei.exo.dto.request.LoginRequest;
import com.hei.exo.dto.response.AuthResponse;
import com.hei.exo.exception.BadRequestException;
import com.hei.exo.repository.UserRepository;
import com.hei.exo.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthResponse login(LoginRequest request) {
    var user =
        userRepository
            .findByEmail(request.email())
            .orElseThrow(() -> new BadRequestException("Invalid credentials"));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new BadRequestException("Invalid credentials");
    }

    String accessToken = jwtService.generateToken(user);

    return new AuthResponse(accessToken, "Bearer", user.getId(), user.getRole());
  }
}
