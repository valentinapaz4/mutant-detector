package com.mercadolibre.mutant_detector.service;

import com.mercadolibre.mutant_detector.dto.StatsResponse;
import com.mercadolibre.mutant_detector.repository.DnaRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {

    private final DnaRecordRepository dnaRecordRepository;

    /**
     * Obtiene las estadísticas de verificaciones de DNA
     */
    public StatsResponse getStats() {
        long countMutant = dnaRecordRepository.countByIsMutant(true);
        long countHuman = dnaRecordRepository.countByIsMutant(false);

        // Calcular ratio
        double ratio;
        if (countHuman == 0) {
            ratio = countMutant > 0 ? countMutant : 0.0;
        } else {
            ratio = (double) countMutant / countHuman;
        }

        log.debug("Stats retrieved: mutants={}, humans={}, ratio={}",
                countMutant, countHuman, ratio);

        return new StatsResponse(countMutant, countHuman, ratio);
    }
}