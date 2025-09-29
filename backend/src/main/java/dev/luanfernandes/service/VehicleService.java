package dev.luanfernandes.service;

import dev.luanfernandes.dto.request.VehicleRequest;
import dev.luanfernandes.dto.request.VehicleUpdateRequest;
import dev.luanfernandes.dto.response.*;
import java.util.List;
import java.util.UUID;

public interface VehicleService {
    VehicleResponse createVehicle(VehicleRequest request);

    VehicleResponse updateVehicle(UUID id, VehicleRequest request);

    VehicleResponse partialUpdateVehicle(UUID id, VehicleUpdateRequest request);

    void deleteVehicle(UUID id);

    VehicleResponse getVehicleById(UUID id);

    List<VehicleResponse> getAllVehicles();

    List<VehicleResponse> getVehiclesByFilters(String veiculo, String marca, Integer ano, String cor);

    VehicleStatisticsResponse getStatistics();

    VehiclesByDecadeResponse getVehiclesByDecade();

    VehiclesByBrandResponse getVehiclesByBrand();

    VehiclesRegisteredLastWeekResponse getVehiclesRegisteredLastWeek();
}
