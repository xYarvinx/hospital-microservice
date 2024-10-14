package com.example.hospitalmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenValidationRequest implements Serializable {
    private String token;
    private String correlationId;
}
