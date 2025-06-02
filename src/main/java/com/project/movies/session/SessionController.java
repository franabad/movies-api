package com.project.movies.session;

import com.project.movies.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    // Get all sessions by movie id
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<SessionModel>> getSessionsByMovieId(@PathVariable String movieId) {
        return sessionService.getAllSessionsByMovieId(movieId);
    }

    // Get all sessions by cinema id
    @GetMapping("/cinema/{cinemaId}")
    public ResponseEntity<List<SessionModel>> getAllSessionsByCinemaId(@PathVariable Long cinemaId) {
        return sessionService.getAllSessionsByCinemaId(cinemaId);
    }

    //Get all sessions by date
    @GetMapping
    public ResponseEntity<Object> getAllSessionsByDate(@RequestParam(required = false) String date) {
        return sessionService.getAllSessionsByDate(date);
    }

    /*
        TODO Handle error creating a session with a movie that does not exist
        TODO Handle error creating a session with a cinema that does not exist
        TODO Handle error creating a session with a date that is in the past
        TODO Handle error creating a session with a time that is in the past
        TODO Handle error creating a session at the same time in the same cinema
    */

    // Create new session (for a movie)
    @PostMapping
    public ResponseEntity<Object> createSession(@Valid @RequestBody SessionModel session) {
        return ResponseEntity.status(201).body(sessionService.createSession(session));
    }

    // Update session by id
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateSession(@PathVariable Long id, @RequestBody SessionModel session) {
        return sessionService.updateSession(id, session);
    }

    // Delete session by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSession(@PathVariable Long id) {
        return sessionService.deleteSession(id);
    }
}
