package com.example.SAPA.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LocationDTOResponse {
    private Long id;
    private String googlePlaceId;
    private String country;
    private String city;
    private String province;

    private Double latitude;
    private Double longitude;
}
