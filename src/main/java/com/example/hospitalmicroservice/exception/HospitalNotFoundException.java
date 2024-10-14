package com.example.hospitalmicroservice.exception;

import org.springframework.http.HttpStatus;

public class HospitalNotFoundException extends ApiException{
    public HospitalNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
