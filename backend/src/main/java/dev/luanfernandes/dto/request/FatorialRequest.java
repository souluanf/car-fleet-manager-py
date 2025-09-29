package dev.luanfernandes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record FatorialRequest(
        @NotNull(message = "O número não pode ser nulo")
                @Min(value = 0, message = "O número deve ser maior ou igual a zero")
                @Schema(example = "5")
                Integer numero) {}
