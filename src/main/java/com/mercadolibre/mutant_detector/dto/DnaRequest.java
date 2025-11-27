package com.mercadolibre.mutant_detector.dto;

import com.mercadolibre.mutant_detector.validation.ValidDnaSequence;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DnaRequest {

    @ValidDnaSequence
    private String[] dna;
}