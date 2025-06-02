package com.project.movies.utils;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.project.movies.session.SessionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Maneja validaciones de @NotNull y otras anotaciones de validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(400).body(
                Map.of("message", Objects.requireNonNull(ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage()))
        );
    }

    // Maneja errores de formato incorrecto en el JSON (Ej: fecha mal escrita o un valor inválido en status)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleInvalidJson(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException invalidFormatException) {
            Class<?> targetType = invalidFormatException.getTargetType();

            if (targetType != null && targetType.equals(SessionModel.Status.class)) {
                return ResponseEntity.status(400).body(Map.of("message", "Invalid status value. Use 'active' or 'archived'"));
            }

            if (targetType != null && targetType.equals(LocalDate.class)) {
                return ResponseEntity.status(400).body(Map.of("message", "Invalid date format. Use YYYY-MM-DD"));
            }
        }

        return ResponseEntity.status(400).body(Map.of("message", "Invalid request body"));
    }

    // Maneja errores de formato incorrecto en la fecha en el parámetro de la URL
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleInvalidDateFormat(MethodArgumentTypeMismatchException ex) {
        if (ex.getValue() instanceof String) {
            return ResponseEntity.status(400).body(Map.of("message", "Invalid date format. Use YYYYMMDD"));
        }
        return ResponseEntity.status(400).body(Map.of("message", "Invalid request body"));
    }
}
