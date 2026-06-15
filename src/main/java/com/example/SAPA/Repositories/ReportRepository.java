package com.example.SAPA.Repositories;

import com.example.SAPA.Models.ReportEntity;
import com.example.SAPA.enums.ReportedContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    List<ReportEntity> findByReviewedFalse();

    List<ReportEntity> findByContentType(ReportedContentType contentType);

    boolean existsByReportedByIdAndContentTypeAndContentId(Long reportedById, ReportedContentType contentType,
                                                           Long contentId);
}
