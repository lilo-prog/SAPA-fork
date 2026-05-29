package com.example.SAPA.entities;

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
@Table(name = "forums")
public class ForumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private UserEntity createdBy;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // False if deleted (we never hard delete)
    @Column(nullable = false)
    private boolean active;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }
}
