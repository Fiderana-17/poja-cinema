package com.hei.exo.endpoint.rest.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hei.exo.conf.FacadeIT;
import com.hei.exo.dto.request.CreateSeatRequest;
import com.hei.exo.model.Room;
import com.hei.exo.repository.ReservationRepository;
import com.hei.exo.repository.RoomRepository;
import com.hei.exo.repository.SeatRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
class SeatControllerIT extends FacadeIT {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private SeatRepository seatRepository;
  @Autowired private RoomRepository roomRepository;
  @Autowired private ReservationRepository reservationRepository;

  @BeforeEach
  void cleanUpSeats() {
    reservationRepository.deleteAll();
    seatRepository.deleteAll();
    roomRepository.deleteAll();
  }

  @Test
  void getSeats_shouldReturn200AndEmptyList() throws Exception {
    mockMvc.perform(get("/seats")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  void createSeat_shouldReturn200WhenRoomExists() throws Exception {
    Room room = roomRepository.save(new Room(null, "A1", 50));
    CreateSeatRequest request = new CreateSeatRequest("S1", room.getId());

    mockMvc
        .perform(
            post("/seats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.number").value("S1"))
        .andExpect(jsonPath("$.roomId").value(room.getId().toString()));
  }

  @Test
  void getSeatById_shouldReturn200WhenSeatExists() throws Exception {
    Room room = roomRepository.save(new Room(null, "A1", 50));
    CreateSeatRequest request = new CreateSeatRequest("S1", room.getId());

    MvcResult createResult =
        mockMvc
            .perform(
                post("/seats")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andReturn();

    UUID seatId =
        UUID.fromString(
            objectMapper
                .readTree(createResult.getResponse().getContentAsString())
                .get("id")
                .asText());

    mockMvc
        .perform(get("/seats/{id}", seatId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(seatId.toString()))
        .andExpect(jsonPath("$.number").value("S1"))
        .andExpect(jsonPath("$.roomId").value(room.getId().toString()));
  }

  @Test
  void getSeatById_shouldReturn404WhenSeatDoesNotExist() throws Exception {
    mockMvc.perform(get("/seats/{id}", UUID.randomUUID())).andExpect(status().isNotFound());
  }

  @Test
  void createSeat_shouldReturn404WhenRoomDoesNotExist() throws Exception {
    CreateSeatRequest request = new CreateSeatRequest("S1", UUID.randomUUID());

    mockMvc
        .perform(
            post("/seats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  void createSeat_shouldReturn400WhenRequestIsInvalid() throws Exception {
    CreateSeatRequest request = new CreateSeatRequest("", null);

    mockMvc
        .perform(
            post("/seats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
}
