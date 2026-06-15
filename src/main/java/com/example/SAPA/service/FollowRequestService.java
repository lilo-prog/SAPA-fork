package com.example.SAPA.service;

import com.example.SAPA.Models.Chat.ConversationEntity;
import com.example.SAPA.Models.Entities.DoctorEntity;
import com.example.SAPA.Models.Entities.PatientEntity;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.Models.FollowRequestEntity;
import com.example.SAPA.Repositories.DoctorRepository;
import com.example.SAPA.Repositories.FollowRequestRepository;
import com.example.SAPA.enums.FollowRequestStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowRequestService {

    private final FollowRequestRepository followRequestRepository;
    private final DoctorRepository doctorRepository;
    private final ConversationService conversationService;
    private final MessageService messageService;
    private final UserContextService userContextService;

    private DoctorEntity getAuthenticatedDoctor() {
        UserEntity user = userContextService.getAuthenticatedUser();
        return doctorRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el médico asociado al usuario autenticado"));
    }

    public FollowRequestEntity create(Long doctorId) {
        PatientEntity patient = userContextService.getAuthenticatedPatient();

        DoctorEntity doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el médico con id: " + doctorId));

        boolean alreadyExists = followRequestRepository
                .existsByPatientAndDoctorAndStatus(patient, doctor, FollowRequestStatus.PENDING);

        if (alreadyExists) {
            throw new RuntimeException("Ya existe una solicitud de seguimiento pendiente con este médico");
        }

        FollowRequestEntity request = FollowRequestEntity.builder()
                .patient(patient)
                .doctor(doctor)
                .build();

        return followRequestRepository.save(request);
    }

    public FollowRequestEntity approve(Long id) {
        DoctorEntity authenticatedDoctor = getAuthenticatedDoctor();

        FollowRequestEntity request = followRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada con id: " + id));

        if (!request.getDoctor().getId().equals(authenticatedDoctor.getId())) {
            throw new RuntimeException("No tenés permiso para aprobar esta solicitud");
        }

        request.setStatus(FollowRequestStatus.APPROVED);
        request.setRespondedAt(LocalDateTime.now());
        FollowRequestEntity saved = followRequestRepository.save(request);

        ConversationEntity conversation = conversationService.createConversation(
                request.getPatient(),
                request.getDoctor()
        );

        messageService.sendSystemMessage(
                conversation,
                "¡La solicitud de seguimiento fue aprobada! Ya pueden comunicarse."
        );

        return saved;
    }

    public FollowRequestEntity reject(Long id) {
        DoctorEntity authenticatedDoctor = getAuthenticatedDoctor();

        FollowRequestEntity request = followRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada con id: " + id));

        if (!request.getDoctor().getId().equals(authenticatedDoctor.getId())) {
            throw new RuntimeException("No tenés permiso para rechazar esta solicitud");
        }

        request.setStatus(FollowRequestStatus.REJECTED);
        request.setRespondedAt(LocalDateTime.now());
        return followRequestRepository.save(request);
    }

    public FollowRequestEntity dissolve(Long id) {
        UserEntity authenticatedUser = userContextService.getAuthenticatedUser();

        FollowRequestEntity request = followRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada con id: " + id));

        boolean isPatient = request.getPatient().getUser().getId().equals(authenticatedUser.getId());
        boolean isDoctor = request.getDoctor().getUser().getId().equals(authenticatedUser.getId());

        if (!isPatient && !isDoctor) {
            throw new RuntimeException("No tenés permiso para disolver esta solicitud");
        }

        if (!request.getStatus().equals(FollowRequestStatus.APPROVED)) {
            throw new RuntimeException("Solo se puede disolver un seguimiento activo (APPROVED)");
        }

        request.setStatus(FollowRequestStatus.DISOLVED);
        request.setRespondedAt(LocalDateTime.now());
        return followRequestRepository.save(request);
    }

    public List<FollowRequestEntity> getPendingRequests() {
        DoctorEntity doctor = getAuthenticatedDoctor();
        return followRequestRepository.findByDoctorAndStatus(doctor, FollowRequestStatus.PENDING);
    }

    public List<FollowRequestEntity> getSentRequests() {
        PatientEntity patient = userContextService.getAuthenticatedPatient();
        return followRequestRepository.findByPatient(patient);
    }
}
