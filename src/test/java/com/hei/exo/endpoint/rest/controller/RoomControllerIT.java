package com.hei.exo.endpoint.rest.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hei.exo.conf.FacadeIT;
import com.hei.exo.dto.request.RoomRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerIT extends FacadeIT {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  private static final String MANAGER = "MANAGER";
  private static final String CLIENT = "CLIENT";

  @Test
  void getRooms_shouldReturn200ForEveryone() throws Exception {
    mockMvc.perform(get("/rooms")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  void getRoomById_shouldReturn404WhenNotFound() throws Exception {
    mockMvc
        .perform(get("/rooms/{id}", UUID.randomUUID()))
        .andExpect(status().isNotFound());
  }

  @Test
  void createRoom_shouldReturn201ForManager() throws Exception {
    RoomRequest request = new RoomRequest("A1", 50);

    mockMvc
        .perform(
            post("/rooms")
                .with(user("manager").roles(MANAGER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.number").value("A1"))
        .andExpect(jsonPath("$.capacity").value(50));
  }

  @Test
  void createRoom_shouldReturn403ForClient() throws Exception {
    RoomRequest request = new RoomRequest("A1", 50);

    mockMvc
        .perform(
            post("/rooms")
                .with(user("client").roles(CLIENT))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden());
  }

  @Test
  void updateRoom_shouldReturn200ForManager() throws Exception {
    RoomRequest createRequest = new RoomRequest("B2", 30);
    MvcResult createResult =
        mockMvc
            .perform(
                post("/rooms")
                    .with(user("manager").roles(MANAGER))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
            .andExpect(status().isCreated())
            .andReturn();
    UUID roomId =
        UUID.fromString(
            objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id")
                .asText());

    RoomRequest updateRequest = new RoomRequest("B2 Updated", 45);

    mockMvc
        .perform(
            put("/rooms/{id}", roomId)
                .with(user("manager").roles(MANAGER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.number").value("B2 Updated"))
        .andExpect(jsonPath("$.capacity").value(45));
  }

  @Test
  void updateRoom_shouldReturn403ForClient() throws Exception {
    RoomRequest updateRequest = new RoomRequest("B2 Updated", 45);

    mockMvc
        .perform(
            put("/rooms/{id}", UUID.randomUUID())
                .with(user("client").roles(CLIENT))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isForbidden());
  }
}
