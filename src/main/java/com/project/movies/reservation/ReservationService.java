package com.project.movies.reservation;

import com.project.movies.reservedSeats.IReservedSeatsRepository;
import com.project.movies.reservedSeats.ReservedSeatsModel;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private IReservationRepository reservationRepository;

    @Autowired
    private IReservedSeatsRepository reservedSeatsRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<List<ReservationModel>> getAllReservations(){
        return ResponseEntity.status(OK).body(reservationRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<ReservationModel>> getReservationsByUserId(@PathVariable String userId) {
        return ResponseEntity.status(OK).body(reservationRepository.findByUserId(userId));
    }

    @Transactional
    public ResponseEntity<ReservationModel> createReservation(@RequestBody ReservationModel reservation) {
        try {
            ReservationModel newReservation = new ReservationModel();

            if (reservation.getSessionId() != null) {
                newReservation.setUserId(reservation.getUserId());
                newReservation.setSessionId(reservation.getSessionId());
                newReservation.setDatetime(reservation.getDatetime());
                newReservation.setStatus("pending");

                return ResponseEntity.status(CREATED).body(reservationRepository.save(newReservation));
            } else {
                throw new IllegalArgumentException("Session ID are required");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // TO DO: Change reservation to accepted (once the payment is done)

    // TO DO: Check if the user can cancel the reservation
    // TO DO: Cancel a reservation by a use
    @Transactional
    public ResponseEntity<String> cancelReservationById(@PathVariable Long reservationId) {
        ReservationModel reservation = reservationRepository.findById(reservationId).orElse(null);

        if (reservation == null) {
            return ResponseEntity.status(NOT_FOUND).body("Reservation not found.");
        }

        List<ReservedSeatsModel> reservedSeats = reservedSeatsRepository.findAllByReservationId(reservationId);

        if (reservedSeats.isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).body("There are no reserved seats for this reservation.");
        }

        reservedSeatsRepository.deleteAll(reservedSeats);

        reservation.setStatus("cancelled");
        reservationRepository.save(reservation);


        return ResponseEntity.status(NO_CONTENT).body("Reservation cancelled successfully.");
    }
}
