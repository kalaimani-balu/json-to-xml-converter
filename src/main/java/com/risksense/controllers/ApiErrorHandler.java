package com.risksense.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

// mimics spring boot's default error message
class ApiError {
    private Date timestamp;
    private int status;
    private String error;
    private String message;

    ApiError(Date timestamp, int status, String error, String message) {
        super();
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

/**
 * REST API response error handler.
 */
@ControllerAdvice
public class ApiErrorHandler {
    /**
     * Handles invalid json inputs
     * @param exception {@link JsonParseException}
     * @return error message
     */
    @ExceptionHandler(value = JsonParseException.class)
    public ResponseEntity<ApiError> jsonParseError(JsonParseException exception) {
        return ResponseEntity
                .badRequest()
                .body(new ApiError(
                        new Date(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid JSON",
                        exception.getMessage())
                );
    }
}
