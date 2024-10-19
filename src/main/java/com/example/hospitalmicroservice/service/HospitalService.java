package com.example.hospitalmicroservice.service;

import com.example.hospitalmicroservice.dto.HospitalRequest;
import com.example.hospitalmicroservice.dto.HospitalResponse;
import com.example.hospitalmicroservice.dto.RoomsResponse;
import com.example.hospitalmicroservice.dto.TokenValidationResponse;
import com.example.hospitalmicroservice.exception.HospitalNotFoundException;
import com.example.hospitalmicroservice.exception.InvalidDataException;
import com.example.hospitalmicroservice.model.HospitalEntity;
import com.example.hospitalmicroservice.repository.HospitalRepository;
import com.example.hospitalmicroservice.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HospitalService {
    private final HospitalRepository hospitalRepository;
    private final RabbitService rabbitService;

    public List<HospitalResponse> getHospitals(Integer from, Integer count) {
        if (from == null || from < 0) {
            throw new InvalidDataException("Параметр from должен быть неотрицательным.");
        }
        if (count == null || count <= 0) {
            throw new InvalidDataException("Параметр count должен быть больше 0");
        }

        List<HospitalResponse> hospitals = hospitalRepository.findAll().stream()
                .skip(from)
                .limit(count)
                .map(hospital -> HospitalResponse.builder()
                        .id(hospital.getId())
                        .address(hospital.getAddress())
                        .contactNumber(hospital.getContactNumber())
                        .name(hospital.getName())
                        .build())
                .collect(Collectors.toList());

        if (hospitals.isEmpty()) {
            throw new HospitalNotFoundException("Больницы по заданным критериям не найдены");
        }

        return hospitals;
    }
    private HospitalEntity getHospitalById(Long hospitalId){
        HospitalEntity hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new HospitalNotFoundException("Больница с данным id не найдена"));
        return hospital;
    }

    public HospitalResponse getHospital(Long hospitalId) {
        HospitalEntity hospital = getHospitalById(hospitalId);

        return HospitalResponse.builder()
                .address(hospital.getAddress())
                .contactNumber(hospital.getContactNumber())
                .name(hospital.getName())
                .build();
    }


    public RoomsResponse getRooms(Long hospitalId){
        HospitalEntity hospital = getHospitalById(hospitalId);

        return RoomsResponse.builder()
                .rooms(hospital.getRooms().stream().toList())
                .build();
    }

    private boolean isAdmin(HttpServletRequest header){
        TokenValidationResponse response = rabbitService.sendRoleValidationRequest(JwtUtil.extractToken(header));
        return response.isValid();
    }

    public void createHospital(HospitalRequest request, HttpServletRequest header){
        if(!isAdmin(header)){
            throw new InvalidDataException("Token is expired or invalid");
        }
        //TODO: Доделать создание госпиталя
    }
}
