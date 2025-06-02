package com.project.movies.reservedSeats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequestMapping("/api/reserved-seats")
public class ReservedSeatsController {

    @Autowired
    private IReservedSeatsRepository reservedSeatsRepository;

    // Block seat by a reservation id (cuando clica el usu en la butaca)
    @PostMapping
    public ResponseEntity<ReservedSeatsModel> blockSeat(@RequestBody ReservedSeatsModel infoReservation) {
        ReservedSeatsModel seat = reservedSeatsRepository.findByRowAndSeatAndReservationId(infoReservation.getRow(), infoReservation.getSeat(), infoReservation.getReservationId()).orElse(null);

        if (seat != null && seat.getStatus().equals("occupied")) {
            return ResponseEntity.status(403).body(null);
        }

        ReservedSeatsModel newSeat = new ReservedSeatsModel();

        newSeat.setRow(infoReservation.getRow());
        newSeat.setSeat(infoReservation.getSeat());
        newSeat.setReservationId(infoReservation.getReservationId());
        newSeat.setStatus("blocked");

        reservedSeatsRepository.save(newSeat);

        return ResponseEntity.status(200).body(newSeat);
    }

    // Occupy seat
    @PutMapping("/occupy/{reservationId}")
    public ResponseEntity<List<ReservedSeatsModel>> occupySeat(@PathVariable Long reservationId) {
        List<ReservedSeatsModel> seats = reservedSeatsRepository.findAllByReservationId(reservationId);

        if (seats == null) {
            return ResponseEntity.status(404).body(null);
        }

        for (ReservedSeatsModel seat : seats) {
            if (!seat.getStatus().equals("blocked")) {
                return ResponseEntity.status(403).body(null);
            }

            seat.setStatus("occupied");
            reservedSeatsRepository.save(seat);
        }

        return ResponseEntity.status(200).body(seats);
    }

    // Get reserved seats by reservation id
    @GetMapping("/{reservationId}")
    public ResponseEntity<List<ReservedSeatsModel>> getReservedSeats(@PathVariable Long reservationId) {
        List<ReservedSeatsModel> seats = reservedSeatsRepository.findAllByReservationId(reservationId);

        return ResponseEntity.status(200).body(seats);
    }

    // Get reserved seats by session id
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<ReservedSeatsModel>> getReservedSeatsBySession(@PathVariable Long sessionId) {
        List<ReservedSeatsModel> seats = reservedSeatsRepository.findAllBySessionId(sessionId);

        return ResponseEntity.status(200).body(seats);
    }

    // Release blocked seats by reservation id
    @PutMapping("/release/{reservationId}")
    public ResponseEntity<List<ReservedSeatsModel>> releaseBlockedSeats(@PathVariable Long reservationId) {
        List<ReservedSeatsModel> seats = reservedSeatsRepository.findAllByReservationId(reservationId);

        for (ReservedSeatsModel seat : seats) {
            if (seat.getStatus().equals("blocked")) {
                seat.setStatus("available");
            }
        }

        reservedSeatsRepository.saveAll(seats);

        return ResponseEntity.status(204).body(seats);
    }
}
