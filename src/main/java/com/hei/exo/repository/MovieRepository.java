package com.hei.exo.repository;

import com.hei.exo.model.Movie;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {}
