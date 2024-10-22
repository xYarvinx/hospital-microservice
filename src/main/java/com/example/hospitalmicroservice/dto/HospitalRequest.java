package com.example.hospitalmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Data
@Schema(description = "Модель запроса для создания или обновления информации о больнице")
public class HospitalRequest {
    @Schema(description = "Название больницы", example = "Городская больница №1", required = true)
    private String name;

    @Schema(description = "Адрес больницы", example = "ул. Ленина, д. 1", required = true)
    private String address;

    @Schema(description = "Контактный телефон больницы", example = "+7 (495) 123-45-67", format = "phone", required = true)
    private String contactPhone;

    @Schema(description = "Список кабинетов в больнице", example = "[\"101\", \"102\"]", required = true)
    private Set<String> rooms;
}
