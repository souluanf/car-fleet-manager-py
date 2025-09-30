package dev.luanfernandes.controller;

import static dev.luanfernandes.constants.PathConstants.VEHICLES_BRANDS_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import dev.luanfernandes.dto.response.BrandResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Brands", description = "Listagem de marcas disponíveis")
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public interface BrandController {

    @Operation(
            summary = "Listar todas as marcas disponíveis",
            description = "Retorna todas as marcas de veículos cadastradas no sistema",
            responses = {@ApiResponse(responseCode = "200", description = "Marcas obtidas com sucesso")})
    @GetMapping(VEHICLES_BRANDS_V1)
    ResponseEntity<List<BrandResponse>> getAllBrands();
}
