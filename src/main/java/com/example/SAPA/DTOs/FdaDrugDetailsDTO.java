package com.example.SAPA.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FdaDrugDetailsDTO {
    @JsonProperty("brand_name")
    private List<String> brandName;
    @JsonProperty("generic_name")
    private List<String> genericName;


}
