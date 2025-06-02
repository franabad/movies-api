package com.project.movies.movie;

import com.project.movies.utils.PropertyNames;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// TODO: Add exception handling when you create a movie that already exists

@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private IMovieRepository movieRepository;

    @GetMapping
    public List<MovieModel> getMovies() {
        return movieRepository.findAll();
    }

    @GetMapping("/{id}")
    public Object getMovieById(@PathVariable String id) {
        Optional<MovieModel> movie = movieRepository.findById(id);

        if (movie.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Movie not found");
            return ResponseEntity.status(404).body(response);
        }

        return ResponseEntity.status(200).body(movie);
    }

    @PostMapping
    public ResponseEntity<MovieModel> createMovie(@RequestBody MovieModel movie) {
        return ResponseEntity.status(201).body(movieRepository.save(movie));
    }

    @PatchMapping("/{id}")
    public Object updateMovie(@PathVariable String id, @RequestBody MovieModel movie) {
        Optional<MovieModel> movieOptional = movieRepository.findById(id);

        if (movieOptional.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Movie not found");
            return ResponseEntity.status(404).body(response);
        }

        MovieModel movieToUpdate = movieOptional.get();

        BeanUtils.copyProperties(movie, movieToUpdate, PropertyNames.getNullOrDefaultPropertyNames(movie));

        MovieModel updatedMovie = movieRepository.save(movieToUpdate);

        return ResponseEntity.status(202).body(updatedMovie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMovie(@PathVariable String id) {
        Optional<MovieModel> movie = movieRepository.findById(id);

        if (movie.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Movie not found");
            return ResponseEntity.status(404).body(response);
        }

        movieRepository.deleteById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Movie deleted successfully");
        response.put("deletedMovie", movie);

        return ResponseEntity.status(200).body(response);
    }
}
