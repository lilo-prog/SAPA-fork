package com.example.SAPA.service;

import com.example.SAPA.DTOs.ChatDTO;
import com.example.SAPA.Models.Chat.ConversationEntity;
import com.example.SAPA.Models.Chat.MessageEntity;
import com.example.SAPA.Models.Entities.DoctorEntity;
import com.example.SAPA.Models.Entities.PatientEntity;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.Repositories.ConversationRepository;
import com.example.SAPA.Repositories.DoctorRepository;
import com.example.SAPA.Repositories.MessageRepository;
import com.example.SAPA.Repositories.PatientRepository;
import com.example.SAPA.mappers.MessageMapper;
import com.example.SAPA.security.entities.CredentialEntity;
import com.example.SAPA.security.repositories.CredentialRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final CredentialRepository credentialRepository;
    private final MessageMapper messageMapper;


    private UserEntity getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        CredentialEntity credential = credentialRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario autenticado no encontrado"));

        return credential.getUser();
    }

    public String resolveSenderName(UserEntity sender) {
        return patientRepository.findByUser(sender)
                .map(p -> p.getFirstName() + " " + p.getLastName())

                .orElseGet(() -> doctorRepository.findByUser(sender)
                        .map(d -> "Dr. " + d.getFirstName() + " " + d.getLastName())
                        .orElse(sender.getEmail()));
    }

    public ConversationEntity createConversation(PatientEntity patient, DoctorEntity doctor) {
        return conversationRepository.findByPatientAndDoctor(patient, doctor)
                .orElseGet(() -> {
                    ConversationEntity conversation = ConversationEntity.builder()
                            .patient(patient)
                            .doctor(doctor)
                            .build();
                    return conversationRepository.save(conversation);
                });
    }

    public List<ChatDTO.ConversationSummary> getMyConversations() {
        UserEntity user = getAuthenticatedUser();

        boolean isPatient = patientRepository.findByUser(user).isPresent();

        List<ConversationEntity> conversations;
        if (isPatient) {
            PatientEntity patient = patientRepository.findByUser(user).get();
            conversations = conversationRepository.findByPatient(patient);
        } else {
            DoctorEntity doctor = doctorRepository.findByUser(user)
                    .orElseThrow(() -> new EntityNotFoundException("No se encontró perfil asociado al usuario"));
            conversations = conversationRepository.findByDoctor(doctor);
        }

        return conversations.stream().map(conv -> {
            List<MessageEntity> messages = messageRepository
                    .findByConversationOrderBySentAtAsc(conv);

            // Nombre del otro participante según quién es el autenticado
            String otherName = isPatient
                    ? "Dr. " + conv.getDoctor().getFirstName() + " " + conv.getDoctor().getLastName()
                    : conv.getPatient().getFirstName() + " " + conv.getPatient().getLastName();

            String lastMessage = messages.isEmpty()
                    ? "Sin mensajes aún"
                    : messages.get(messages.size() - 1).getContent();

            java.time.LocalDateTime lastMessageAt = messages.isEmpty()
                    ? conv.getCreatedAt()
                    : messages.get(messages.size() - 1).getSentAt();

            return new ChatDTO.ConversationSummary(
                    conv.getId(),
                    otherName,
                    lastMessage,
                    lastMessageAt,
                    conv.getCreatedAt()
            );
        }).toList();
    }

    public List<ChatDTO.MessageResponse> getConversationHistory(Long conversationId) {
        UserEntity user = getAuthenticatedUser();

        ConversationEntity conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new EntityNotFoundException("Conversación no encontrada con id: " + conversationId));

        validateParticipant(user, conversation);

        return messageRepository.findByConversationOrderBySentAtAsc(conversation)
                .stream()
                .map(message -> {
                    String senderName = resolveSenderName(message.getSender());
                    return messageMapper.toResponse(message, senderName);
                })
                .toList();
    }

    public void validateParticipant(UserEntity user, ConversationEntity conversation) {
        boolean isPatient = conversation.getPatient().getUser().getId().equals(user.getId());
        boolean isDoctor = conversation.getDoctor().getUser().getId().equals(user.getId());

        if (!isPatient && !isDoctor) {
            throw new RuntimeException("No tenés permiso para acceder a esta conversación");
        }
    }
}