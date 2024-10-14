package com.example.hospitalmicroservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

@Builder
@Data
@Setter
@Accessors(chain = true)
public class ErrorResponse {
    private Error error;
}
