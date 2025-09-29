package dev.luanfernandes.service;

import dev.luanfernandes.domain.entity.Brand;
import dev.luanfernandes.dto.response.BrandResponse;
import java.util.List;

public interface BrandService {
    List<BrandResponse> getAllBrands();

    boolean isBrandValid(String brandName);

    Brand findBrandByName(String brandName);
}
