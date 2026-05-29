package com.example.SAPA.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FdaResponseDTO {

    private List<FdaResultDTO> results;
}
