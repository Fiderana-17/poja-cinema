package com.hei.exo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.hei.exo.exception.RoomNotFoundException;
import com.hei.exo.mapper.RoomMapper;
import com.hei.exo.model.Room;
import com.hei.exo.model.RoomModel;
import com.hei.exo.repository.RoomRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

  @Mock private RoomRepository repository;
  @Mock private RoomMapper mapper;

  private RoomService service;
  private UUID roomId;
  private Room roomEntity;
  private RoomModel roomModel;

  @BeforeEach
  void setUp() {
    service = new RoomService(repository, mapper);
    roomId = UUID.randomUUID();
    roomEntity = new Room(roomId, "A1", 50);
    roomModel = new RoomModel(roomId, "A1", 50);
  }

  @Test
  void getAll_shouldReturnAllRooms() {
    when(repository.findAll()).thenReturn(List.of(roomEntity));
    when(mapper.toModel(roomEntity)).thenReturn(roomModel);

    List<RoomModel> result = service.getAll();

    assertEquals(1, result.size());
    assertEquals(roomModel, result.getFirst());
  }

  @Test
  void getById_shouldReturnRoomWhenFound() {
    when(repository.findById(roomId)).thenReturn(Optional.of(roomEntity));
    when(mapper.toModel(roomEntity)).thenReturn(roomModel);

    RoomModel result = service.getById(roomId);

    assertEquals(roomModel, result);
  }

  @Test
  void getById_shouldReturnNullWhenNotFound() {
    when(repository.findById(roomId)).thenReturn(Optional.empty());

    RoomModel result = service.getById(roomId);

    assertNull(result);
  }

  @Test
  void save_shouldPersistAndReturnRoom() {
    when(mapper.toEntity(roomModel)).thenReturn(roomEntity);
    when(repository.save(roomEntity)).thenReturn(roomEntity);
    when(mapper.toModel(roomEntity)).thenReturn(roomModel);

    RoomModel result = service.save(roomModel);

    assertEquals(roomModel, result);
    verify(repository).save(roomEntity);
  }

  @Test
  void update_shouldModifyExistingRoom() {
    UUID otherId = UUID.randomUUID();
    Room existingEntity = new Room(roomId, "Old", 10);
    RoomModel updateModel = new RoomModel(otherId, "A1 Updated", 100);
    Room updatedEntity = new Room(roomId, "A1 Updated", 100);

    when(repository.findById(roomId)).thenReturn(Optional.of(existingEntity));
    when(repository.save(any())).thenReturn(updatedEntity);
    when(mapper.toModel(updatedEntity)).thenReturn(new RoomModel(roomId, "A1 Updated", 100));

    RoomModel result = service.update(roomId, updateModel);

    assertEquals("A1 Updated", result.number());
    assertEquals(100, result.capacity());
    verify(repository).save(any());
  }

  @Test
  void update_shouldThrowWhenNotFound() {
    when(repository.findById(roomId)).thenReturn(Optional.empty());

    assertThrows(RoomNotFoundException.class, () -> service.update(roomId, roomModel));
  }
}
