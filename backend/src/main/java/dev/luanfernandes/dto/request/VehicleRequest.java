package dev.luanfernandes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VehicleRequest(
        @NotBlank(message = "O nome do veículo é obrigatório")
                @Size(max = 100, message = "O nome do veículo deve ter no máximo 100 caracteres")
                @Schema(example = "Gol")
                String veiculo,
        @NotBlank(message = "A marca do veículo é obrigatória")
                @Size(max = 100, message = "A marca do veículo deve ter no máximo 100 caracteres")
                @Schema(example = "Volkswagen")
                String marca,
        @NotNull(message = "O ano é obrigatório")
                @Min(value = 1900, message = "O ano deve ser maior ou igual a 1900")
                @Schema(example = "2020")
                Integer ano,
        @NotBlank(message = "A descrição é obrigatória")
                @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
                @Schema(example = "Carro compacto, econômico e ideal para cidade")
                String descricao,
        @Size(max = 50, message = "A cor deve ter no máximo 50 caracteres") @Schema(example = "Branco") String cor,
        @NotNull(message = "O status de vendido é obrigatório") @Schema(example = "false") Boolean vendido) {}
