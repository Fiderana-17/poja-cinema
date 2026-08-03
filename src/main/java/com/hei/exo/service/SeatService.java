package com.hei.exo.service;

import com.hei.exo.dto.request.CreateSeatRequest;
import com.hei.exo.dto.response.SeatResponse;
import com.hei.exo.exception.NotFoundException;
import com.hei.exo.mapper.SeatMapper;
import com.hei.exo.repository.SeatRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SeatService {

  private final SeatRepository seatRepository;
  private final SeatMapper seatMapper;

  public List<SeatResponse> getAll() {
    return seatRepository.findAll().stream().map(seatMapper::toResponse).toList();
  }

  public SeatResponse getById(UUID id) {
    return seatRepository
        .findById(id)
        .map(seatMapper::toResponse)
        .orElseThrow(() -> new NotFoundException("Seat not found"));
  }

  public SeatResponse create(CreateSeatRequest request) {
    var seat = seatMapper.toModel(request);
    var savedSeat = seatRepository.save(seat);
    return seatMapper.toResponse(savedSeat);
  }
}
