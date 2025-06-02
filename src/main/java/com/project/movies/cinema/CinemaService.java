package com.project.movies.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// FIX ME: Change exception message when cinema id is not int
@Service
public class CinemaService {

    @Autowired
    private ICinemaRepository cinemaRepository;

    @Transactional(readOnly = true)
    public List<CinemaModel> getAllCinemas() {
        return cinemaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getCinemaById(@PathVariable Long id) {
        CinemaModel cinema = cinemaRepository.findById(id).orElse(null);

        if (cinema == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Cinema not found");
            return ResponseEntity.status(NOT_FOUND).body(response);
        }

        return ResponseEntity.status(OK).body(cinema);
    }

    // TO DO: Implement validation for the cinema before saving it
    @Transactional
    public ResponseEntity<CinemaModel> createCinema(@RequestBody CinemaModel cinema) {
        cinemaRepository.save(cinema);
        return ResponseEntity.status(CREATED).body(cinemaRepository.save(cinema));
    }

    @Transactional
    public ResponseEntity<Object> updateCinema(@PathVariable Long id, @RequestBody CinemaModel cinema) {
        CinemaModel cinemaToUpdate = cinemaRepository.findById(id).orElse(null);

        if (cinemaToUpdate == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Cinema not found");
            return ResponseEntity.status(NOT_FOUND).body(response);
        }

        cinemaToUpdate.setLayout(cinema.getLayout());
        cinemaToUpdate.setCapacity(cinema.getCapacity());

        CinemaModel updatedCinema = cinemaRepository.save(cinemaToUpdate);

        return ResponseEntity.status(OK).body(updatedCinema);
    }
}
