package dev.luanfernandes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record VotingDataRequest(
        @NotNull(message = "Total de eleitores é obrigatório")
                @Min(value = 1, message = "Total de eleitores deve ser maior que zero")
                @Schema(example = "1000")
                Integer totalEleitores,
        @NotNull(message = "Votos válidos é obrigatório")
                @Min(value = 0, message = "Votos válidos não pode ser negativo")
                @Schema(example = "800")
                Integer votosValidos,
        @NotNull(message = "Votos brancos é obrigatório")
                @Min(value = 0, message = "Votos brancos não pode ser negativo")
                @Schema(example = "150")
                Integer votosBrancos,
        @NotNull(message = "Votos nulos é obrigatório")
                @Min(value = 0, message = "Votos nulos não pode ser negativo")
                @Schema(example = "50")
                Integer votosNulos) {}
