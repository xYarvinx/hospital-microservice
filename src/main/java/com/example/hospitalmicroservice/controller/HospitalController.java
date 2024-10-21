package com.example.hospitalmicroservice.controller;

import com.example.hospitalmicroservice.dto.HospitalRequest;
import com.example.hospitalmicroservice.dto.HospitalResponse;
import com.example.hospitalmicroservice.dto.MessageResponse;
import com.example.hospitalmicroservice.dto.RoomsResponse;
import com.example.hospitalmicroservice.exception.ControllerExceptionHandler;
import com.example.hospitalmicroservice.service.HospitalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Hospitals")
@AllArgsConstructor
@ControllerExceptionHandler
public class HospitalController {
    private final HospitalService hospitalService;

    @GetMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    private List<HospitalResponse> getHospitals(@RequestParam Integer from, @RequestParam Integer count) {
        return hospitalService.getHospitals(from, count);
    }

    @GetMapping("/{hospitalId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    private HospitalResponse getHospitalById(@PathVariable Long hospitalId) {
        return hospitalService.getHospital(hospitalId);
    }

    @GetMapping("/{hospitalId}/Rooms")
    @ResponseStatus(HttpStatus.ACCEPTED)
    private RoomsResponse getRoomsById(@PathVariable Long hospitalId) {
        return hospitalService.getRooms(hospitalId);
    }


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    private MessageResponse createHospital(@RequestBody HospitalRequest request, @RequestHeader("Authorization") String token) {
        hospitalService.createHospital(request, token);

        return new MessageResponse("Больница успешно добавлена");
    }

    @PutMapping("/{hospitalId}")
    @ResponseStatus(HttpStatus.OK)
    private MessageResponse updateHospital(@PathVariable Long hospitalId, @RequestBody HospitalRequest request, @RequestHeader("Authorization") String token) {
        hospitalService.updateHosptial(hospitalId, request, token);
        return new MessageResponse("Данные больницы успешно обновленны");
    }

    @DeleteMapping("/{hospitalId}")
    @ResponseStatus(HttpStatus.OK)
    private MessageResponse deleteHospital(@PathVariable Long hospitalId, @RequestHeader("Authorization") String token) {
        hospitalService.deleteById(hospitalId, token);
        return new MessageResponse("Больница успешно удалена");
    }
}
