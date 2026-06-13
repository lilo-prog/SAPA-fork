package com.example.SAPA.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTORequest {
    private Long id;
    private Long user_reporter_id;
    private Long content_id;
    private String contentType;
    private String reason;
    private LocalDateTime created_at;
    private boolean reviewed;
}
