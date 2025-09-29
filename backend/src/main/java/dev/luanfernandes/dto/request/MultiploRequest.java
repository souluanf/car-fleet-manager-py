package dev.luanfernandes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MultiploRequest(
        @NotNull(message = "O número não pode ser nulo")
                @Min(value = 1, message = "O número deve ser maior que zero")
                @Schema(example = "10")
                Integer numero) {}
