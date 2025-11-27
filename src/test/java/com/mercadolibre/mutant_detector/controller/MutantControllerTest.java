package com.mercadolibre.mutant_detector.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.mutant_detector.dto.DnaRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("POST /mutant debe retornar 200 OK cuando es mutante")
    void testCheckMutant_ReturnOk_WhenIsMutant() throws Exception {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        DnaRequest request = new DnaRequest(dna);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 403 Forbidden cuando NO es mutante")
    void testCheckMutant_ReturnForbidden_WhenIsNotMutant() throws Exception {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };

        DnaRequest request = new DnaRequest(dna);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request para DNA inválido - matriz no cuadrada")
    void testCheckMutant_ReturnBadRequest_WhenDnaIsNotSquare() throws Exception {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TGAC"  // Solo 3 filas
        };

        DnaRequest request = new DnaRequest(dna);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid DNA sequence: must be a square NxN matrix (minimum 4x4) with only A, T, C, G characters"));
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request para DNA con caracteres inválidos")
    void testCheckMutant_ReturnBadRequest_WhenDnaHasInvalidCharacters() throws Exception {
        String[] dna = {
                "ATXC",  // X es inválido
                "CAGT",
                "TGAC",
                "GCAT"
        };

        DnaRequest request = new DnaRequest(dna);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request para DNA vacío")
    void testCheckMutant_ReturnBadRequest_WhenDnaIsEmpty() throws Exception {
        String[] dna = {};

        DnaRequest request = new DnaRequest(dna);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request para DNA muy pequeño (3x3)")
    void testCheckMutant_ReturnBadRequest_WhenDnaIsTooSmall() throws Exception {
        String[] dna = {
                "ATG",
                "CAG",
                "TGA"
        };

        DnaRequest request = new DnaRequest(dna);

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ========== TESTS GET /stats ==========

    @Test
    @DisplayName("GET /stats debe retornar 200 OK con estructura correcta")
    void testGetStats_ReturnOk_WithCorrectStructure() throws Exception {
        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count_mutant_dna").exists())
                .andExpect(jsonPath("$.count_human_dna").exists())
                .andExpect(jsonPath("$.ratio").exists());
    }

    @Test
    @DisplayName("GET /stats debe retornar números válidos")
    void testGetStats_ReturnValidNumbers() throws Exception {
        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").isNumber())
                .andExpect(jsonPath("$.count_human_dna").isNumber())
                .andExpect(jsonPath("$.ratio").isNumber());
    }
}