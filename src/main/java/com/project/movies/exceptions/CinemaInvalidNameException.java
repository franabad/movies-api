package com.project.movies.exceptions;

public class CinemaInvalidNameException extends RuntimeException{
    public CinemaInvalidNameException(String message){
        super(message);
    }
}
