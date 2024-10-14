package com.example.hospitalmicroservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenValidationResponse implements Serializable {
    private boolean valid;
    private String correlationId;

}
