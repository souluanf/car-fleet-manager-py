package dev.luanfernandes.controller;

import static dev.luanfernandes.constants.PathConstants.EXERCISES_BUBBLE_SORT;
import static dev.luanfernandes.constants.PathConstants.EXERCISES_FATORIAL;
import static dev.luanfernandes.constants.PathConstants.EXERCISES_MULTIPLOS;
import static dev.luanfernandes.constants.PathConstants.EXERCISES_VOTING;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import dev.luanfernandes.dto.request.BubbleSortRequest;
import dev.luanfernandes.dto.request.FatorialRequest;
import dev.luanfernandes.dto.request.MultiploRequest;
import dev.luanfernandes.dto.request.VotingDataRequest;
import dev.luanfernandes.dto.response.BubbleSortResponse;
import dev.luanfernandes.dto.response.FatorialResponse;
import dev.luanfernandes.dto.response.MultiploResponse;
import dev.luanfernandes.dto.response.VotingPercentageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Exercises", description = "Endpoints para exercícios de lógica de programação")
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public interface ExerciseController {

    @Operation(
            summary = "Calcular percentuais de votos",
            description = "Calcula o percentual de votos válidos, brancos e nulos em relação ao total de eleitores",
            responses = {
                @ApiResponse(responseCode = "200", description = "Percentuais calculados com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
            })
    @PostMapping(EXERCISES_VOTING)
    ResponseEntity<VotingPercentageResponse> calculateVotingPercentages(@Valid @RequestBody VotingDataRequest request);

    @Operation(
            summary = "Ordenar vetor usando Bubble Sort",
            description = "Ordena um vetor de inteiros utilizando o algoritmo Bubble Sort",
            responses = {
                @ApiResponse(responseCode = "200", description = "Vetor ordenado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Vetor inválido")
            })
    @PostMapping(EXERCISES_BUBBLE_SORT)
    ResponseEntity<BubbleSortResponse> sortArray(@Valid @RequestBody BubbleSortRequest request);

    @Operation(
            summary = "Calcular fatorial de um número",
            description = "Calcula o fatorial de um número natural usando recursão",
            responses = {
                @ApiResponse(responseCode = "200", description = "Fatorial calculado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Número inválido")
            })
    @PostMapping(EXERCISES_FATORIAL)
    ResponseEntity<FatorialResponse> calculateFactorial(@Valid @RequestBody FatorialRequest request);

    @Operation(
            summary = "Calcular soma de múltiplos de 3 ou 5",
            description = "Calcula a soma de todos os números múltiplos de 3 ou 5 abaixo de um número X",
            responses = {
                @ApiResponse(responseCode = "200", description = "Soma calculada com sucesso"),
                @ApiResponse(responseCode = "400", description = "Número inválido")
            })
    @PostMapping(EXERCISES_MULTIPLOS)
    ResponseEntity<MultiploResponse> calculateMultiples(@Valid @RequestBody MultiploRequest request);
}
