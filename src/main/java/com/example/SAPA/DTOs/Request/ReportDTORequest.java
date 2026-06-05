package com.example.SAPA.DTOs.Request;

import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.enums.ReportedContentType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTORequest {
    private Long user_id;

    private ReportedContentType contentType;

    private Long contentId;

    private String reason;

    private LocalDateTime createdAt;

    private boolean reviewed;

}
