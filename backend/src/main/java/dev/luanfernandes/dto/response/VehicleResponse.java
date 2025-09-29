package dev.luanfernandes.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleResponse(
        UUID id,
        String veiculo,
        String marca,
        Integer ano,
        String descricao,
        String cor,
        Boolean vendido,
        LocalDateTime created,
        LocalDateTime updated) {}
