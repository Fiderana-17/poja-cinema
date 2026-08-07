package com.hei.exo.service;

import com.hei.exo.exception.NotFoundException;
import com.hei.exo.mapper.ProjectionMapper;
import com.hei.exo.model.Movie;
import com.hei.exo.model.Projection;
import com.hei.exo.model.ProjectionModel;
import com.hei.exo.model.Room;
import com.hei.exo.repository.MovieRepository;
import com.hei.exo.repository.ProjectionRepository;
import com.hei.exo.repository.RoomRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectionService {

  private final ProjectionRepository projectionRepository;
  private final MovieRepository movieRepository;
  private final RoomRepository roomRepository;
  private final ProjectionMapper mapper;

  public List<ProjectionModel> getAll() {
    return projectionRepository.findAll().stream().map(mapper::toModel).toList();
  }

  public ProjectionModel getById(UUID id) {
    return projectionRepository
        .findById(id)
        .map(mapper::toModel)
        .orElseThrow(() -> new NotFoundException("Projection not found"));
  }

  public ProjectionModel update(UUID id, ProjectionModel model) {
    Movie movie =
        movieRepository
            .findById(model.movieId())
            .orElseThrow(() -> new NotFoundException("Movie not found"));
    Room room =
        roomRepository
            .findById(model.roomId())
            .orElseThrow(() -> new NotFoundException("Room not found"));
    Projection existing =
        projectionRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Projection not found"));
    existing.setMovie(movie);
    existing.setRoom(room);
    existing.setDatetime(model.datetime());
    existing.setSeatPrice(model.seatPrice());
    Projection saved = projectionRepository.save(existing);
    return mapper.toModel(saved);
  }
}
