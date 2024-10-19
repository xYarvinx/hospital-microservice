package com.example.hospitalmicroservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class HospitalRequest {
    private String name;
    private String address;
    private String ContactPhone;
    private List<String> rooms;
}
