package com.example.SAPA.Models.MedicalRecord;

import com.example.SAPA.enums.TimeLapse;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "duration")
public class DurationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="duration_id")
    private Long id;
    private int length;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeLapse timeLapse;
}
