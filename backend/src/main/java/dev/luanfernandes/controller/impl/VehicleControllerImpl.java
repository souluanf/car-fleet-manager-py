package dev.luanfernandes.controller.impl;

import static org.springframework.http.ResponseEntity.noContent;

import dev.luanfernandes.controller.VehicleController;
import dev.luanfernandes.dto.request.VehicleRequest;
import dev.luanfernandes.dto.request.VehicleUpdateRequest;
import dev.luanfernandes.dto.response.*;
import dev.luanfernandes.service.VehicleService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VehicleControllerImpl implements VehicleController {

    private final VehicleService vehicleService;

    @Override
    public ResponseEntity<List<VehicleResponse>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @Override
    public ResponseEntity<List<VehicleResponse>> searchVehicles(String veiculo, String marca, Integer ano, String cor) {
        return ResponseEntity.ok(vehicleService.getVehiclesByFilters(veiculo, marca, ano, cor));
    }

    @Override
    public ResponseEntity<VehicleResponse> getVehicleById(UUID id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @Override
    public ResponseEntity<VehicleResponse> createVehicle(VehicleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.createVehicle(request));
    }

    @Override
    public ResponseEntity<VehicleResponse> updateVehicle(UUID id, VehicleRequest request) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, request));
    }

    @Override
    public ResponseEntity<VehicleResponse> partialUpdateVehicle(UUID id, VehicleUpdateRequest request) {
        return ResponseEntity.ok(vehicleService.partialUpdateVehicle(id, request));
    }

    @Override
    public ResponseEntity<Void> deleteVehicle(UUID id) {
        vehicleService.deleteVehicle(id);
        return noContent().build();
    }

    @Override
    public ResponseEntity<VehicleStatisticsResponse> getStatistics() {
        return ResponseEntity.ok(vehicleService.getStatistics());
    }

    @Override
    public ResponseEntity<VehiclesByDecadeResponse> getVehiclesByDecade() {
        return ResponseEntity.ok(vehicleService.getVehiclesByDecade());
    }

    @Override
    public ResponseEntity<VehiclesByBrandResponse> getVehiclesByBrand() {
        return ResponseEntity.ok(vehicleService.getVehiclesByBrand());
    }

    @Override
    public ResponseEntity<VehiclesRegisteredLastWeekResponse> getVehiclesRegisteredLastWeek() {
        return ResponseEntity.ok(vehicleService.getVehiclesRegisteredLastWeek());
    }
}
