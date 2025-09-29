package dev.luanfernandes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record BubbleSortRequest(
        @NotNull(message = "O vetor não pode ser nulo")
                @Size(min = 1, message = "O vetor deve conter pelo menos um elemento")
                @Schema(example = "[5, 3, 8, 1, 2, 50, 7]")
                List<Integer> vetor) {}
