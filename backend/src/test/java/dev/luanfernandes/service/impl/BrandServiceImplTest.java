package dev.luanfernandes.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.luanfernandes.domain.entity.Brand;
import dev.luanfernandes.dto.mapper.BrandMapper;
import dev.luanfernandes.dto.response.BrandResponse;
import dev.luanfernandes.repository.BrandRepository;
import java.util.Arrays;
import java.util.Collections;
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
@DisplayName("Tests for BrandServiceImpl")
class BrandServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private BrandServiceImpl brandService;

    @Test
    @DisplayName("Should get all brands from database")
    void shouldGetAllBrands_FromDatabase() {
        var brands = createMockBrandEntities();
        var brandResponses = List.of(
                new BrandResponse(UUID.randomUUID(), "Fiat"),
                new BrandResponse(UUID.randomUUID(), "Chevrolet"),
                new BrandResponse(UUID.randomUUID(), "Volkswagen"));

        when(brandRepository.findAll()).thenReturn(brands);
        when(brandMapper.toResponse(brands.get(0))).thenReturn(brandResponses.get(0));
        when(brandMapper.toResponse(brands.get(1))).thenReturn(brandResponses.get(1));
        when(brandMapper.toResponse(brands.get(2))).thenReturn(brandResponses.get(2));

        var result = brandService.getAllBrands();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).name()).isEqualTo("Fiat");
        assertThat(result.get(1).name()).isEqualTo("Chevrolet");
        assertThat(result.get(2).name()).isEqualTo("Volkswagen");
        verify(brandRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no brands found")
    void shouldReturnEmptyList_WhenNoBrandsFound() {
        when(brandRepository.findAll()).thenReturn(Collections.emptyList());

        var result = brandService.getAllBrands();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should validate brand successfully when exists")
    void shouldValidateBrand_WhenExists() {
        var brand = new Brand(UUID.randomUUID(), "Volkswagen");
        when(brandRepository.findByNameIgnoreCase("Volkswagen")).thenReturn(Optional.of(brand));

        boolean result = brandService.isBrandValid("Volkswagen");

        assertThat(result).isTrue();
        verify(brandRepository).findByNameIgnoreCase("Volkswagen");
    }

    @Test
    @DisplayName("Should return false when brand not found")
    void shouldReturnFalse_WhenBrandNotFound() {
        when(brandRepository.findByNameIgnoreCase("InvalidBrand")).thenReturn(Optional.empty());

        boolean result = brandService.isBrandValid("InvalidBrand");

        assertThat(result).isFalse();
        verify(brandRepository).findByNameIgnoreCase("InvalidBrand");
    }

    @Test
    @DisplayName("Should return false when brand name is null")
    void shouldReturnFalse_WhenBrandNameIsNull() {
        boolean result = brandService.isBrandValid(null);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return false when brand name is blank")
    void shouldReturnFalse_WhenBrandNameIsBlank() {
        boolean result = brandService.isBrandValid("   ");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should find brand by name successfully")
    void shouldFindBrandByName_Successfully() {
        var brand = new Brand(UUID.randomUUID(), "Fiat");
        when(brandRepository.findByNameIgnoreCase("Fiat")).thenReturn(Optional.of(brand));

        var result = brandService.findBrandByName("Fiat");

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Fiat");
        verify(brandRepository).findByNameIgnoreCase("Fiat");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when brand not found by name")
    void shouldThrowException_WhenBrandNotFoundByName() {
        when(brandRepository.findByNameIgnoreCase("InvalidBrand")).thenReturn(Optional.empty());

        var exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class, () -> brandService.findBrandByName("InvalidBrand"));

        assertThat(exception.getMessage()).isEqualTo("Marca não encontrada: InvalidBrand");
        verify(brandRepository).findByNameIgnoreCase("InvalidBrand");
    }

    private List<Brand> createMockBrandEntities() {
        return Arrays.asList(
                new Brand(UUID.randomUUID(), "Fiat"),
                new Brand(UUID.randomUUID(), "Chevrolet"),
                new Brand(UUID.randomUUID(), "Volkswagen"));
    }
}
