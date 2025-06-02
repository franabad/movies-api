package com.project.movies.reservation;

import com.project.movies.reservedSeats.ReservedSeatsModel;
import com.project.movies.reservedSeats.IReservedSeatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private IReservationRepository reservationRepository;

    @Autowired
    private IReservedSeatsRepository reservedSeatsRepository;

    @GetMapping
    public List<ReservationModel> getReservations() {
        return reservationRepository.findAll();
    }

    // Find reservations of a specific user
    @GetMapping("/user/{userId}")
    public List<ReservationModel> getReservationsByUser(@PathVariable String userId) {
        return reservationRepository.findByUserId(userId);
    }

    // TODO: Change reservation to accepted (once the payment is done)

    // TODO: Check if the user can cancel the reservation
    // Cancel a reservation by a user
    @PutMapping("/user/cancel/{reservationId}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long reservationId) {
        ReservationModel reservation = reservationRepository.findById(reservationId).orElse(null);

        if (reservation == null) {
            return ResponseEntity.notFound().build();
        }

        List<ReservedSeatsModel> reservedSeats = reservedSeatsRepository.findAllByReservationId(reservationId);

        if (reservedSeats.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no reserved seats for this reservation.");
        }

        reservedSeatsRepository.deleteAll(reservedSeats);

        reservation.setStatus("cancelled");
        reservationRepository.save(reservation);


        return ResponseEntity.status(202).body("Reservation cancelled successfully.");
    }

    // Create a new reservation by a user
    @PostMapping
    public ReservationModel createReservation(@RequestBody ReservationModel reservation) {
        try {
            ReservationModel newReservation = new ReservationModel();

            if (reservation.getSessionId() != null) {
                newReservation.setUserId(reservation.getUserId());
                newReservation.setSessionId(reservation.getSessionId());
                newReservation.setDatetime(reservation.getDatetime());
                newReservation.setStatus("pending");

                return reservationRepository.save(newReservation);
            } else {
                throw new IllegalArgumentException("Session ID are required");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
