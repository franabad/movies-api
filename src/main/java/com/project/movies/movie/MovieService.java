package com.project.movies.movie;

import com.project.movies.utils.PropertyNames;
import org.springframework.beans.BeanUtils;
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
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private IMovieRepository movieRepository;

    @Transactional(readOnly = true)
    public List<MovieModel> getAllMovies() {
        return movieRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Object getMovieById(@PathVariable String id) {
        Optional<MovieModel> movie = movieRepository.findById(id);

        if (movie.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Movie not found");
            return ResponseEntity.status(NOT_FOUND).body(response);
        }

        return ResponseEntity.status(OK).body(movie);
    }

    // TO DO: Add exception handling when you create a movie that already exists
    @Transactional
    public ResponseEntity<MovieModel> createMovie(@RequestBody MovieModel movie) {
        return ResponseEntity.status(CREATED).body(movieRepository.save(movie));
    }

    @Transactional
    public Object updateMovie(@PathVariable String id, @RequestBody MovieModel movie) {
        Optional<MovieModel> movieOptional = movieRepository.findById(id);

        if (movieOptional.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Movie not found");
            return ResponseEntity.status(NOT_FOUND).body(response);
        }

        MovieModel movieToUpdate = movieOptional.get();

        BeanUtils.copyProperties(movie, movieToUpdate, PropertyNames.getNullOrDefaultPropertyNames(movie));

        MovieModel updatedMovie = movieRepository.save(movieToUpdate);

        return ResponseEntity.status(OK).body(updatedMovie);
    }

    @Transactional
    public ResponseEntity<Object> deleteMovieById(@PathVariable String id) {
        Optional<MovieModel> movie = movieRepository.findById(id);

        if (movie.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Movie not found");
            return ResponseEntity.status(NOT_FOUND).body(response);
        }

        movieRepository.deleteById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Movie deleted successfully");
        response.put("deletedMovie", movie);

        return ResponseEntity.status(OK).body(response); // Puede ser también un NO_CONTENT (204) si no se quiere devolver nada en el cuerpo de la respuesta
    }
}
