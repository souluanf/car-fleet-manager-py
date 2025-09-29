package dev.luanfernandes.dto.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

import dev.luanfernandes.domain.entity.Vehicle;
import dev.luanfernandes.dto.request.VehicleRequest;
import dev.luanfernandes.dto.request.VehicleUpdateRequest;
import dev.luanfernandes.dto.response.VehicleResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = SPRING)
public interface VehicleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "brand", ignore = true)
    Vehicle toEntity(VehicleRequest response);

    @Mapping(target = "marca", source = "brand.name")
    VehicleResponse toResponse(Vehicle vehicle);

    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "brand", ignore = true)
    void update(VehicleRequest request, @MappingTarget Vehicle vehicle);

    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void partialUpdate(VehicleUpdateRequest request, @MappingTarget Vehicle vehicle);
}
