package dev.luanfernandes.service.impl;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import dev.luanfernandes.domain.entity.Brand;
import dev.luanfernandes.domain.entity.Vehicle;
import dev.luanfernandes.domain.exception.InvalidBrandException;
import dev.luanfernandes.domain.exception.VehicleNotFoundException;
import dev.luanfernandes.dto.mapper.VehicleMapper;
import dev.luanfernandes.dto.request.VehicleRequest;
import dev.luanfernandes.dto.request.VehicleUpdateRequest;
import dev.luanfernandes.dto.response.VehicleResponse;
import dev.luanfernandes.dto.response.VehicleStatisticsResponse;
import dev.luanfernandes.dto.response.VehiclesByBrandResponse;
import dev.luanfernandes.dto.response.VehiclesByDecadeResponse;
import dev.luanfernandes.dto.response.VehiclesRegisteredLastWeekResponse;
import dev.luanfernandes.repository.VehicleRepository;
import dev.luanfernandes.service.BrandService;
import dev.luanfernandes.service.VehicleService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final BrandService brandService;

    @Override
    @Transactional
    public VehicleResponse createVehicle(VehicleRequest request) {
        validateBrand(request.marca());
        Brand brand = brandService.findBrandByName(request.marca());
        Vehicle vehicle = vehicleMapper.toEntity(request);
        vehicle.setBrand(brand);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(savedVehicle);
    }

    @Override
    @Transactional
    public VehicleResponse updateVehicle(UUID id, VehicleRequest request) {
        validateBrand(request.marca());
        Brand brand = brandService.findBrandByName(request.marca());
        Vehicle vehicle = findVehicleById(id);
        vehicleMapper.update(request, vehicle);
        vehicle.setBrand(brand);
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(updatedVehicle);
    }

    private void validateBrand(String marca) {
        if (!brandService.isBrandValid(marca)) {
            throw new InvalidBrandException(marca);
        }
    }

    @Override
    @Transactional
    public VehicleResponse partialUpdateVehicle(UUID id, VehicleUpdateRequest request) {
        Vehicle vehicle = findVehicleById(id);
        if (request.marca() != null) {
            validateBrand(request.marca());
            Brand brand = brandService.findBrandByName(request.marca());
            vehicle.setBrand(brand);
        }
        vehicleMapper.partialUpdate(request, vehicle);
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(updatedVehicle);
    }

    @Override
    @Transactional
    public void deleteVehicle(UUID id) {
        Vehicle vehicle = findVehicleById(id);
        vehicleRepository.delete(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleResponse getVehicleById(UUID id) {
        Vehicle vehicle = findVehicleById(id);
        return vehicleMapper.toResponse(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(vehicleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> getVehiclesByFilters(String veiculo, String marca, Integer ano, String cor) {
        return vehicleRepository.findAll().stream()
                .filter(vehicle ->
                        veiculo == null || vehicle.getVeiculo().toLowerCase().contains(veiculo.toLowerCase()))
                .filter(vehicle -> marca == null
                        || vehicle.getBrand().getName().toLowerCase().contains(marca.toLowerCase()))
                .filter(vehicle -> ano == null || vehicle.getAno().equals(ano))
                .filter(vehicle -> cor == null || vehicle.getCor().toLowerCase().contains(cor.toLowerCase()))
                .map(vehicleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleStatisticsResponse getStatistics() {
        List<Vehicle> allVehicles = vehicleRepository.findAll();
        long totalVehicles = allVehicles.size();
        long unsoldVehicles = allVehicles.stream().filter(v -> !v.getVendido()).count();
        long soldVehicles = totalVehicles - unsoldVehicles;
        return new VehicleStatisticsResponse(totalVehicles, unsoldVehicles, soldVehicles);
    }

    @Override
    @Transactional(readOnly = true)
    public VehiclesByDecadeResponse getVehiclesByDecade() {
        Map<String, Long> vehiclesByDecade = vehicleRepository.findAll().stream()
                .collect(groupingBy(vehicle -> (vehicle.getAno() / 10 * 10) + "s", counting()));
        return new VehiclesByDecadeResponse(vehiclesByDecade);
    }

    @Override
    @Transactional(readOnly = true)
    public VehiclesByBrandResponse getVehiclesByBrand() {
        Map<String, Long> vehiclesByBrand = vehicleRepository.findAll().stream()
                .collect(groupingBy(vehicle -> vehicle.getBrand().getName(), counting()));
        return new VehiclesByBrandResponse(vehiclesByBrand);
    }

    @Override
    @Transactional(readOnly = true)
    public VehiclesRegisteredLastWeekResponse getVehiclesRegisteredLastWeek() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        List<VehicleResponse> vehicleResponses = vehicleRepository.findAll().stream()
                .filter(vehicle -> vehicle.getCreated().isAfter(oneWeekAgo))
                .map(vehicleMapper::toResponse)
                .toList();

        return new VehiclesRegisteredLastWeekResponse(vehicleResponses, (long) vehicleResponses.size());
    }

    private Vehicle findVehicleById(UUID id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new VehicleNotFoundException(id));
    }
}
