package com.example.SAPA.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "location")
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String googlePlaceId;

    private String country;
    private String province;
    private String city;

    private Double latitude;
    private Double longitude;

}
