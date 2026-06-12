package com.example.SAPA.service;

import com.example.SAPA.Models.FollowRequestEntity;
import com.example.SAPA.Repositories.FollowRequestRepository;
import com.example.SAPA.enums.FollowRequestStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FollowRequestService {
    //Atributos.
    private final FollowRequestRepository followRequestRepository;

    //Métodos.
    public FollowRequestService(FollowRequestRepository followRequestRepository) {
        this.followRequestRepository = followRequestRepository;
    }

    public FollowRequestEntity create (FollowRequestEntity request){
        return followRequestRepository.save(request);
    }

    public List<FollowRequestEntity> getAll(){
        return followRequestRepository.findAll();
    }

    public FollowRequestEntity approve(Long id){
        FollowRequestEntity request = followRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        request.setStatus(FollowRequestStatus.APPROVED);
        request.setRespondedAt(LocalDateTime.now());

        return followRequestRepository.save(request);
    }

    public FollowRequestEntity reject(Long id){
        FollowRequestEntity request = followRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        request.setStatus(FollowRequestStatus.REJECTED);
        request.setRespondedAt(LocalDateTime.now());

        return followRequestRepository.save(request);
    }

    public FollowRequestEntity dissolve(Long id){
        FollowRequestEntity request = followRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        request.setStatus(FollowRequestStatus.DISOLVED);
        request.setRespondedAt(LocalDateTime.now());

        return followRequestRepository.save(request);
    }

}
