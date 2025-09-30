package dev.luanfernandes.controller.impl;

import dev.luanfernandes.controller.BrandController;
import dev.luanfernandes.dto.response.BrandResponse;
import dev.luanfernandes.service.BrandService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BrandControllerImpl implements BrandController {

    private final BrandService brandService;

    @Override
    public ResponseEntity<List<BrandResponse>> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }
}
