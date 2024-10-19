package com.example.hospitalmicroservice.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class HospitalResponse {
    private Long id;

    private String name;

    private String address;

    private String contactNumber;

}
