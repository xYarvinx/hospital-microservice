package com.example.hospitalmicroservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidDataException extends ApiException{
    public InvalidDataException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
