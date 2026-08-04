package com.hei.exo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(optional = false)
  @JoinColumn(name = "seat_id", nullable = false)
  private Seat seat;

  @Column(name = "projection_id", nullable = false)
  private UUID projectionId;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReservationStatus status;

  @PrePersist
  void prePersist() {
    if (createdAt == null) {
      createdAt = Instant.now();
    }

    if (status == null) {
      status = ReservationStatus.PENDING;
    }
  }
}
