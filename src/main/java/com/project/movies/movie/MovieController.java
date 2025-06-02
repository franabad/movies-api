package com.project.movies.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public List<MovieModel> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMovieById(@PathVariable String id) {
        return movieService.getMovieById(id);
    }

    @PostMapping
    public ResponseEntity<MovieModel> createMovie(@RequestBody MovieModel movie) {
        return movieService.createMovie(movie);
    }

    @PatchMapping("/{id}")
    public Object updateMovie(@PathVariable String id, @RequestBody MovieModel movie) {
        return movieService.updateMovie(id, movie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMovie(@PathVariable String id) {
        return movieService.deleteMovieById(id);
    }
}
