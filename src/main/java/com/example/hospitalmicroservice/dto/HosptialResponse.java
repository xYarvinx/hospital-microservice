package com.example.hospitalmicroservice.dto;

import com.fasterxml.jackson.core.JsonToken;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HosptialResponse {
    private Long id;
}
