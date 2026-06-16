package com.example.SAPA.DTOs;

import com.example.SAPA.enums.AttachmentType;
import com.example.SAPA.enums.MessageType;

import java.time.LocalDateTime;
import java.util.List;

public class ChatDTO {

    public record SendMessageRequest(
            String content,
            MessageType type
    ) {}

    public record MessageResponse(
            Long messageId,
            Long conversationId,
            Long senderId,
            String senderName,
            String content,
            MessageType type,
            List<AttachmentResponse> attachments,
            LocalDateTime sentAt
    ) {}

    public record AttachmentResponse(
            Long attachmentId,
            String fileName,
            String fileUrl,
            AttachmentType type,
            LocalDateTime uploadedAt
    ) {}

    public record ConversationSummary(
            Long conversationId,
            String otherParticipantName,
            String lastMessage,
            LocalDateTime lastMessageAt,
            LocalDateTime createdAt
    ) {}
}
