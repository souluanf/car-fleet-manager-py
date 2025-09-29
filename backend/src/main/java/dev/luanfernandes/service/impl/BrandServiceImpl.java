package dev.luanfernandes.service.impl;

import dev.luanfernandes.domain.entity.Brand;
import dev.luanfernandes.dto.mapper.BrandMapper;
import dev.luanfernandes.dto.response.BrandResponse;
import dev.luanfernandes.repository.BrandRepository;
import dev.luanfernandes.service.BrandService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BrandResponse> getAllBrands() {
        return brandRepository.findAll().stream().map(brandMapper::toResponse).toList();
    }

    @Override
    public boolean isBrandValid(String brandName) {
        if (brandName == null || brandName.isBlank()) {
            return false;
        }
        return brandRepository.findByNameIgnoreCase(brandName).isPresent();
    }

    @Override
    public Brand findBrandByName(String brandName) {
        return brandRepository
                .findByNameIgnoreCase(brandName)
                .orElseThrow(() -> new IllegalArgumentException("Marca não encontrada: " + brandName));
    }
}
