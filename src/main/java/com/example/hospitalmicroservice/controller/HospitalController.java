package com.example.hospitalmicroservice.controller;

import com.example.hospitalmicroservice.dto.*;
import com.example.hospitalmicroservice.exception.ControllerExceptionHandler;
import com.example.hospitalmicroservice.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Hospitals")
@AllArgsConstructor
@ControllerExceptionHandler
@Tag(name = "Hospital Controller", description = "API для управления больницами")
@ApiResponse(responseCode = "40*", description = "Ошибка в запросе",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
@SecurityRequirement(name = "bearerAuth")
public class HospitalController {
    private final HospitalService hospitalService;

    @Operation(summary = "Получить список больниц", description = "Возвращает список больниц с пагинацией. Доступно всем пользователям.")
    @ApiResponse(responseCode = "200", description = "Список больниц успешно получен",
            content = @Content(schema = @Schema(implementation = HospitalResponse.class)))
    @GetMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<HospitalResponse> getHospitals(
            @Parameter(description = "Начальный индекс выборки")
            @RequestParam Integer from,

            @Parameter(description = "Количество записей для выборки")
            @RequestParam Integer count
    ) {
        return hospitalService.getHospitals(from, count);
    }

    @Operation(summary = "Получить информацию о больнице", description = "Возвращает информацию о конкретной больнице по ее идентификатору. Доступно всем пользователям.")
    @ApiResponse(responseCode = "200", description = "Информация о больнице успешно получена",
            content = @Content(schema = @Schema(implementation = HospitalResponse.class)))
    @GetMapping("/{hospitalId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public HospitalResponse getHospitalById(
            @Parameter(description = "Идентификатор больницы")
            @PathVariable Long hospitalId
    ) {
        return hospitalService.getHospital(hospitalId);
    }

    @Operation(summary = "Получить список палат в больнице", description = "Возвращает список палат для указанной больницы. Доступно всем пользователям.")
    @ApiResponse(responseCode = "200", description = "Список палат успешно получен",
            content = @Content(schema = @Schema(implementation = RoomsResponse.class)))
    @GetMapping("/{hospitalId}/Rooms")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RoomsResponse getRoomsById(
            @Parameter(description = "Идентификатор больницы")
            @PathVariable Long hospitalId
    ) {
        return hospitalService.getRooms(hospitalId);
    }

    @Operation(summary = "Создать новую больницу", description = "Добавляет новую больницу в систему. Доступно только администраторам.")
    @ApiResponse(responseCode = "201", description = "Больница успешно создана",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse createHospital(
            @Parameter(description = "Данные новой больницы", required = true)
            @RequestBody @Validated HospitalRequest request,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token
    ) {
        hospitalService.createHospital(request, token);

        return new MessageResponse("Больница успешно добавлена");
    }

    @Operation(summary = "Обновить информацию о больнице", description = "Обновляет данные существующей больницы. Доступно только администраторам.")
    @ApiResponse(responseCode = "202", description = "Данные больницы успешно обновлены",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    @PutMapping("/{hospitalId}")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse updateHospital(
            @Parameter(description = "Идентификатор больницы", example = "1", required = true)
            @PathVariable Long hospitalId,

            @Parameter(description = "Обновленные данные больницы", required = true)
            @RequestBody @Validated HospitalRequest request,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token
    ) {
        hospitalService.updateHosptial(hospitalId, request, token);
        return new MessageResponse("Данные больницы успешно обновленны");
    }

    @Operation(summary = "Удалить больницу", description = "Удаляет больницу из системы. Доступно только администраторам.")
    @ApiResponse(responseCode = "202", description = "Больница успешно удалена",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    @DeleteMapping("/{hospitalId}")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse deleteHospital(
            @Parameter(description = "Идентификатор больницы", example = "1", required = true)
            @PathVariable Long hospitalId,

            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token
    ) {
        hospitalService.deleteById(hospitalId, token);
        return new MessageResponse("Больница успешно удалена");
    }
}
