package com.project.movies.seat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISeatRepository extends JpaRepository<SeatModel, Long> {
    List<SeatModel> findByCinemaId(Long cinemaId);

    @Query("SELECT cinemaId FROM SessionModel s WHERE s.id = :sessionId")
    Long findCinemaIdBySessionId(@Param("sessionId") Long sessionId);

    List<SeatModel> findAllByCinemaId(Long cinemaId);
}
