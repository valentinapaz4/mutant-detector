package com.mercadolibre.mutant_detector.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private MutantDetector detector;

    @BeforeEach
    void setUp() {
        detector = new MutantDetector();
    }


    @Test
    @DisplayName("Debe detectar mutante con secuencias horizontal y diagonal")
    void testMutantWithHorizontalAndDiagonal() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        assertTrue(detector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con secuencias verticales")
    void testMutantWithVerticalSequences() {
        String[] dna = {
                "ATGCGA",
                "ATGTGC",
                "ATATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        String[] dna2 = {
                "AAAAGA",
                "ATGTGC",
                "ATATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        assertTrue(detector.isMutant(dna2));
    }

    @Test
    @DisplayName("Debe detectar mutante con ambas diagonales")
    void testMutantWithBothDiagonals() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante en matriz grande 10x10")
    void testMutantLargeDna() {
        String[] dna = {
                "ATGCGAATGC",
                "CAGTGCAGTG",
                "TTATGTTTAT",
                "AGAAGGAGAA",
                "CCCCTACCCC",
                "TCACTGTCAC",
                "ATGCGAATGC",
                "CAGTGCAGTG",
                "TTATGTTTAT",
                "AGAAGGAGAA"
        };
        assertTrue(detector.isMutant(dna));
    }


    @Test
    @DisplayName("No debe detectar mutante con solo una secuencia")
    void testNotMutantWithOneSequence() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };
        assertFalse(detector.isMutant(dna));
    }

    @Test
    @DisplayName("No debe detectar mutante sin secuencias")
    void testNotMutantNoSequences() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TGAC",
                "GCAT"
        };
        assertFalse(detector.isMutant(dna));
    }

    @Test
    @DisplayName("No debe detectar mutante en matriz pequeña 4x4 sin secuencias")
    void testNotMutantSmallDna() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TGAC",
                "GCAT"
        };
        assertFalse(detector.isMutant(dna));
    }


    @Test
    @DisplayName("Debe retornar false para DNA null")
    void testNullDna() {
        assertFalse(detector.isMutant(null));
    }

    @Test
    @DisplayName("Debe retornar false para DNA vacío")
    void testEmptyDna() {
        String[] dna = {};
        assertFalse(detector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe retornar false para matriz no cuadrada")
    void testNonSquareDna() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TGAC"
        };
        assertFalse(detector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe retornar false para caracteres inválidos")
    void testInvalidCharacters() {
        String[] dna = {
                "ATXC",
                "CAGT",
                "TGAC",
                "GCAT"
        };
        assertFalse(detector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe retornar false para matriz muy pequeña (3x3)")
    void testTooSmallDna() {
        String[] dna = {
                "ATG",
                "CAG",
                "TGA"
        };
        assertFalse(detector.isMutant(dna));
    }
}
