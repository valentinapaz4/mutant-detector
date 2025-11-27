package com.mercadolibre.mutant_detector.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {

    private static final int MIN_SIZE = 4;

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        // Null o vacío
        if (dna == null || dna.length == 0) {
            return false;
        }

        final int n = dna.length;

        // Tamaño mínimo
        if (n < MIN_SIZE) {
            return false;
        }

        // Validar que sea matriz cuadrada y caracteres válidos
        for (String row : dna) {
            // Fila null o longitud incorrecta
            if (row == null || row.length() != n) {
                return false;
            }

            // Validar caracteres
            for (char c : row.toCharArray()) {
                if (c != 'A' && c != 'T' && c != 'C' && c != 'G') {
                    return false;
                }
            }
        }

        return true;
    }
}