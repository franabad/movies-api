package com.project.movies.reservedSeats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IReservedSeatsRepository extends JpaRepository<ReservedSeatsModel, Long> {
    List<ReservedSeatsModel> findAllByReservationId(Long reservationId);

    Optional<ReservedSeatsModel> findByRowAndSeatAndReservationId(Long row, Long seat, Long reservationId);

    @Query("SELECT rs FROM ReservedSeatsModel rs WHERE rs.reservationId IN (SELECT r.id FROM ReservationModel r WHERE r.sessionId = :sessionId)")
    List<ReservedSeatsModel> findAllBySessionId(@Param("sessionId") Long sessionId);
}
