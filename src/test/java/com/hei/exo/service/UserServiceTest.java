package com.hei.exo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.hei.exo.dto.request.CreateUserRequest;
import com.hei.exo.dto.response.UserResponse;
import com.hei.exo.exception.ConflictException;
import com.hei.exo.exception.NotFoundException;
import com.hei.exo.mapper.UserMapper;
import com.hei.exo.model.User;
import com.hei.exo.model.UserRole;
import com.hei.exo.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private UserMapper userMapper;
  @Mock private PasswordEncoder passwordEncoder;

  private UserService service;
  private UUID userId;
  private User user;
  private UserResponse response;

  @BeforeEach
  void setUp() {
    service = new UserService(userRepository, userMapper, passwordEncoder);
    userId = UUID.randomUUID();
    user =
        new User(
            userId,
            "John",
            "Doe",
            LocalDate.of(2000, 1, 1),
            "john.doe@example.com",
            "hashed-password",
            "+261340000000",
            UserRole.CLIENT);
    response =
        new UserResponse(
            userId,
            "John",
            "Doe",
            LocalDate.of(2000, 1, 1),
            "john.doe@example.com",
            "+261340000000",
            UserRole.CLIENT);
  }

  @Test
  void getAll_shouldReturnAllUsers() {
    when(userRepository.findAll()).thenReturn(List.of(user));
    when(userMapper.toResponse(user)).thenReturn(response);

    List<UserResponse> result = service.getAll();

    assertEquals(1, result.size());
    assertEquals(response, result.getFirst());
  }

  @Test
  void getById_shouldReturnUserWhenFound() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userMapper.toResponse(user)).thenReturn(response);

    UserResponse result = service.getById(userId);

    assertEquals(response, result);
  }

  @Test
  void getById_shouldThrowWhenUserDoesNotExist() {
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> service.getById(userId));
  }

  @Test
  void create_shouldCreateUserWithHashedPassword() {
    CreateUserRequest request =
        new CreateUserRequest(
            "John",
            "Doe",
            LocalDate.of(2000, 1, 1),
            "john.doe@example.com",
            "plain-password",
            "+261340000000",
            UserRole.CLIENT);

    when(userRepository.existsByEmail(request.email())).thenReturn(false);
    when(passwordEncoder.encode(request.password())).thenReturn("hashed-password");
    when(userMapper.toModel(request, "hashed-password")).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toResponse(user)).thenReturn(response);

    UserResponse result = service.create(request);

    assertEquals(response, result);
    verify(passwordEncoder).encode("plain-password");
    verify(userRepository).save(user);
  }

  @Test
  void create_shouldThrowWhenEmailAlreadyExists() {
    CreateUserRequest request =
        new CreateUserRequest(
            "John",
            "Doe",
            LocalDate.of(2000, 1, 1),
            "john.doe@example.com",
            "plain-password",
            "+261340000000",
            UserRole.CLIENT);

    when(userRepository.existsByEmail(request.email())).thenReturn(true);

    assertThrows(ConflictException.class, () -> service.create(request));
    verify(userRepository, never()).save(any());
  }
}
