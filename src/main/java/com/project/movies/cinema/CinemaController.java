package com.project.movies.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequestMapping("/api/cinemas")
public class CinemaController {

    @Autowired
    private ICinemaRepository cinemaRepository;

    @GetMapping
    public List<CinemaModel> getCinemas() {
        return cinemaRepository.findAll();
    }

    @GetMapping("/{id}")
    public CinemaModel getCinemaById(@PathVariable Long id) {
        return cinemaRepository.findById(id).orElse(null);
    }

    @PostMapping
    public ResponseEntity<CinemaModel> createCinema(@RequestBody CinemaModel cinema) {
        return ResponseEntity.status(201).body(cinemaRepository.save(cinema));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateCinema(@PathVariable Long id, @RequestBody CinemaModel cinema) {
        CinemaModel cinemaToUpdate = cinemaRepository.findById(id).orElse(null);

        if (cinemaToUpdate == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Cinema not found");
            return ResponseEntity.status(404).body(response);
        }

        cinemaToUpdate.setLayout(cinema.getLayout());
        cinemaToUpdate.setCapacity(cinema.getCapacity());

        CinemaModel updatedCinema = cinemaRepository.save(cinemaToUpdate);

        return ResponseEntity.status(200).body(updatedCinema);
    }
}
