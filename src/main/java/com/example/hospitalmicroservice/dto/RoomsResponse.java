package com.example.hospitalmicroservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomsResponse {
    private List<String> rooms;
}
