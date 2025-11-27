package com.mercadolibre.mutant_detector.service;

import com.mercadolibre.mutant_detector.dto.StatsResponse;
import com.mercadolibre.mutant_detector.repository.DnaRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private StatsService statsService;

    @Test
    @DisplayName("Debe calcular ratio correctamente cuando hay mutantes y humanos")
    void testGetStats_CalculateRatioCorrectly() {
        // Given
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(40L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(100L);

        // When
        StatsResponse stats = statsService.getStats();

        // Then
        assertEquals(40, stats.getCountMutantDna());
        assertEquals(100, stats.getCountHumanDna());
        assertEquals(0.4, stats.getRatio(), 0.01);
    }

    @Test
    @DisplayName("Debe retornar ratio 0 cuando no hay mutantes")
    void testGetStats_ReturnZeroRatio_WhenNoMutants() {
        // Given
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(0L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(100L);

        // When
        StatsResponse stats = statsService.getStats();

        // Then
        assertEquals(0, stats.getCountMutantDna());
        assertEquals(100, stats.getCountHumanDna());
        assertEquals(0.0, stats.getRatio());
    }

    @Test
    @DisplayName("Debe manejar caso especial cuando no hay humanos")
    void testGetStats_HandleNoHumans() {
        // Given
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(40L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(0L);

        // When
        StatsResponse stats = statsService.getStats();

        // Then
        assertEquals(40, stats.getCountMutantDna());
        assertEquals(0, stats.getCountHumanDna());
        assertEquals(40.0, stats.getRatio());
    }

    @Test
    @DisplayName("Debe retornar 0 cuando no hay datos")
    void testGetStats_ReturnZero_WhenNoData() {
        // Given
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(0L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(0L);

        // When
        StatsResponse stats = statsService.getStats();

        // Then
        assertEquals(0, stats.getCountMutantDna());
        assertEquals(0, stats.getCountHumanDna());
        assertEquals(0.0, stats.getRatio());
    }

    @Test
    @DisplayName("Debe calcular ratio 1.0 cuando hay igual cantidad de mutantes y humanos")
    void testGetStats_ReturnOneRatio_WhenEqual() {
        // Given
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(50L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(50L);

        // When
        StatsResponse stats = statsService.getStats();

        // Then
        assertEquals(50, stats.getCountMutantDna());
        assertEquals(50, stats.getCountHumanDna());
        assertEquals(1.0, stats.getRatio());
    }

    @Test
    @DisplayName("Debe calcular ratio mayor a 1 cuando hay más mutantes que humanos")
    void testGetStats_ReturnRatioGreaterThanOne_WhenMoreMutants() {
        // Given
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(100L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(50L);

        // When
        StatsResponse stats = statsService.getStats();

        // Then
        assertEquals(100, stats.getCountMutantDna());
        assertEquals(50, stats.getCountHumanDna());
        assertEquals(2.0, stats.getRatio());
    }
}