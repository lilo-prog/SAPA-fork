package com.example.SAPA.entities;

import com.example.SAPA.enums.AttachmentType;
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
@Table(name = "attachments")
public class AttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "message_id", nullable = false)
    private MessageEntity message;

    @Column(nullable = false)
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttachmentType type;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    @PrePersist
    public void prePersist() {
        this.uploadedAt = LocalDateTime.now();
    }
}
