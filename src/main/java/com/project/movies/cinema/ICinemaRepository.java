package com.project.movies.cinema;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ICinemaRepository extends JpaRepository<CinemaModel, Long> {
}
