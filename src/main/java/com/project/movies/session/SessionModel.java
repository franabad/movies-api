package com.project.movies.session;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(name = "sessions")
public class SessionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "MovieId is required")
    @Column(name = "movie_id")
    private String movieId;

    @NotNull(message = "CinemaId is required")
    @Column(name = "cinema_id")
    private Long cinemaId;

    @NotNull(message = "Date is required")
    @Column(name = "`date`")
    private LocalDate date;

    @NotNull(message = "Time is required")
    @Column(name = "`time`")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    private Status status = Status.active;

    public enum Status {
        active,
        archived
    }

    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) { this.movieId = movieId; }

    public Long getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Long cinemaId) {
        this.cinemaId = cinemaId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
