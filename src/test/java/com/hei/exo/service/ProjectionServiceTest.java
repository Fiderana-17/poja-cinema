package com.hei.exo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hei.exo.exception.NotFoundException;
import com.hei.exo.mapper.ProjectionMapper;
import com.hei.exo.model.Genre;
import com.hei.exo.model.Movie;
import com.hei.exo.model.Projection;
import com.hei.exo.model.ProjectionModel;
import com.hei.exo.model.Room;
import com.hei.exo.repository.MovieRepository;
import com.hei.exo.repository.ProjectionRepository;
import com.hei.exo.repository.RoomRepository;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
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
class ProjectionServiceTest {

  @Mock private ProjectionRepository projectionRepository;
  @Mock private MovieRepository movieRepository;
  @Mock private RoomRepository roomRepository;
  @Mock private ProjectionMapper mapper;

  private ProjectionService service;
  private UUID projectionId;
  private UUID movieId;
  private UUID roomId;
  private Projection projectionEntity;
  private ProjectionModel projectionModel;

  @BeforeEach
  void setUp() {
    service = new ProjectionService(projectionRepository, movieRepository, roomRepository, mapper);
    projectionId = UUID.randomUUID();
    movieId = UUID.randomUUID();
    roomId = UUID.randomUUID();
    Movie movie =
        new Movie(
            movieId,
            "Inception",
            Set.of(Genre.ACTION, Genre.SCI_FI),
            "A thief with the ability...",
            Duration.ofMinutes(148));
    Room room = new Room(roomId, "A1", 50);
    Instant datetime = Instant.parse("2026-08-07T14:00:00Z");
    BigDecimal seatPrice = new BigDecimal("5000.00");
    projectionEntity = new Projection(projectionId, movie, room, datetime, seatPrice);
    projectionModel = new ProjectionModel(projectionId, movieId, roomId, datetime, seatPrice);
  }

  @Test
  void getAll_shouldReturnAllProjections() {
    when(projectionRepository.findAll()).thenReturn(List.of(projectionEntity));
    when(mapper.toModel(projectionEntity)).thenReturn(projectionModel);

    List<ProjectionModel> result = service.getAll();

    assertEquals(1, result.size());
    assertEquals(projectionModel, result.getFirst());
  }

  @Test
  void getById_shouldReturnProjectionWhenFound() {
    when(projectionRepository.findById(projectionId)).thenReturn(Optional.of(projectionEntity));
    when(mapper.toModel(projectionEntity)).thenReturn(projectionModel);

    ProjectionModel result = service.getById(projectionId);

    assertEquals(projectionModel, result);
  }

  @Test
  void getById_shouldThrowWhenNotFound() {
    when(projectionRepository.findById(projectionId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> service.getById(projectionId));
  }

  @Test
  void update_shouldModifyExistingProjection() {
    Movie newMovie =
        new Movie(
            UUID.randomUUID(),
            "Interstellar",
            Set.of(Genre.SCI_FI),
            "A team travels through a wormhole",
            Duration.ofMinutes(169));
    Room newRoom = new Room(UUID.randomUUID(), "B2", 80);
    Instant newDatetime = Instant.parse("2026-08-08T20:00:00Z");
    BigDecimal newSeatPrice = new BigDecimal("6000.00");
    ProjectionModel updateModel =
        new ProjectionModel(
            projectionId, newMovie.getId(), newRoom.getId(), newDatetime, newSeatPrice);
    Projection updatedEntity =
        new Projection(projectionId, newMovie, newRoom, newDatetime, newSeatPrice);

    when(movieRepository.findById(newMovie.getId())).thenReturn(Optional.of(newMovie));
    when(roomRepository.findById(newRoom.getId())).thenReturn(Optional.of(newRoom));
    when(projectionRepository.findById(projectionId)).thenReturn(Optional.of(projectionEntity));
    when(projectionRepository.save(any())).thenReturn(updatedEntity);
    when(mapper.toModel(updatedEntity)).thenReturn(updateModel);

    ProjectionModel result = service.update(projectionId, updateModel);

    assertEquals(newDatetime, result.datetime());
    assertEquals(newSeatPrice, result.seatPrice());
    verify(projectionRepository).save(any());
  }

  @Test
  void update_shouldThrowWhenMovieNotFound() {
    when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> service.update(projectionId, projectionModel));
  }

  @Test
  void update_shouldThrowWhenRoomNotFound() {
    when(movieRepository.findById(movieId)).thenReturn(Optional.of(projectionEntity.getMovie()));
    when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> service.update(projectionId, projectionModel));
  }

  @Test
  void update_shouldThrowWhenProjectionNotFound() {
    when(movieRepository.findById(movieId)).thenReturn(Optional.of(projectionEntity.getMovie()));
    when(roomRepository.findById(roomId)).thenReturn(Optional.of(projectionEntity.getRoom()));
    when(projectionRepository.findById(projectionId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> service.update(projectionId, projectionModel));
  }
}
