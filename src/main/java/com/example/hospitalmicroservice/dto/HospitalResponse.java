package com.example.hospitalmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@Schema(description = "Ответ, содержащий информацию о больнице")
public class HospitalResponse {
    @Schema(description = "Идентификатор больницы", example = "1", required = true)
    private Long id;

    @Schema(description = "Название больницы", example = "Городская больница №1", required = true)
    private String name;

    @Schema(description = "Адрес больницы", example = "ул. Ленина, д. 1", required = true)
    private String address;

    @Schema(description = "Телефон для связи с больницей", example = "+7 (495) 123-45-67", required = true)
    private String contactPhone;

}
