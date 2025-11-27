package com.mercadolibre.mutant_detector.service;

import com.mercadolibre.mutant_detector.entity.DnaRecord;
import com.mercadolibre.mutant_detector.repository.DnaRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MutantService {

    private final MutantDetector mutantDetector;
    private final DnaRecordRepository dnaRecordRepository;

    /**
     * Analiza una secuencia de DNA y guarda el resultado en BD.
     * Si el DNA ya fue analizado, retorna el resultado cacheado.
     */
    @Transactional
    public boolean analyzeDna(String[] dna) {
        // Calcular hash del DNA
        String dnaHash = calculateDnaHash(dna);

        // Verificar si ya fue analizado (caché)
        Optional<DnaRecord> existingRecord = dnaRecordRepository.findByDnaHash(dnaHash);

        if (existingRecord.isPresent()) {
            log.debug("DNA already analyzed (cached): {}", dnaHash);
            return existingRecord.get().getIsMutant();
        }

        // Analizar DNA
        boolean isMutant = mutantDetector.isMutant(dna);

        // Guardar resultado
        DnaRecord record = new DnaRecord(dnaHash, isMutant);
        dnaRecordRepository.save(record);

        log.info("DNA analyzed and saved: hash={}, isMutant={}", dnaHash, isMutant);

        return isMutant;
    }

    /**
     * Calcula el hash SHA-256 de una secuencia de DNA
     */
    private String calculateDnaHash(String[] dna) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String concatenated = String.join("", dna);
            byte[] hashBytes = digest.digest(concatenated.getBytes(StandardCharsets.UTF_8));

            // Convertir a hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            log.error("Error calculating DNA hash", e);
            throw new RuntimeException("Error calculating DNA hash", e);
        }
    }
}