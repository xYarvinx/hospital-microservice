package com.example.hospitalmicroservice.service;

import com.example.hospitalmicroservice.dto.HosptialResponse;
import com.example.hospitalmicroservice.exception.HospitalNotFoundException;
import com.example.hospitalmicroservice.exception.InvalidDataException;
import com.example.hospitalmicroservice.repository.HospitalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HospitalService {
    private final HospitalRepository hospitalRepository;

    public List<HosptialResponse> getHospitals(Integer from, Integer count) {
        if (from == null || from < 0) {
            throw new InvalidDataException("Параметр from должен быть неотрицательным.");
        }
        if (count == null || count <= 0) {
            throw new InvalidDataException("Параметр count должен быть больше 0");
        }

        List<HosptialResponse> hospitals = hospitalRepository.findAll().stream()
                .skip(from)
                .limit(count)
                .map(hospital -> HosptialResponse.builder()
                        .id(hospital.getId())
                        .build())
                .collect(Collectors.toList());

        if (hospitals.isEmpty()) {
            throw new HospitalNotFoundException("Больницы по заданным критериям не найдены");
        }

        return hospitals;
    }
}
