package com.example.SAPA.Models.Entities;

import com.example.SAPA.Models.LocationEntity;
import com.example.SAPA.enums.AccountStatus;
import com.example.SAPA.enums.UserCategory;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "user")
public class UserEntity {
    // Entidad de usuario.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserCategory role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
