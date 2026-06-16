package com.example.SAPA.DTOs.Request;

import com.example.SAPA.enums.ReportedContentType;

public record CreateReportRequestDTO(
        ReportedContentType contentType,
        Long contentId,
        String reason
) {}