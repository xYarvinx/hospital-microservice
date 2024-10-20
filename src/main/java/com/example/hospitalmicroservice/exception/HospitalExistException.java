package com.example.hospitalmicroservice.exception;

import org.springframework.http.HttpStatus;

public class HospitalExistException extends ApiException{
    public HospitalExistException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
