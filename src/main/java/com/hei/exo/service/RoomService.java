package com.hei.exo.service;

import com.hei.exo.entity.Room;
import com.hei.exo.exception.RoomNotFoundException;
import com.hei.exo.mapper.RoomMapper;
import com.hei.exo.model.RoomModel;
import com.hei.exo.repository.RoomRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoomService {

  private final RoomRepository repository;
  private final RoomMapper mapper;

  public List<RoomModel> getAll() {
    return repository.findAll().stream().map(mapper::toModel).toList();
  }

  public RoomModel getById(UUID id) {
    return repository.findById(id).map(mapper::toModel).orElse(null);
  }

  public RoomModel save(RoomModel model) {
    Room entity = mapper.toEntity(model);
    Room saved = repository.save(entity);
    return mapper.toModel(saved);
  }

  public RoomModel update(UUID id, RoomModel model) {
    Room existing = repository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
    existing.setNumber(model.number());
    existing.setCapacity(model.capacity());
    Room saved = repository.save(existing);
    return mapper.toModel(saved);
  }
}
