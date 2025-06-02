package com.project.movies.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationModel>> getAllReservations() {
        return reservationService.getAllReservations();
    }

    // Find reservations of a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationModel>> getReservationsByUser(@PathVariable String userId) {
        return reservationService.getReservationsByUserId(userId);
    }

    // Create a new reservation by a user
    @PostMapping
    public ResponseEntity<ReservationModel> createReservation(@RequestBody ReservationModel reservation) {
        return reservationService.createReservation(reservation);
    }

    @PutMapping("/user/cancel/{reservationId}")
    public ResponseEntity<String> cancelReservationById(@PathVariable Long reservationId) {
        return reservationService.cancelReservationById(reservationId);
    }
}
