//package com.project.movies.controllers;
//
////TODO: Add validations and uniqueness seats
//
//import com.project.movies.reservedSeats.ReservedSeatsModel;
//import com.project.movies.seat.SeatModel;
//import com.project.movies.reservedSeats.IReservedSeatsRepository;
//import com.project.movies.seat.ISeatRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.*;
//
//@CrossOrigin(origins = "http://localhost:4000")
//@RestController
//@RequestMapping("/api/seats")
//public class SeatController {
//
//    @Autowired
//    private ISeatRepository seatRepository;
//
//    @Autowired
//    private IReservedSeatsRepository reservedSeatsRepository;
//
//    // Create a seat
//    @PostMapping
//    public ResponseEntity<SeatModel> createSeat(@RequestBody SeatModel seat) {
//        return ResponseEntity.status(201).body(seatRepository.save(seat));
//    }
//
//    // Get all seats by a cinema
//    @GetMapping("/cinema/{cinemaId}")
//    public ResponseEntity<List<SeatModel>> getSeatsByCinema(@PathVariable Long cinemaId) {
//        return ResponseEntity.ok(seatRepository.findByCinemaId(cinemaId));
//    }
//
//    // Get all seats
//    @GetMapping
//    public ResponseEntity<List<SeatModel>> getAllSeats() {
//        return ResponseEntity.ok(seatRepository.findAll());
//    }
//
//    // Get all seat by a session
//    @GetMapping("/session/{sessionId}")
//    public ResponseEntity<List<Map<String, Object>>> getSeatsBySession(@PathVariable Long sessionId) {
//        Long cinemaId = seatRepository.findCinemaIdBySessionId(sessionId);
//
//        List<SeatModel> allSeats = seatRepository.findAllByCinemaId(cinemaId);
//
//        List<Map<String, Object>> seatInfoList = new ArrayList<>();
//
//        for (SeatModel seat : allSeats) {
//            Map<String, Object> seatInfo = new LinkedHashMap<>();
//            seatInfo.put("id", seat.getId());
//            seatInfo.put("row", seat.getRow());
//            seatInfo.put("seat", seat.getSeat());
//            seatInfo.put("status", "available");
//            seatInfoList.add(seatInfo);
//        }
//
//        List<ReservedSeatsModel> reservedSeats = reservedSeatsRepository.findAllBySessionId(sessionId);
//
//        for (ReservedSeatsModel reservedSeat : reservedSeats) {
//            Long seatId = reservedSeat.getRow();
//
//            for (Map<String, Object> seatInfo : seatInfoList) {
//                if (seatInfo.get("id").equals(seatId)) {
//                    String status = reservedSeat.getStatus();
//                    seatInfo.put("status", status);
//                    break;
//                }
//            }
//        }
//
//        seatInfoList.sort(Comparator.comparingInt((Map<String, Object> seat) -> (Integer) seat.get("row"))
//                .thenComparingInt(seat -> (Integer) seat.get("seat")));
//
//        return ResponseEntity.ok(seatInfoList);
//    }
//
//}
