package dev.luanfernandes.dto.response;

import java.util.Map;

public record VehiclesByBrandResponse(Map<String, Long> vehiclesByBrand) {}
