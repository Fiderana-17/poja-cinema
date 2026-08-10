package com.hei.exo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hei.exo.model.Genre;
import com.hei.exo.model.Movie;
import com.hei.exo.model.Projection;
import com.hei.exo.model.ProjectionModel;
import com.hei.exo.model.Room;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProjectionMapperTest {

  private final ProjectionMapper mapper = new ProjectionMapper();

  @Test
  void toModel_shouldMapAllEntityFields() {
    UUID projectionId = UUID.randomUUID();
    Movie movie =
        new Movie(
            UUID.randomUUID(),
            "Inception",
            Set.of(Genre.ACTION, Genre.SCI_FI),
            "A thief with the ability...",
            Duration.ofMinutes(148));
    Room room = new Room(UUID.randomUUID(), "A1", 50);
    Instant datetime = Instant.parse("2026-08-07T14:00:00Z");
    BigDecimal seatPrice = new BigDecimal("5000.00");
    Projection entity = new Projection(projectionId, movie, room, datetime, seatPrice);

    ProjectionModel model = mapper.toModel(entity);

    assertEquals(projectionId, model.id());
    assertEquals(movie.getId(), model.movieId());
    assertEquals(room.getId(), model.roomId());
    assertEquals(datetime, model.datetime());
    assertEquals(seatPrice, model.seatPrice());
  }

  @Test
  void toEntity_shouldMapAllModelFields() {
    UUID projectionId = UUID.randomUUID();
    Movie movie =
        new Movie(
            UUID.randomUUID(),
            "Inception",
            Set.of(Genre.ACTION, Genre.SCI_FI),
            "A thief with the ability...",
            Duration.ofMinutes(148));
    Room room = new Room(UUID.randomUUID(), "A1", 50);
    Instant datetime = Instant.parse("2026-08-07T14:00:00Z");
    BigDecimal seatPrice = new BigDecimal("5000.00");
    ProjectionModel model =
        new ProjectionModel(projectionId, movie.getId(), room.getId(), datetime, seatPrice);

    Projection entity = mapper.toEntity(model, movie, room);

    assertEquals(projectionId, entity.getId());
    assertEquals(movie.getId(), entity.getMovie().getId());
    assertEquals(room.getId(), entity.getRoom().getId());
    assertEquals(datetime, entity.getDatetime());
    assertEquals(seatPrice, entity.getSeatPrice());
  }
}
