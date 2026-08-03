package com.hei.exo.service;

import com.hei.exo.exception.NotFoundException;
import com.hei.exo.mapper.MovieMapper;
import com.hei.exo.model.Movie;
import com.hei.exo.model.MovieModel;
import com.hei.exo.repository.MovieRepository;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MovieService {

  private final MovieRepository repository;
  private final MovieMapper mapper;

  public List<MovieModel> getAll() {
    return repository.findAll().stream().map(mapper::toModel).toList();
  }

  public MovieModel getById(UUID id) {
    return repository
        .findById(id)
        .map(mapper::toModel)
        .orElseThrow(() -> new NotFoundException("Movie not found"));
  }

  public MovieModel update(UUID id, MovieModel model) {
    Movie existing =
        repository.findById(id).orElseThrow(() -> new NotFoundException("Movie not found"));
    existing.setTitle(model.title());
    existing.setGenres(new HashSet<>(model.genres()));
    existing.setDescription(model.description());
    existing.setDuration(model.duration());
    Movie saved = repository.save(existing);
    return mapper.toModel(saved);
  }
}
