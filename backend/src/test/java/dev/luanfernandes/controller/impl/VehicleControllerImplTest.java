package dev.luanfernandes.controller.impl;

import static dev.luanfernandes.constants.PathConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.luanfernandes.config.web.ExceptionHandlerAdvice;
import dev.luanfernandes.dto.request.VehicleRequest;
import dev.luanfernandes.dto.request.VehicleUpdateRequest;
import dev.luanfernandes.dto.response.VehicleResponse;
import dev.luanfernandes.dto.response.VehicleStatisticsResponse;
import dev.luanfernandes.dto.response.VehiclesByBrandResponse;
import dev.luanfernandes.dto.response.VehiclesByDecadeResponse;
import dev.luanfernandes.dto.response.VehiclesRegisteredLastWeekResponse;
import dev.luanfernandes.service.VehicleService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for VehicleControllerImpl")
class VehicleControllerImplTest {

    private MockMvc mockMvc;

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleControllerImpl vehicleController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController)
                .setControllerAdvice(new ExceptionHandlerAdvice())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    @DisplayName("Should return 200 when getting all vehicles")
    void shouldReturnOk_WhenGettingAllVehicles() throws Exception {
        var response = createVehicleResponse();

        when(vehicleService.getAllVehicles()).thenReturn(List.of(response));

        mockMvc.perform(get(VEHICLES))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].veiculo").value("Gol"));
    }

    @Test
    @DisplayName("Should return 200 when getting vehicle by id")
    void shouldReturnOk_WhenGettingVehicleById() throws Exception {
        UUID id = UUID.randomUUID();
        var response = createVehicleResponse();

        when(vehicleService.getVehicleById(id)).thenReturn(response);

        mockMvc.perform(get(VEHICLES_BY_ID_V1, id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.veiculo").value("Gol"));
    }

    @Test
    @DisplayName("Should return 201 when creating vehicle")
    void shouldReturnCreated_WhenCreatingVehicle() throws Exception {
        var request = createVehicleRequest();
        var response = createVehicleResponse();

        when(vehicleService.createVehicle(any(VehicleRequest.class))).thenReturn(response);

        mockMvc.perform(post(VEHICLES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.veiculo").value("Gol"));
    }

    @Test
    @DisplayName("Should return 200 when updating vehicle")
    void shouldReturnOk_WhenUpdatingVehicle() throws Exception {
        UUID id = UUID.randomUUID();
        var request = createVehicleRequest();
        var response = createVehicleResponse();

        when(vehicleService.updateVehicle(any(UUID.class), any(VehicleRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put(VEHICLES_BY_ID_V1, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.veiculo").value("Gol"));
    }

    @Test
    @DisplayName("Should return 204 when deleting vehicle")
    void shouldReturnNoContent_WhenDeletingVehicle() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete(VEHICLES_BY_ID_V1, id)).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 200 when getting statistics")
    void shouldReturnOk_WhenGettingStatistics() throws Exception {
        var stats = new VehicleStatisticsResponse(100L, 60L, 40L);

        when(vehicleService.getStatistics()).thenReturn(stats);

        mockMvc.perform(get(VEHICLES_STATISTICS_V1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVehicles").value(100))
                .andExpect(jsonPath("$.unsoldVehicles").value(60))
                .andExpect(jsonPath("$.soldVehicles").value(40));
    }

    @Test
    @DisplayName("Should return 200 when searching vehicles with filters")
    void shouldReturnOk_WhenSearchingVehiclesWithFilters() throws Exception {
        var response = createVehicleResponse();

        when(vehicleService.getVehiclesByFilters("Gol", "VW", 2020, null)).thenReturn(List.of(response));

        mockMvc.perform(get(VEHICLES_SEARCH_V1)
                        .param("veiculo", "Gol")
                        .param("marca", "VW")
                        .param("ano", "2020"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].veiculo").value("Gol"));
    }

    @Test
    @DisplayName("Should return 200 when partially updating vehicle")
    void shouldReturnOk_WhenPartiallyUpdatingVehicle() throws Exception {
        UUID id = UUID.randomUUID();
        var request = new VehicleUpdateRequest("Gol G6", null, null, null, null, true);
        var response = createVehicleResponse();

        when(vehicleService.partialUpdateVehicle(any(UUID.class), any(VehicleUpdateRequest.class)))
                .thenReturn(response);

        mockMvc.perform(patch(VEHICLES_BY_ID_V1, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.veiculo").value("Gol"));
    }

    @Test
    @DisplayName("Should return 200 when getting vehicles by decade")
    void shouldReturnOk_WhenGettingVehiclesByDecade() throws Exception {
        Map<String, Long> decadeMap = new HashMap<>();
        decadeMap.put("2020s", 50L);
        decadeMap.put("2010s", 30L);
        var response = new VehiclesByDecadeResponse(decadeMap);

        when(vehicleService.getVehiclesByDecade()).thenReturn(response);

        mockMvc.perform(get(VEHICLES_STATISTICS_BY_DECADE_V1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehiclesByDecade.2020s").value(50))
                .andExpect(jsonPath("$.vehiclesByDecade.2010s").value(30));
    }

    @Test
    @DisplayName("Should return 200 when getting vehicles by brand")
    void shouldReturnOk_WhenGettingVehiclesByBrand() throws Exception {
        Map<String, Long> brandMap = new HashMap<>();
        brandMap.put("VW", 40L);
        brandMap.put("FIAT", 30L);
        var response = new VehiclesByBrandResponse(brandMap);

        when(vehicleService.getVehiclesByBrand()).thenReturn(response);

        mockMvc.perform(get(VEHICLES_STATISTICS_BY_BRAND_V1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehiclesByBrand.VW").value(40))
                .andExpect(jsonPath("$.vehiclesByBrand.FIAT").value(30));
    }

    @Test
    @DisplayName("Should return 200 when getting vehicles registered last week")
    void shouldReturnOk_WhenGettingVehiclesRegisteredLastWeek() throws Exception {
        var vehicle = createVehicleResponse();
        var response = new VehiclesRegisteredLastWeekResponse(List.of(vehicle), 5L);

        when(vehicleService.getVehiclesRegisteredLastWeek()).thenReturn(response);

        mockMvc.perform(get(VEHICLES_STATISTICS_LAST_WEEK_V1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(5))
                .andExpect(jsonPath("$.vehicles[0].veiculo").value("Gol"));
    }

    private VehicleRequest createVehicleRequest() {
        return new VehicleRequest("Gol", "FIAT", 2020, "Carro compacto", "Branco", false);
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
