package dev.luanfernandes.controller;

import static dev.luanfernandes.constants.PathConstants.VEHICLES;
import static dev.luanfernandes.constants.PathConstants.VEHICLES_BY_ID_V1;
import static dev.luanfernandes.constants.PathConstants.VEHICLES_SEARCH_V1;
import static dev.luanfernandes.constants.PathConstants.VEHICLES_STATISTICS_BY_BRAND_V1;
import static dev.luanfernandes.constants.PathConstants.VEHICLES_STATISTICS_BY_DECADE_V1;
import static dev.luanfernandes.constants.PathConstants.VEHICLES_STATISTICS_LAST_WEEK_V1;
import static dev.luanfernandes.constants.PathConstants.VEHICLES_STATISTICS_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import dev.luanfernandes.dto.request.VehicleRequest;
import dev.luanfernandes.dto.request.VehicleUpdateRequest;
import dev.luanfernandes.dto.response.VehicleResponse;
import dev.luanfernandes.dto.response.VehicleStatisticsResponse;
import dev.luanfernandes.dto.response.VehiclesByBrandResponse;
import dev.luanfernandes.dto.response.VehiclesByDecadeResponse;
import dev.luanfernandes.dto.response.VehiclesRegisteredLastWeekResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Vehicles", description = "Endpoints para gerenciamento de veículos")
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public interface VehicleController {

    @Operation(
            summary = "Listar todos os veículos",
            description = "Retorna todos os veículos cadastrados",
            responses = {@ApiResponse(responseCode = "200", description = "Lista de veículos retornada com sucesso")})
    @GetMapping(VEHICLES)
    ResponseEntity<List<VehicleResponse>> getAllVehicles();

    @Operation(
            summary = "Buscar veículos com filtros",
            description = "Busca veículos por marca, ano, cor ou nome",
            responses = {@ApiResponse(responseCode = "200", description = "Veículos encontrados")})
    @GetMapping(VEHICLES_SEARCH_V1)
    ResponseEntity<List<VehicleResponse>> searchVehicles(
            @Parameter(description = "Nome do veículo") @RequestParam(required = false) String veiculo,
            @Parameter(description = "Marca do veículo") @RequestParam(required = false) String marca,
            @Parameter(description = "Ano do veículo") @RequestParam(required = false) Integer ano,
            @Parameter(description = "Cor do veículo") @RequestParam(required = false) String cor);

    @Operation(
            summary = "Buscar veículo por ID",
            description = "Retorna os detalhes de um veículo específico",
            responses = {
                @ApiResponse(responseCode = "200", description = "Veículo encontrado"),
                @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
            })
    @GetMapping(VEHICLES_BY_ID_V1)
    ResponseEntity<VehicleResponse> getVehicleById(@PathVariable UUID id);

    @Operation(
            summary = "Cadastrar novo veículo",
            description = "Adiciona um novo veículo ao sistema",
            responses = {
                @ApiResponse(responseCode = "201", description = "Veículo criado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos")
            })
    @PostMapping(VEHICLES)
    ResponseEntity<VehicleResponse> createVehicle(@Valid @RequestBody VehicleRequest request);

    @Operation(
            summary = "Atualizar veículo completo",
            description = "Atualiza todos os dados de um veículo",
            responses = {
                @ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso"),
                @ApiResponse(responseCode = "404", description = "Veículo não encontrado"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos")
            })
    @PutMapping(VEHICLES_BY_ID_V1)
    ResponseEntity<VehicleResponse> updateVehicle(@PathVariable UUID id, @Valid @RequestBody VehicleRequest request);

    @Operation(
            summary = "Atualizar veículo parcialmente",
            description = "Atualiza apenas alguns campos do veículo",
            responses = {
                @ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso"),
                @ApiResponse(responseCode = "404", description = "Veículo não encontrado"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos")
            })
    @PatchMapping(VEHICLES_BY_ID_V1)
    ResponseEntity<VehicleResponse> partialUpdateVehicle(
            @PathVariable UUID id, @Valid @RequestBody VehicleUpdateRequest request);

    @Operation(
            summary = "Excluir veículo",
            description = "Remove um veículo do sistema",
            responses = {
                @ApiResponse(responseCode = "204", description = "Veículo excluído com sucesso"),
                @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
            })
    @DeleteMapping(VEHICLES_BY_ID_V1)
    ResponseEntity<Void> deleteVehicle(@PathVariable UUID id);

    @Operation(
            summary = "Obter estatísticas",
            description = "Retorna quantidade de veículos vendidos e não vendidos",
            responses = {@ApiResponse(responseCode = "200", description = "Estatísticas obtidas com sucesso")})
    @GetMapping(VEHICLES_STATISTICS_V1)
    ResponseEntity<VehicleStatisticsResponse> getStatistics();

    @Operation(
            summary = "Obter distribuição por década",
            description = "Retorna a quantidade de veículos por década de fabricação",
            responses = {@ApiResponse(responseCode = "200", description = "Distribuição obtida com sucesso")})
    @GetMapping(VEHICLES_STATISTICS_BY_DECADE_V1)
    ResponseEntity<VehiclesByDecadeResponse> getVehiclesByDecade();

    @Operation(
            summary = "Obter distribuição por fabricante",
            description = "Retorna a quantidade de veículos por fabricante",
            responses = {@ApiResponse(responseCode = "200", description = "Distribuição obtida com sucesso")})
    @GetMapping(VEHICLES_STATISTICS_BY_BRAND_V1)
    ResponseEntity<VehiclesByBrandResponse> getVehiclesByBrand();

    @Operation(
            summary = "Obter veículos da última semana",
            description = "Retorna os veículos cadastrados na última semana",
            responses = {@ApiResponse(responseCode = "200", description = "Veículos obtidos com sucesso")})
    @GetMapping(VEHICLES_STATISTICS_LAST_WEEK_V1)
    ResponseEntity<VehiclesRegisteredLastWeekResponse> getVehiclesRegisteredLastWeek();
}
