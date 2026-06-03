package com.example.SAPA.service;

import com.example.SAPA.DTOs.FdaResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FdaService {
    //Atributos.
    private final RestTemplate restTemplate = new RestTemplate();

    //Métodos
    public FdaResponseDTO searchForMedicationByName(String name){
        String url = "https://api.fda.gov/drug/label.json?search=openfda.brand_name:\"" +name+ "\"&limit=5";

        try{
            return restTemplate.getForObject(url, FdaResponseDTO.class);

        }catch (Exception e){
            System.out.println("Error al conectar con openFDA: "+e.getMessage());
            return null;
        }
    }
}
