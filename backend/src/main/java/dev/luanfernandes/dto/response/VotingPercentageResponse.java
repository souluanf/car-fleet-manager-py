package dev.luanfernandes.dto.response;

import java.math.BigDecimal;

public record VotingPercentageResponse(
        BigDecimal percentualVotosValidos, BigDecimal percentualVotosBrancos, BigDecimal percentualVotosNulos) {}
