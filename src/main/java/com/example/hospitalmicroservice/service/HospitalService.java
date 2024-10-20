package com.example.hospitalmicroservice.service;

import com.example.hospitalmicroservice.dto.HospitalRequest;
import com.example.hospitalmicroservice.dto.HospitalResponse;
import com.example.hospitalmicroservice.dto.RoomsResponse;
import com.example.hospitalmicroservice.dto.TokenValidationResponse;
import com.example.hospitalmicroservice.exception.HospitalExistException;
import com.example.hospitalmicroservice.exception.HospitalNotFoundException;
import com.example.hospitalmicroservice.exception.InvalidDataException;
import com.example.hospitalmicroservice.model.HospitalEntity;
import com.example.hospitalmicroservice.repository.HospitalRepository;
import jakarta.transaction.Transactional;
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

    private boolean isAdmin(String token){
        TokenValidationResponse response = rabbitService.sendRoleValidationRequest(token.substring(7));
        return response.isValid();
    }

    public void createHospital(HospitalRequest request, String token){
        if(!isAdmin(token)){
            throw new InvalidDataException("Token is expired or invalid");
        }

        if (hospitalRepository.existsByName(request.getName())) {
            throw new HospitalExistException("Больница с таким названием уже существует.");
        }

        try {
            HospitalEntity hospital =  HospitalEntity.builder()
                    .address(request.getAddress())
                    .name(request.getName())
                    .contactNumber(request.getContactPhone())
                    .rooms(request.getRooms())
                    .build();

            hospitalRepository.save(hospital);
        } catch (Exception e) {
            throw new InvalidDataException("Ошибка в данных");
        }
    }

    @Transactional
    public void updateHosptial(Long hospitalId, HospitalRequest request, String token){
        if(!isAdmin(token)){
            throw new InvalidDataException("Invalid or expired token");
        }

        HospitalEntity hospital = getHospitalById(hospitalId);
        if (!hospital.getName().equals(request.getName()) && hospitalRepository.existsByName(request.getName())) {
            throw new HospitalExistException("Больница с таким названием уже существует.");
        }

        try {
            hospital.setAddress(request.getAddress());
            hospital.setName(request.getName());
            hospital.setContactNumber(request.getContactPhone());
            hospital.setRooms(request.getRooms());

        } catch (Exception e) {
            throw new InvalidDataException("Ошибка в данных");
        }

    }

    public void deleteById(Long hospitalId, String token) {
        if(!isAdmin(token)){
            throw new InvalidDataException("Invalid or expired token");
        }

        HospitalEntity hospital = getHospitalById(hospitalId);
        hospitalRepository.delete(hospital);
    }
}
