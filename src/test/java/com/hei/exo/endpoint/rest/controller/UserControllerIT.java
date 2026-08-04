package com.hei.exo.endpoint.rest.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hei.exo.conf.FacadeIT;
import com.hei.exo.dto.request.CreateUserRequest;
import com.hei.exo.model.UserRole;
import com.hei.exo.repository.ReservationRepository;
import com.hei.exo.repository.UserRepository;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
class UserControllerIT extends FacadeIT {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private UserRepository userRepository;
  @Autowired private ReservationRepository reservationRepository;

  @BeforeEach
  void cleanUpUsers() {
    reservationRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void getUsers_shouldReturn200AndEmptyList() throws Exception {
    mockMvc.perform(get("/users")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  void createUser_shouldReturn200AndHidePassword() throws Exception {
    CreateUserRequest request =
        new CreateUserRequest(
            "John",
            "Doe",
            LocalDate.of(2000, 1, 1),
            "john.doe@example.com",
            "plain-password",
            "+261340000000",
            UserRole.CLIENT);

    mockMvc
        .perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.firstName").value("John"))
        .andExpect(jsonPath("$.lastName").value("Doe"))
        .andExpect(jsonPath("$.birthdate").value("2000-01-01"))
        .andExpect(jsonPath("$.email").value("john.doe@example.com"))
        .andExpect(jsonPath("$.phone").value("+261340000000"))
        .andExpect(jsonPath("$.role").value("CLIENT"))
        .andExpect(jsonPath("$.password").doesNotExist());
  }

  @Test
  void getUserById_shouldReturn200WhenUserExists() throws Exception {
    CreateUserRequest request =
        new CreateUserRequest(
            "Jane",
            "Doe",
            LocalDate.of(1999, 5, 10),
            "jane.doe@example.com",
            "plain-password",
            "+261341111111",
            UserRole.EMPLOYEE);

    MvcResult createResult =
        mockMvc
            .perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andReturn();

    UUID userId =
        UUID.fromString(
            objectMapper
                .readTree(createResult.getResponse().getContentAsString())
                .get("id")
                .asText());

    mockMvc
        .perform(get("/users/{id}", userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.firstName").value("Jane"))
        .andExpect(jsonPath("$.lastName").value("Doe"))
        .andExpect(jsonPath("$.email").value("jane.doe@example.com"))
        .andExpect(jsonPath("$.role").value("EMPLOYEE"))
        .andExpect(jsonPath("$.password").doesNotExist());
  }

  @Test
  void getUserById_shouldReturn404WhenUserDoesNotExist() throws Exception {
    mockMvc.perform(get("/users/{id}", UUID.randomUUID())).andExpect(status().isNotFound());
  }

  @Test
  void createUser_shouldReturn409WhenEmailAlreadyExists() throws Exception {
    CreateUserRequest request =
        new CreateUserRequest(
            "John",
            "Doe",
            LocalDate.of(2000, 1, 1),
            "duplicate@example.com",
            "plain-password",
            "+261340000000",
            UserRole.CLIENT);

    mockMvc
        .perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    mockMvc
        .perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isConflict());
  }

  @Test
  void createUser_shouldReturn400WhenRequestIsInvalid() throws Exception {
    CreateUserRequest request =
        new CreateUserRequest(
            "",
            "Doe",
            LocalDate.of(2000, 1, 1),
            "invalid-email",
            "",
            "+261340000000",
            UserRole.CLIENT);

    mockMvc
        .perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
}
