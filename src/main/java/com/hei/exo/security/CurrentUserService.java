package com.hei.exo.security;

import com.hei.exo.exception.ForbiddenException;
import com.hei.exo.exception.NotFoundException;
import com.hei.exo.model.User;
import com.hei.exo.repository.UserRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CurrentUserService {

  private final UserRepository userRepository;

  public User getAuthenticatedUser(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new ForbiddenException("Authentication required");
    }

    if (!(authentication.getPrincipal() instanceof Jwt jwt)) {
      throw new ForbiddenException("Invalid authenticated user");
    }

    UUID userId = UUID.fromString(jwt.getClaimAsString("userId"));

    return userRepository
        .findById(userId)
        .orElseThrow(() -> new NotFoundException("Authenticated user not found"));
  }
}
