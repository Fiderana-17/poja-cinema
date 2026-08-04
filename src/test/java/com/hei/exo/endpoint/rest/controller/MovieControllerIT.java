package com.hei.exo.endpoint.rest.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hei.exo.conf.FacadeIT;
import com.hei.exo.dto.request.MovieRequest;
import com.hei.exo.model.Genre;
import com.hei.exo.model.Movie;
import com.hei.exo.repository.MovieRepository;
import java.time.Duration;
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
class MovieControllerIT extends FacadeIT {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private MovieRepository movieRepository;

  @BeforeEach
  void cleanUpMovies() {
    movieRepository.deleteAll();
  }

  private static final String MANAGER = "MANAGER";
  private static final String EMPLOYEE = "EMPLOYEE";
  private static final String CLIENT = "CLIENT";

  @Test
  void getMovies_shouldReturn200ForEveryone() throws Exception {
    mockMvc.perform(get("/movies")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  void getMovieById_shouldReturn200WhenFound() throws Exception {
    Movie movie =
        movieRepository.save(
            new Movie(
                null,
                "Inception",
                Set.of(Genre.SCI_FI, Genre.ACTION),
                "A thief with the ability...",
                Duration.ofMinutes(148)));

    mockMvc
        .perform(get("/movies/{id}", movie.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Inception"))
        .andExpect(jsonPath("$.genres", hasSize(2)));
  }

  @Test
  void getMovieById_shouldReturn404WhenNotFound() throws Exception {
    mockMvc.perform(get("/movies/{id}", UUID.randomUUID())).andExpect(status().isNotFound());
  }

  @Test
  void updateMovie_shouldReturn200ForManager() throws Exception {
    Movie movie =
        movieRepository.save(
            new Movie(
                null,
                "Inception",
                Set.of(Genre.SCI_FI),
                "A thief with the ability...",
                Duration.ofMinutes(148)));
    MovieRequest request =
        new MovieRequest(
            "Inception 2", Set.of(Genre.ACTION), "New description", Duration.ofMinutes(120));

    mockMvc
        .perform(
            put("/movies/{id}", movie.getId())
                .with(user("manager").roles(MANAGER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Inception 2"))
        .andExpect(jsonPath("$.genres[0]").value("ACTION"))
        .andExpect(jsonPath("$.description").value("New description"));
  }

  @Test
  void updateMovie_shouldReturn403ForClient() throws Exception {
    Movie movie =
        movieRepository.save(
            new Movie(
                null,
                "Inception",
                Set.of(Genre.SCI_FI),
                "A thief with the ability...",
                Duration.ofMinutes(148)));
    MovieRequest request =
        new MovieRequest(
            "Inception 2", Set.of(Genre.ACTION), "New description", Duration.ofMinutes(120));

    mockMvc
        .perform(
            put("/movies/{id}", movie.getId())
                .with(user("client").roles(CLIENT))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden());
  }

  @Test
  void updateMovie_shouldReturn403ForEmployee() throws Exception {
    Movie movie =
        movieRepository.save(
            new Movie(
                null,
                "Inception",
                Set.of(Genre.SCI_FI),
                "A thief with the ability...",
                Duration.ofMinutes(148)));
    MovieRequest request =
        new MovieRequest(
            "Inception 2", Set.of(Genre.ACTION), "New description", Duration.ofMinutes(120));

    mockMvc
        .perform(
            put("/movies/{id}", movie.getId())
                .with(user("employee").roles(EMPLOYEE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden());
  }
}
