package dev.luanfernandes.dto.response;

import java.util.List;

public record VehiclesRegisteredLastWeekResponse(List<VehicleResponse> vehicles, Long total) {}
