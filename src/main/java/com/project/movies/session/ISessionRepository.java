package com.project.movies.session;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ISessionRepository extends JpaRepository<SessionModel, Long> {
    List<SessionModel> findByMovieId(String movieId);

    List<SessionModel> findByCinemaId(Long cinemaId);

    List<SessionModel> findByDate(LocalDate date);
}
