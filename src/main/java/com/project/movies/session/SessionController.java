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
    private ISessionRepository sessionRepository;

    // Get all sessions by movie id
    @GetMapping("/movie/{movieId}")
    public List<SessionModel> getSessionsByMovieId(@PathVariable String movieId) {
        return sessionRepository.findByMovieId(movieId);
    }

    // Get all sessions by cinema id
    @GetMapping("/cinema/{cinemaId}")
    public List<SessionModel> getSessionsByCinemaId(@PathVariable Long cinemaId) {
        return sessionRepository.findByCinemaId(cinemaId);
    }

    //Get all sessions by date
    @GetMapping
    public ResponseEntity<Object> getSessionsByDate(@RequestParam(required = false) String date) {
        List<SessionModel> sessions;

        if (date != null) {
            if (!date.matches("\\d{8}")) {
                throw new MethodArgumentTypeMismatchException(date, LocalDate.class, null, null, null);
            }
                LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE);
                sessions = sessionRepository.findByDate(parsedDate);

                if (sessions.isEmpty()) {
                    return ResponseEntity.status(404).body(Map.of("message", "No sessions found for this date"));
                }
        } else {
            sessions = sessionRepository.findAll();
        }

        return ResponseEntity.status(200).body(new ApiResponse<>(sessions));
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
            sessionRepository.save(session);
            return ResponseEntity.status(201).body(session);
    }

    // Update session by id
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateSession(@PathVariable Long id, @RequestBody SessionModel session) {
        Optional<SessionModel> sessionOptional = sessionRepository.findById(id);

        if (sessionOptional.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Session not found"));
        }

        SessionModel sessionToUpdate = sessionOptional.get();
//        sessionToUpdate.setMovieId(session.getMovieId());
//        sessionToUpdate.setCinemaId(session.getCinemaId());
        sessionToUpdate.setDate(session.getDate());
        sessionToUpdate.setTime(session.getTime());

        sessionRepository.save(sessionToUpdate);

        return ResponseEntity.status(202).body(new ApiResponse<>(sessionToUpdate));
    }

    // Delete session by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSession(@PathVariable Long id) {
        Optional<SessionModel> session = sessionRepository.findById(id);

        if (session.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Session not found"));
        }

        sessionRepository.deleteById(id);

        return ResponseEntity.status(204).body(null);
    }
}
