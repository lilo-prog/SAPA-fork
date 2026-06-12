package com.example.SAPA.DTOs.Response.fda;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@NoArgsConstructor
public class FdaDrugDetailsDTO {

    @JsonProperty("brand_name")
    private List<String> brandName;

    @JsonProperty("generic_name")
    private List<String> genericName;
}
