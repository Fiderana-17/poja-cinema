package com.hei.exo.endpoint.rest.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hei.exo.conf.FacadeIT;
import com.hei.exo.dto.request.ProjectionRequest;
import com.hei.exo.model.Genre;
import com.hei.exo.model.Movie;
import com.hei.exo.model.Projection;
import com.hei.exo.model.Room;
import com.hei.exo.repository.MovieRepository;
import com.hei.exo.repository.ProjectionRepository;
import com.hei.exo.repository.RoomRepository;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectionControllerIT extends FacadeIT {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private ProjectionRepository projectionRepository;
  @Autowired private MovieRepository movieRepository;
  @Autowired private RoomRepository roomRepository;

  private static final String MANAGER = "MANAGER";
  private static final String EMPLOYEE = "EMPLOYEE";
  private static final String CLIENT = "CLIENT";

  private Movie movie;
  private Room room;
  private Projection projection;

  @BeforeEach
  void cleanUpAndSeed() {
    projectionRepository.deleteAll();
    movieRepository.deleteAll();
    roomRepository.deleteAll();

    movie =
        movieRepository.save(
            new Movie(
                null,
                "Inception",
                Set.of(Genre.SCI_FI, Genre.ACTION),
                "A thief with the ability...",
                Duration.ofMinutes(148)));
    room = roomRepository.save(new Room(null, "A1", 50));
    projection =
        projectionRepository.save(
            new Projection(
                null,
                movie,
                room,
                Instant.parse("2026-08-07T14:00:00Z"),
                new BigDecimal("5000.00")));
  }

  @Test
  void getProjections_shouldReturn200ForEveryone() throws Exception {
    projectionRepository.deleteAll();

    mockMvc
        .perform(get("/projections"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  void getProjectionById_shouldReturn200WhenFound() throws Exception {
    mockMvc
        .perform(get("/projections/{id}", projection.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.movieId").value(movie.getId().toString()))
        .andExpect(jsonPath("$.roomId").value(room.getId().toString()))
        .andExpect(jsonPath("$.datetime").value("2026-08-07T14:00:00Z"));
  }

  @Test
  void getProjectionById_shouldReturn404WhenNotFound() throws Exception {
    mockMvc.perform(get("/projections/{id}", UUID.randomUUID())).andExpect(status().isNotFound());
  }

  @Test
  void updateProjection_shouldReturn200ForManager() throws Exception {
    ProjectionRequest request =
        new ProjectionRequest(
            movie.getId(),
            room.getId(),
            Instant.parse("2026-08-08T20:00:00Z"),
            new BigDecimal("6000.00"));

    mockMvc
        .perform(
            put("/projections/{id}", projection.getId())
                .with(user("manager").roles(MANAGER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.datetime").value("2026-08-08T20:00:00Z"))
        .andExpect(jsonPath("$.seatPrice").value(6000.0));
  }

  @Test
  void updateProjection_shouldReturn403ForClient() throws Exception {
    ProjectionRequest request =
        new ProjectionRequest(
            movie.getId(),
            room.getId(),
            Instant.parse("2026-08-08T20:00:00Z"),
            new BigDecimal("6000.00"));

    mockMvc
        .perform(
            put("/projections/{id}", projection.getId())
                .with(user("client").roles(CLIENT))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden());
  }

  @Test
  void updateProjection_shouldReturn403ForEmployee() throws Exception {
    ProjectionRequest request =
        new ProjectionRequest(
            movie.getId(),
            room.getId(),
            Instant.parse("2026-08-08T20:00:00Z"),
            new BigDecimal("6000.00"));

    mockMvc
        .perform(
            put("/projections/{id}", projection.getId())
                .with(user("employee").roles(EMPLOYEE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden());
  }
}
