package com.example.SAPA.DTOs.Response.fda;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@NoArgsConstructor
public class FdaResponseDTO {

    private List<FdaResultDTO> results;
}
