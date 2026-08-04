package com.hei.exo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.hei.exo.exception.NotFoundException;
import com.hei.exo.mapper.MovieMapper;
import com.hei.exo.model.Genre;
import com.hei.exo.model.Movie;
import com.hei.exo.model.MovieModel;
import com.hei.exo.repository.MovieRepository;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

  @Mock private MovieRepository repository;
  @Mock private MovieMapper mapper;

  private MovieService service;
  private UUID movieId;
  private Movie movieEntity;
  private MovieModel movieModel;

  @BeforeEach
  void setUp() {
    service = new MovieService(repository, mapper);
    movieId = UUID.randomUUID();
    Set<Genre> genres = Set.of(Genre.ACTION, Genre.SCI_FI);
    movieEntity =
        new Movie(
            movieId, "Inception", genres, "A thief with the ability...", Duration.ofMinutes(148));
    movieModel =
        new MovieModel(
            movieId, "Inception", genres, "A thief with the ability...", Duration.ofMinutes(148));
  }

  @Test
  void getAll_shouldReturnAllMovies() {
    when(repository.findAll()).thenReturn(List.of(movieEntity));
    when(mapper.toModel(movieEntity)).thenReturn(movieModel);

    List<MovieModel> result = service.getAll();

    assertEquals(1, result.size());
    assertEquals(movieModel, result.getFirst());
  }

  @Test
  void getById_shouldReturnMovieWhenFound() {
    when(repository.findById(movieId)).thenReturn(Optional.of(movieEntity));
    when(mapper.toModel(movieEntity)).thenReturn(movieModel);

    MovieModel result = service.getById(movieId);

    assertEquals(movieModel, result);
  }

  @Test
  void getById_shouldThrowWhenNotFound() {
    when(repository.findById(movieId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> service.getById(movieId));
  }

  @Test
  void update_shouldModifyExistingMovie() {
    Movie existingEntity =
        new Movie(movieId, "Old", Set.of(Genre.DRAMA), "Old description", Duration.ofMinutes(90));
    MovieModel updateModel =
        new MovieModel(
            movieId, "New", Set.of(Genre.ACTION), "New description", Duration.ofMinutes(120));
    Movie updatedEntity =
        new Movie(movieId, "New", Set.of(Genre.ACTION), "New description", Duration.ofMinutes(120));

    when(repository.findById(movieId)).thenReturn(Optional.of(existingEntity));
    when(repository.save(any())).thenReturn(updatedEntity);
    when(mapper.toModel(updatedEntity)).thenReturn(updateModel);

    MovieModel result = service.update(movieId, updateModel);

    assertEquals("New", result.title());
    assertEquals(Set.of(Genre.ACTION), result.genres());
    assertEquals(Duration.ofMinutes(120), result.duration());
    verify(repository).save(any());
  }

  @Test
  void update_shouldThrowWhenNotFound() {
    when(repository.findById(movieId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> service.update(movieId, movieModel));
  }
}
