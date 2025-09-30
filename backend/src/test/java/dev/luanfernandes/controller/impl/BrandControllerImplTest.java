package dev.luanfernandes.controller.impl;

import static dev.luanfernandes.constants.PathConstants.VEHICLES_BRANDS_V1;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luanfernandes.config.web.ExceptionHandlerAdvice;
import dev.luanfernandes.dto.response.BrandResponse;
import dev.luanfernandes.service.BrandService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for BrandControllerImpl")
class BrandControllerImplTest {

    private MockMvc mockMvc;

    @Mock
    private BrandService brandService;

    @InjectMocks
    private BrandControllerImpl brandController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(brandController)
                .setControllerAdvice(new ExceptionHandlerAdvice())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    @DisplayName("Should return 200 with all brands")
    void shouldReturnOk_WhenGettingAllBrands() throws Exception {
        var brands = List.of(
                new BrandResponse(UUID.randomUUID(), "Fiat"),
                new BrandResponse(UUID.randomUUID(), "Volkswagen"),
                new BrandResponse(UUID.randomUUID(), "Ford"));

        when(brandService.getAllBrands()).thenReturn(brands);

        mockMvc.perform(get(VEHICLES_BRANDS_V1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Fiat"))
                .andExpect(jsonPath("$[1].name").value("Volkswagen"))
                .andExpect(jsonPath("$[2].name").value("Ford"));

        verify(brandService).getAllBrands();
    }

    @Test
    @DisplayName("Should return 200 with empty list when no brands found")
    void shouldReturnOk_WhenNoBrandsFound() throws Exception {
        when(brandService.getAllBrands()).thenReturn(List.of());

        mockMvc.perform(get(VEHICLES_BRANDS_V1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(brandService).getAllBrands();
    }
}
