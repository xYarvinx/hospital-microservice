package com.example.hospitalmicroservice.repository;

import com.example.hospitalmicroservice.model.HospitalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<HospitalEntity,Long> {
}
