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

@Service
public class CinemaService {

    @Autowired
    private ICinemaRepository cinemaRepository;

    @Transactional(readOnly = true)
    public List<CinemaModel> getAllCinemas() {
        return cinemaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CinemaModel getCinemaById(@PathVariable Long id) {
        return cinemaRepository.findById(id).orElse(null);
    }

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
