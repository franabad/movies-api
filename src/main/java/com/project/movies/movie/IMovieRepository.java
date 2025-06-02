package com.project.movies.movie;

import org.springframework.data.jpa.repository.JpaRepository;


public interface IMovieRepository extends JpaRepository<MovieModel, String> {
}
