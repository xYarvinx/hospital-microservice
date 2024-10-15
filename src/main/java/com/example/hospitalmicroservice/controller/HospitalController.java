package com.example.hospitalmicroservice.controller;

import com.example.hospitalmicroservice.dto.HosptialResponse;
import com.example.hospitalmicroservice.exception.ControllerExceptionHandler;
import com.example.hospitalmicroservice.service.HospitalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private List<HosptialResponse> getHospitals(@RequestParam Integer from, @RequestParam Integer count) {
        return hospitalService.getHospitals(from, count);
    }
}
