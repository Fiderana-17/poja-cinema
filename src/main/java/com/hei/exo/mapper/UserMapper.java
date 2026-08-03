package com.hei.exo.mapper;

import com.hei.exo.dto.request.CreateUserRequest;
import com.hei.exo.dto.response.UserResponse;
import com.hei.exo.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public User toModel(CreateUserRequest request, String hashedPassword) {
    User user = new User();
    user.setFirstName(request.firstName());
    user.setLastName(request.lastName());
    user.setBirthdate(request.birthdate());
    user.setEmail(request.email());
    user.setPassword(hashedPassword);
    user.setPhone(request.phone());
    user.setRole(request.role());
    return user;
  }

  public UserResponse toResponse(User user) {
    return new UserResponse(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getBirthdate(),
        user.getEmail(),
        user.getPhone(),
        user.getRole());
  }
}
