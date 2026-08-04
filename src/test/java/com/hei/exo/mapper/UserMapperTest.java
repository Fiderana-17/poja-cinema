package com.hei.exo.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.hei.exo.dto.request.CreateUserRequest;
import com.hei.exo.dto.response.UserResponse;
import com.hei.exo.model.User;
import com.hei.exo.model.UserRole;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserMapperTest {

  private final UserMapper mapper = new UserMapper();

  @Test
  void toModel_shouldMapRequestToUserWithHashedPassword() {
    CreateUserRequest request =
        new CreateUserRequest(
            "John",
            "Doe",
            LocalDate.of(2000, 1, 1),
            "john.doe@example.com",
            "plain-password",
            "+261340000000",
            UserRole.CLIENT);

    User result = mapper.toModel(request, "hashed-password");

    assertNull(result.getId());
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals(LocalDate.of(2000, 1, 1), result.getBirthdate());
    assertEquals("john.doe@example.com", result.getEmail());
    assertEquals("hashed-password", result.getPassword());
    assertEquals("+261340000000", result.getPhone());
    assertEquals(UserRole.CLIENT, result.getRole());
  }

  @Test
  void toResponse_shouldMapUserWithoutPassword() {
    UUID id = UUID.randomUUID();
    User user =
        new User(
            id,
            "John",
            "Doe",
            LocalDate.of(2000, 1, 1),
            "john.doe@example.com",
            "hashed-password",
            "+261340000000",
            UserRole.CLIENT);

    UserResponse result = mapper.toResponse(user);

    assertEquals(id, result.id());
    assertEquals("John", result.firstName());
    assertEquals("Doe", result.lastName());
    assertEquals(LocalDate.of(2000, 1, 1), result.birthdate());
    assertEquals("john.doe@example.com", result.email());
    assertEquals("+261340000000", result.phone());
    assertEquals(UserRole.CLIENT, result.role());
  }
}
