package com.hei.exo.mapper;

import com.hei.exo.model.Movie;
import com.hei.exo.model.Projection;
import com.hei.exo.model.ProjectionModel;
import com.hei.exo.model.Room;
import org.springframework.stereotype.Component;

@Component
public class ProjectionMapper {

  public ProjectionModel toModel(Projection entity) {
    return new ProjectionModel(
        entity.getId(),
        entity.getMovie().getId(),
        entity.getRoom().getId(),
        entity.getDatetime(),
        entity.getSeatPrice());
  }

  public Projection toEntity(ProjectionModel model, Movie movie, Room room) {
    return new Projection(model.id(), movie, room, model.datetime(), model.seatPrice());
  }
}
