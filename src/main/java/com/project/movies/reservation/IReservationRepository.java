package com.project.movies.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IReservationRepository extends JpaRepository<ReservationModel, Long> {
    List<ReservationModel> findByUserId(String userId);
}
