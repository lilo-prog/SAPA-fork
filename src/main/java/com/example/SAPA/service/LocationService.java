package com.example.SAPA.service;

import com.example.SAPA.Repositories.LocationRepository;
import com.example.SAPA.exceptions.EmptyCollectionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public void validateLocations() throws EmptyCollectionException {
        if(locationRepository.count()==0) throw new EmptyCollectionException("No hay ubicaciones");
    }

}
