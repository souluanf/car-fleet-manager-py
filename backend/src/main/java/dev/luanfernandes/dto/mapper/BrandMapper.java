package dev.luanfernandes.dto.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import dev.luanfernandes.domain.entity.Brand;
import dev.luanfernandes.dto.response.BrandResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = SPRING)
public interface BrandMapper {

    BrandResponse toResponse(Brand source);
}
