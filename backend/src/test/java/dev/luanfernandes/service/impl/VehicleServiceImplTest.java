package dev.luanfernandes.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.luanfernandes.domain.entity.Brand;
import dev.luanfernandes.domain.entity.Vehicle;
import dev.luanfernandes.domain.exception.VehicleNotFoundException;
import dev.luanfernandes.dto.mapper.VehicleMapper;
import dev.luanfernandes.dto.request.VehicleRequest;
import dev.luanfernandes.dto.request.VehicleUpdateRequest;
import dev.luanfernandes.dto.response.VehicleResponse;
import dev.luanfernandes.repository.VehicleRepository;
import dev.luanfernandes.service.BrandService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for VehicleServiceImpl")
class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private BrandService brandService;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Test
    @DisplayName("Should create vehicle successfully when valid data provided")
    void shouldCreateVehicle_WhenValidData() {
        var request = createVehicleRequest();
        var vehicle = createVehicleEntity();
        var response = createVehicleResponse();

        when(brandService.isBrandValid(request.marca())).thenReturn(true);
        when(vehicleMapper.toEntity(request)).thenReturn(vehicle);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.toResponse(vehicle)).thenReturn(response);

        var result = vehicleService.createVehicle(request);

        assertThat(result).isNotNull().isEqualTo(response);
        verify(brandService).isBrandValid(request.marca());
        verify(vehicleMapper).toEntity(request);
        verify(vehicleRepository).save(vehicle);
        verify(vehicleMapper).toResponse(vehicle);
    }

    @Test
    @DisplayName("Should update vehicle successfully when valid data provided")
    void shouldUpdateVehicle_WhenValidData() {
        UUID id = UUID.randomUUID();
        var request = createVehicleRequest();
        var vehicle = createVehicleEntity();
        var response = createVehicleResponse();

        when(brandService.isBrandValid(request.marca())).thenReturn(true);
        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.toResponse(vehicle)).thenReturn(response);

        var result = vehicleService.updateVehicle(id, request);

        assertThat(result).isNotNull().isEqualTo(response);
        verify(brandService).isBrandValid(request.marca());
        verify(vehicleRepository).findById(id);
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    @DisplayName("Should get vehicle by id successfully")
    void shouldGetVehicleById_Successfully() {
        UUID id = UUID.randomUUID();
        var vehicle = createVehicleEntity();
        var response = createVehicleResponse();

        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));
        when(vehicleMapper.toResponse(vehicle)).thenReturn(response);

        var result = vehicleService.getVehicleById(id);

        assertThat(result).isNotNull().isEqualTo(response);
        verify(vehicleRepository).findById(id);
        verify(vehicleMapper).toResponse(vehicle);
    }

    @Test
    @DisplayName("Should throw exception when vehicle not found")
    void shouldThrowException_WhenVehicleNotFound() {
        UUID id = UUID.randomUUID();
        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.getVehicleById(id)).isInstanceOf(VehicleNotFoundException.class);
    }

    @Test
    @DisplayName("Should partial update vehicle when some fields provided")
    void shouldPartialUpdateVehicle_WhenSomeFieldsProvided() {
        UUID id = UUID.randomUUID();
        var request = new VehicleUpdateRequest("New Name", null, null, null, null, true);
        var vehicle = createVehicleEntity();
        var response = createVehicleResponse();

        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.toResponse(vehicle)).thenReturn(response);

        var result = vehicleService.partialUpdateVehicle(id, request);

        assertThat(result).isNotNull();
        verify(vehicleRepository).findById(id);
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    @DisplayName("Should delete vehicle successfully")
    void shouldDeleteVehicle_Successfully() {
        UUID id = UUID.randomUUID();
        var vehicle = createVehicleEntity();

        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));

        vehicleService.deleteVehicle(id);

        verify(vehicleRepository).findById(id);
        verify(vehicleRepository).delete(vehicle);
    }

    @Test
    @DisplayName("Should get all vehicles successfully")
    void shouldGetAllVehicles_Successfully() {
        var vehicles = Arrays.asList(createVehicleEntity(), createVehicleEntity());
        var responses = Arrays.asList(createVehicleResponse(), createVehicleResponse());

        when(vehicleRepository.findAll()).thenReturn(vehicles);
        when(vehicleMapper.toResponse(any(Vehicle.class)))
                .thenReturn(responses.get(0))
                .thenReturn(responses.get(1));

        var result = vehicleService.getAllVehicles();

        assertThat(result).hasSize(2);
        verify(vehicleRepository).findAll();
    }

    @Test
    @DisplayName("Should get vehicles by filters")
    void shouldGetVehiclesByFilters() {
        var vehicle = createVehicleEntity();
        var response = createVehicleResponse();

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));
        when(vehicleMapper.toResponse(any(Vehicle.class))).thenReturn(response);

        var result = vehicleService.getVehiclesByFilters("Gol", "Volkswagen", 2020, "Branco");

        assertThat(result).hasSize(1);
        verify(vehicleRepository).findAll();
    }

    @Test
    @DisplayName("Should get statistics correctly")
    void shouldGetStatistics() {
        var vehicle1 = createVehicleEntity();
        vehicle1.setVendido(false);
        var vehicle2 = createVehicleEntity();
        vehicle2.setVendido(false);
        var vehicle3 = createVehicleEntity();
        vehicle3.setVendido(true);

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle1, vehicle2, vehicle3));

        var result = vehicleService.getStatistics();

        assertThat(result).isNotNull();
        assertThat(result.totalVehicles()).isEqualTo(3L);
        assertThat(result.unsoldVehicles()).isEqualTo(2L);
        assertThat(result.soldVehicles()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should get vehicles by decade")
    void shouldGetVehiclesByDecade() {
        var vehicle2015 = createVehicleEntity();
        vehicle2015.setAno(2015);
        var vehicle2023 = createVehicleEntity();
        vehicle2023.setAno(2023);

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle2015, vehicle2023));

        var result = vehicleService.getVehiclesByDecade();

        assertThat(result).isNotNull();
        assertThat(result.vehiclesByDecade())
                .hasSize(2)
                .containsEntry("2010s", 1L)
                .containsEntry("2020s", 1L);
    }

    @Test
    @DisplayName("Should get vehicles by brand")
    void shouldGetVehiclesByBrand() {
        var vehicleVW = createVehicleWithBrand("Volkswagen");
        var vehicleFiat = createVehicleWithBrand("Fiat");

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicleVW, vehicleFiat));

        var result = vehicleService.getVehiclesByBrand();

        assertThat(result).isNotNull();
        assertThat(result.vehiclesByBrand())
                .hasSize(2)
                .containsEntry("Volkswagen", 1L)
                .containsEntry("Fiat", 1L);
    }

    @Test
    @DisplayName("Should get vehicles registered last week")
    void shouldGetVehiclesRegisteredLastWeek() {
        var vehicle = createVehicleEntity();
        vehicle.setCreated(LocalDateTime.now().minusDays(3));
        var response = createVehicleResponse();

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));
        when(vehicleMapper.toResponse(any(Vehicle.class))).thenReturn(response);

        var result = vehicleService.getVehiclesRegisteredLastWeek();

        assertThat(result).isNotNull();
        assertThat(result.vehicles()).hasSize(1);
        assertThat(result.total()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should throw exception when creating vehicle with invalid brand")
    void shouldThrowException_WhenCreatingVehicleWithInvalidBrand() {
        var request = createVehicleRequest();

        when(brandService.isBrandValid(request.marca())).thenReturn(false);

        assertThatThrownBy(() -> vehicleService.createVehicle(request))
                .isInstanceOf(dev.luanfernandes.domain.exception.InvalidBrandException.class);

        verify(brandService).isBrandValid(request.marca());
    }

    @Test
    @DisplayName("Should validate brand when partial update includes brand")
    void shouldValidateBrand_WhenPartialUpdateIncludesBrand() {
        UUID id = UUID.randomUUID();
        var request = new VehicleUpdateRequest("New Name", "Volkswagen", null, null, null, true);
        var vehicle = createVehicleEntity();
        var response = createVehicleResponse();

        when(brandService.isBrandValid("Volkswagen")).thenReturn(true);
        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.toResponse(vehicle)).thenReturn(response);

        var result = vehicleService.partialUpdateVehicle(id, request);

        assertThat(result).isNotNull();
        verify(brandService).isBrandValid("Volkswagen");
        verify(vehicleRepository).findById(id);
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    @DisplayName("Should throw exception when updating vehicle with invalid brand")
    void shouldThrowException_WhenUpdatingVehicleWithInvalidBrand() {
        UUID id = UUID.randomUUID();
        var request = createVehicleRequest();

        when(brandService.isBrandValid(request.marca())).thenReturn(false);

        assertThatThrownBy(() -> vehicleService.updateVehicle(id, request))
                .isInstanceOf(dev.luanfernandes.domain.exception.InvalidBrandException.class);

        verify(brandService).isBrandValid(request.marca());
    }

    @Test
    @DisplayName("Should throw exception when partial update vehicle with invalid brand")
    void shouldThrowException_WhenPartialUpdateVehicleWithInvalidBrand() {
        UUID id = UUID.randomUUID();
        var vehicle = createVehicleEntity();
        vehicle.setId(id);
        var request = new VehicleUpdateRequest("New Name", "InvalidBrand", null, null, null, true);

        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));
        when(brandService.isBrandValid("InvalidBrand")).thenReturn(false);

        assertThatThrownBy(() -> vehicleService.partialUpdateVehicle(id, request))
                .isInstanceOf(dev.luanfernandes.domain.exception.InvalidBrandException.class);

        verify(brandService).isBrandValid("InvalidBrand");
    }

    @Test
    @DisplayName("Should get vehicles by filters with null filters")
    void shouldGetVehiclesByFilters_WithNullFilters() {
        var vehicle = createVehicleEntity();
        var response = createVehicleResponse();

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));
        when(vehicleMapper.toResponse(any(Vehicle.class))).thenReturn(response);

        var result = vehicleService.getVehiclesByFilters(null, null, null, null);

        assertThat(result).hasSize(1);
        verify(vehicleRepository).findAll();
    }

    @Test
    @DisplayName("Should filter vehicles by veiculo only")
    void shouldFilterVehiclesByVeiculoOnly() {
        var vehicle1 = createVehicleEntity();
        vehicle1.setVeiculo("Gol");
        var vehicle2 = createVehicleEntity();
        vehicle2.setVeiculo("Palio");
        var response = createVehicleResponse();

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle1, vehicle2));
        when(vehicleMapper.toResponse(any(Vehicle.class))).thenReturn(response);

        var result = vehicleService.getVehiclesByFilters("gol", null, null, null);

        assertThat(result).hasSize(1);
        verify(vehicleRepository).findAll();
    }

    @Test
    @DisplayName("Should filter vehicles by marca only")
    void shouldFilterVehiclesByMarcaOnly() {
        var vehicle1 = createVehicleWithBrand("Volkswagen");
        var vehicle2 = createVehicleWithBrand("Fiat");
        var response = createVehicleResponse();

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle1, vehicle2));
        when(vehicleMapper.toResponse(any(Vehicle.class))).thenReturn(response);

        var result = vehicleService.getVehiclesByFilters(null, "volkswagen", null, null);

        assertThat(result).hasSize(1);
        verify(vehicleRepository).findAll();
    }

    @Test
    @DisplayName("Should filter vehicles by ano only")
    void shouldFilterVehiclesByAnoOnly() {
        var vehicle1 = createVehicleEntity();
        vehicle1.setAno(2020);
        var vehicle2 = createVehicleEntity();
        vehicle2.setAno(2021);
        var response = createVehicleResponse();

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle1, vehicle2));
        when(vehicleMapper.toResponse(any(Vehicle.class))).thenReturn(response);

        var result = vehicleService.getVehiclesByFilters(null, null, 2020, null);

        assertThat(result).hasSize(1);
        verify(vehicleRepository).findAll();
    }

    @Test
    @DisplayName("Should filter vehicles by cor only")
    void shouldFilterVehiclesByCorOnly() {
        var vehicle1 = createVehicleEntity();
        vehicle1.setCor("Branco");
        var vehicle2 = createVehicleEntity();
        vehicle2.setCor("Preto");
        var response = createVehicleResponse();

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle1, vehicle2));
        when(vehicleMapper.toResponse(any(Vehicle.class))).thenReturn(response);

        var result = vehicleService.getVehiclesByFilters(null, null, null, "branco");

        assertThat(result).hasSize(1);
        verify(vehicleRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no vehicles match filters")
    void shouldReturnEmptyList_WhenNoVehiclesMatchFilters() {
        var vehicle = createVehicleEntity();
        vehicle.setVeiculo("Gol");

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));

        var result = vehicleService.getVehiclesByFilters("Palio", null, null, null);

        assertThat(result).isEmpty();
        verify(vehicleRepository).findAll();
    }

    @Test
    @DisplayName("Should get empty list when no vehicles registered last week")
    void shouldGetEmptyList_WhenNoVehiclesRegisteredLastWeek() {
        var vehicle = createVehicleEntity();
        vehicle.setCreated(LocalDateTime.now().minusWeeks(2));

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));

        var result = vehicleService.getVehiclesRegisteredLastWeek();

        assertThat(result).isNotNull();
        assertThat(result.vehicles()).isEmpty();
        assertThat(result.total()).isZero();
    }

    private VehicleRequest createVehicleRequest() {
        return new VehicleRequest("Gol", "VW", 2020, "Carro compacto", "Branco", false);
    }

    private Vehicle createVehicleEntity() {
        return createVehicleWithBrand("Volkswagen");
    }

    private Vehicle createVehicleWithBrand(String brandName) {
        var vehicle = new Vehicle();
        vehicle.setId(UUID.randomUUID());
        vehicle.setVeiculo("Gol");
        vehicle.setBrand(createBrandEntity(brandName));
        vehicle.setAno(2020);
        vehicle.setDescricao("Carro compacto");
        vehicle.setCor("Branco");
        vehicle.setVendido(false);
        return vehicle;
    }

    private Brand createBrandEntity(String name) {
        var brand = new Brand();
        brand.setId(UUID.randomUUID());
        brand.setName(name);
        return brand;
    }

    private VehicleResponse createVehicleResponse() {
        return new VehicleResponse(
                UUID.randomUUID(),
                "Gol",
                "VW",
                2020,
                "Carro compacto",
                "Branco",
                false,
                LocalDateTime.now(),
                LocalDateTime.now());
    }
}
