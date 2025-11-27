package com.mercadolibre.mutant_detector.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "dna_records",
        indexes = {
                @Index(name = "idx_dna_hash", columnList = "dnaHash"),
                @Index(name = "idx_is_mutant", columnList = "isMutant")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DnaRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dna_hash", unique = true, nullable = false, length = 64)
    private String dnaHash;

    @Column(name = "is_mutant", nullable = false)
    private Boolean isMutant;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public DnaRecord(String dnaHash, Boolean isMutant) {
        this.dnaHash = dnaHash;
        this.isMutant = isMutant;
        this.createdAt = LocalDateTime.now();
    }
}