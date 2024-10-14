package com.example.hospitalmicroservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Set;

@Entity
@Table(name = "hospitals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE hospitals SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class HospitalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String address;

    private String contactNumber;

    private Set<String> rooms;

    @Column(nullable = false)
    private Boolean deleted = Boolean.FALSE;
}
