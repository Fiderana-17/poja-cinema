package com.hei.exo.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.hei.exo.model.Genre;
import com.hei.exo.model.Movie;
import com.hei.exo.model.MovieModel;
import java.time.Duration;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MovieMapperTest {

  private final MovieMapper mapper = new MovieMapper();

  @Test
  void toModel_shouldMapAllEntityFields() {
    UUID id = UUID.randomUUID();
    Set<Genre> genres = Set.of(Genre.ACTION, Genre.SCI_FI);
    Movie entity =
        new Movie(id, "Inception", genres, "A thief with the ability...", Duration.ofMinutes(148));

    MovieModel model = mapper.toModel(entity);

    assertEquals(id, model.id());
    assertEquals("Inception", model.title());
    assertEquals(genres, model.genres());
    assertEquals("A thief with the ability...", model.description());
    assertEquals(Duration.ofMinutes(148), model.duration());
  }

  @Test
  void toEntity_shouldMapAllModelFields() {
    UUID id = UUID.randomUUID();
    Set<Genre> genres = Set.of(Genre.ACTION, Genre.SCI_FI);
    MovieModel model =
        new MovieModel(
            id, "Inception", genres, "A thief with the ability...", Duration.ofMinutes(148));

    Movie entity = mapper.toEntity(model);

    assertEquals(id, entity.getId());
    assertEquals("Inception", entity.getTitle());
    assertEquals(genres, entity.getGenres());
    assertEquals("A thief with the ability...", entity.getDescription());
    assertEquals(Duration.ofMinutes(148), entity.getDuration());
  }
}
