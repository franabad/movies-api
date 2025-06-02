package com.project.movies.session;

import com.project.movies.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// FIX ME: Change exception message when PathVariable is not int
// TO DO: Add exceptions
@Service
public class SessionService {

    @Autowired
    private ISessionRepository sessionRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getAllSessionsByMovieId(@PathVariable String movieId) {
        List<SessionModel> sessions = sessionRepository.findByMovieId(movieId);

        if (sessions.isEmpty()) {
            Map<String, String> errorResponse = Map.of("message", "No sessions found for this movie");
            return ResponseEntity.status(NOT_FOUND).body(errorResponse);
        }

        return ResponseEntity.status(OK).body(sessions);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getAllSessionsByCinemaId(@PathVariable Long cinemaId) {
        List<SessionModel> sessions = sessionRepository.findByCinemaId(cinemaId);

        if (sessions.isEmpty()) {
            Map<String, String> errorResponse = Map.of("message", "No sessions found for this cinema");
            return ResponseEntity.status(NOT_FOUND).body(errorResponse);
        }

        return ResponseEntity.status(OK).body(sessions);
    }

    // FIX ME: Check the response
    @Transactional(readOnly = true)
    public ResponseEntity<Object> getAllSessionsByDate(@RequestParam(required = false) String date) {
        List<SessionModel> sessions;

        if (date != null) {
            if (!date.matches("\\d{8}")) {
                throw new MethodArgumentTypeMismatchException(date, LocalDate.class, null, null, null);
            }
            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE);
            sessions = sessionRepository.findByDate(parsedDate);

            if (sessions.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(Map.of("message", "No sessions found for this date"));
            }
        } else {
            sessions = sessionRepository.findAll();
        }

        return ResponseEntity.status(OK).body(new ApiResponse<>(sessions));
    }

    /*
    TO DO:
     - Handle error creating a session with a movie that does not exist
     - Handle error creating a session with a cinema that does not exist
     - Handle error creating a session with a date that is in the past
     - Handle error creating a session with a time that is in the past
     - Handle error creating a session at the same time in the same cinema
    */
    @Transactional
    public ResponseEntity<Object> createSession(@Valid @RequestBody SessionModel session) {
        sessionRepository.save(session);
        return ResponseEntity.status(201).body(session);
    }

    // FIX ME: Check the response and the comment block
    @Transactional
    public ResponseEntity<Object> updateSession(@PathVariable Long id, @RequestBody SessionModel session) {
        Optional<SessionModel> sessionOptional = sessionRepository.findById(id);

        if (sessionOptional.isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).body(Map.of("message", "Session not found"));
        }

        SessionModel sessionToUpdate = sessionOptional.get();
//        sessionToUpdate.setMovieId(session.getMovieId());
//        sessionToUpdate.setCinemaId(session.getCinemaId());
        sessionToUpdate.setDate(session.getDate());
        sessionToUpdate.setTime(session.getTime());

        sessionRepository.save(sessionToUpdate);

        return ResponseEntity.status(OK).body(new ApiResponse<>(sessionToUpdate));
    }

    @Transactional
    public ResponseEntity<Object> deleteSession(@PathVariable Long id) {
        Optional<SessionModel> session = sessionRepository.findById(id);

        if (session.isEmpty()) {
            return ResponseEntity.status(NOT_FOUND).body(Map.of("message", "Session not found"));
        }

        sessionRepository.deleteById(id);

        return ResponseEntity.status(NO_CONTENT).body(null);
    }
}
