package com.mercadolibre.mutant_detector.controller;

import com.mercadolibre.mutant_detector.dto.DnaRequest;
import com.mercadolibre.mutant_detector.dto.StatsResponse;
import com.mercadolibre.mutant_detector.service.MutantService;
import com.mercadolibre.mutant_detector.service.StatsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;


    @PostMapping("/mutant")
    public ResponseEntity<Void> checkMutant(@Valid @RequestBody DnaRequest request) {
        log.info("Received DNA check request");

        boolean isMutant = mutantService.analyzeDna(request.getDna());

        if (isMutant) {
            log.info("DNA is MUTANT");
            return ResponseEntity.ok().build();
        } else {
            log.info("DNA is HUMAN");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        log.info("Received stats request");

        StatsResponse stats = statsService.getStats();

        return ResponseEntity.ok(stats);
    }
}
