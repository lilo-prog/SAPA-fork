package com.example.SAPA.service;

import com.example.SAPA.Repositories.LocationRepository;
import com.example.SAPA.exceptions.EmptyCollectionException;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    public LocationService(LocationRepository locationRepository){
        this.locationRepository=locationRepository;
    }
    public void validateLocations() throws EmptyCollectionException {
        if(locationRepository.count()==0) throw new EmptyCollectionException("No hay ubicaciones");
    }

}
