package com.example.hospitalmicroservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class HospitalRequest {
    private String name;
    private String address;
    private String ContactPhone;
    private Set<String> rooms;
}
