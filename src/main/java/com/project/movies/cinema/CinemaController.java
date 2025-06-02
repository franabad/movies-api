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
    private CinemaService cinemaService;

    @GetMapping
    public List<CinemaModel> getAllCinemas() {
        return cinemaService.getAllCinemas();
    }

    @GetMapping("/{id}")
    public CinemaModel getCinemaById(@PathVariable Long id) {
        return cinemaService.getCinemaById(id);
    }

    @PostMapping
    public ResponseEntity<CinemaModel> createCinema(@RequestBody CinemaModel cinema) {
        return cinemaService.createCinema(cinema);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateCinema(@PathVariable Long id, @RequestBody CinemaModel cinema) {
        return cinemaService.updateCinema(id, cinema);
    }
}
