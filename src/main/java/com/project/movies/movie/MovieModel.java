package com.project.movies.movie;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.movies.utils.UUIDGenerator;
import jakarta.persistence.*;

@Entity
@Table(name = "movies")
public class MovieModel {

    @Id
    private String id;

    private String title;
    private String description;
    private int year;
    private int votes;
    private double rating;
    private int duration;
    private String genre;

    @Column(name = "cover_url")
    @JsonProperty("coverUrl")
    private String coverUrl;

    @PrePersist
    public void generateId() {
        this.id = UUIDGenerator.generateUUID();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
