package com.example.SAPA.entities;

import com.example.SAPA.enums.ReportedContentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

@Entity
@Table(name = "reports")
public class ReportEntity {
    // Entidad de reporte.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reported_by", nullable = false)
    private UserEntity reportedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportedContentType contentType;

    @Column(nullable = false)
    private Long contentId;

    @Column(nullable = false, length = 1000)
    private String reason;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean reviewed;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.reviewed = false;
    }
}
