package com.example.hospitalmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenValidationRequest {
    private String token;
    private String correlationId;
}
