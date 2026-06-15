package com.example.SAPA.service;

import com.example.SAPA.DTOs.Response.fda.FdaResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class FdaService {

    private final RestTemplate restTemplate;

    public FdaResponseDTO searchForMedicationByName(String name){
        String url = "https://api.fda.gov/drug/label.json?search=openfda.brand_name:\"" +name+ "\"&limit=1";

        try{
            return restTemplate.getForObject(url, FdaResponseDTO.class);

        }catch (Exception e){
            System.out.println("Error al conectar con openFDA: "+e.getMessage());
            return null;
        }
    }
}
