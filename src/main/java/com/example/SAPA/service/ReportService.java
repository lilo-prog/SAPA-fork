package com.example.SAPA.service;

import com.example.SAPA.DTOs.Request.CreateReportRequestDTO;
import com.example.SAPA.DTOs.Response.ReportResponseDTO;
import com.example.SAPA.Models.Entities.UserEntity;
import com.example.SAPA.Models.ReportEntity;
import com.example.SAPA.Repositories.ReportRepository;
import com.example.SAPA.mappers.ReportMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final UserContextService userContext;


    public ReportResponseDTO createReport(CreateReportRequestDTO request) {
        UserEntity user = userContext.getAuthenticatedUser();

        boolean alreadyReported = reportRepository.existsByReportedByIdAndContentTypeAndContentId(
                user.getId(), request.contentType(), request.contentId());

        if (alreadyReported) {
            throw new RuntimeException("Ya reportaste este contenido anteriormente");
        }

        ReportEntity report = ReportEntity.builder()
                .reportedBy(user)
                .contentType(request.contentType())
                .contentId(request.contentId())
                .reason(request.reason())
                .build();

        ReportEntity saved = reportRepository.save(report);
        String reportedByName = userContext.resolveName(user);
        return reportMapper.toResponse(saved, reportedByName);
    }


    public List<ReportResponseDTO> getUnreviewed() {
        return reportRepository.findByReviewedFalse()
                .stream()
                .map(r -> reportMapper.toResponse(r, userContext.resolveName(r.getReportedBy())))
                .toList();
    }


    public ReportResponseDTO markAsReviewed(Long reportId) {
        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Reporte no encontrado con id: " + reportId));

        report.setReviewed(true);
        ReportEntity saved = reportRepository.save(report);
        return reportMapper.toResponse(saved, userContext.resolveName(saved.getReportedBy()));
    }
}
