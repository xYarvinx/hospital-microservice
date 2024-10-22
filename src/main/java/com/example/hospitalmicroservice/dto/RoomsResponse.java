package com.example.hospitalmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Ответ, содержащий список номеров кабинетов")
public class RoomsResponse {
    @Schema(description = "Список номеров кабинетов", example = "[\"101\", \"102\"]", required = true)
    private List<String> rooms;
}
