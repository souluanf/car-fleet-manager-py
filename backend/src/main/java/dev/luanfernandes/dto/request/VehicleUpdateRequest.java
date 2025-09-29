package dev.luanfernandes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record VehicleUpdateRequest(
        @Size(max = 100, message = "O nome do veículo deve ter no máximo 100 caracteres") @Schema(example = "Gol G5")
                String veiculo,
        @Size(max = 100, message = "A marca do veículo deve ter no máximo 100 caracteres")
                @Schema(example = "Volkswagen")
                String marca,
        @Min(value = 1900, message = "O ano deve ser maior ou igual a 1900") @Schema(example = "2021") Integer ano,
        @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
                @Schema(example = "Carro compacto em ótimo estado")
                String descricao,
        @Size(max = 50, message = "A cor deve ter no máximo 50 caracteres") @Schema(example = "Prata") String cor,
        @Schema(example = "true") Boolean vendido) {}
