package com.example.SAPA.DTOs;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FdaResponseDTO {
    private List<FdaResultDTO> results;
}
