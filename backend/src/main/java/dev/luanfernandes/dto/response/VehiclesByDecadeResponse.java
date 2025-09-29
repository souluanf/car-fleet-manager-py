package dev.luanfernandes.dto.response;

import java.util.Map;

public record VehiclesByDecadeResponse(Map<String, Long> vehiclesByDecade) {}
