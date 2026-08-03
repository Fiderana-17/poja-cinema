package com.hei.exo.mapper;

import com.hei.exo.model.Movie;
import com.hei.exo.model.MovieModel;
import java.util.HashSet;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

  public MovieModel toModel(Movie entity) {
    return new MovieModel(
        entity.getId(),
        entity.getTitle(),
        entity.getGenres(),
        entity.getDescription(),
        entity.getDuration());
  }

  public Movie toEntity(MovieModel model) {
    return new Movie(
        model.id(),
        model.title(),
        new HashSet<>(model.genres()),
        model.description(),
        model.duration());
  }
}
