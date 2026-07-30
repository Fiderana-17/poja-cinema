package com.hei.exo.mapper;

import com.hei.exo.entity.Room;
import com.hei.exo.model.RoomModel;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

  public RoomModel toModel(Room entity) {
    return new RoomModel(entity.getId(), entity.getNumber(), entity.getCapacity());
  }

  public Room toEntity(RoomModel model) {
    return new Room(model.id(), model.number(), model.capacity());
  }
}
