from sqlalchemy.orm import Session
from app.repositories.vehicle_repository import VehicleRepository
from app.services.brand_service import BrandService
from app.dto.request import VehicleRequest, VehicleUpdateRequest
from app.dto.response import (
    VehicleResponse,
    VehicleStatisticsResponse,
    VehiclesByDecadeResponse,
    VehiclesByBrandResponse,
    VehiclesRegisteredLastWeekResponse
)
from app.domain.vehicle import Vehicle
from app.exceptions.business_exceptions import VehicleNotFoundException, InvalidBrandException
from typing import List, Optional
from uuid import UUID
import uuid


class VehicleService:
    def __init__(self, db: Session):
        self.db = db
        self.vehicle_repository = VehicleRepository(db)
        self.brand_service = BrandService(db)

    def create_vehicle(self, request: VehicleRequest) -> VehicleResponse:
        self._validate_brand(request.marca)
        brand = self.brand_service.find_brand_by_name(request.marca)

        vehicle = Vehicle(
            veiculo=request.veiculo,
            brand_id=brand.id,
            ano=request.ano,
            descricao=request.descricao,
            cor=request.cor,
            vendido=request.vendido
        )

        saved_vehicle = self.vehicle_repository.save(vehicle)
        return self._to_response(saved_vehicle)

    def update_vehicle(self, vehicle_id: UUID, request: VehicleRequest) -> VehicleResponse:
        self._validate_brand(request.marca)
        brand = self.brand_service.find_brand_by_name(request.marca)
        vehicle = self._find_vehicle_by_id(vehicle_id)

        vehicle.veiculo = request.veiculo
        vehicle.brand_id = brand.id
        vehicle.ano = request.ano
        vehicle.descricao = request.descricao
        vehicle.cor = request.cor
        vehicle.vendido = request.vendido

        updated_vehicle = self.vehicle_repository.save(vehicle)
        return self._to_response(updated_vehicle)

    def partial_update_vehicle(self, vehicle_id: UUID, request: VehicleUpdateRequest) -> VehicleResponse:
        vehicle = self._find_vehicle_by_id(vehicle_id)

        if request.marca is not None:
            self._validate_brand(request.marca)
            brand = self.brand_service.find_brand_by_name(request.marca)
            vehicle.brand_id = brand.id

        if request.veiculo is not None:
            vehicle.veiculo = request.veiculo
        if request.ano is not None:
            vehicle.ano = request.ano
        if request.descricao is not None:
            vehicle.descricao = request.descricao
        if request.cor is not None:
            vehicle.cor = request.cor
        if request.vendido is not None:
            vehicle.vendido = request.vendido

        updated_vehicle = self.vehicle_repository.save(vehicle)
        return self._to_response(updated_vehicle)

    def patch_vehicle(self, vehicle_id: UUID) -> VehicleResponse:
        """Marca veículo como vendido"""
        vehicle = self._find_vehicle_by_id(vehicle_id)
        vehicle.vendido = True
        updated_vehicle = self.vehicle_repository.save(vehicle)
        return self._to_response(updated_vehicle)

    def delete_vehicle(self, vehicle_id: UUID) -> None:
        vehicle = self._find_vehicle_by_id(vehicle_id)
        self.vehicle_repository.delete(vehicle)

    def get_vehicle_by_id(self, vehicle_id: UUID) -> VehicleResponse:
        vehicle = self._find_vehicle_by_id(vehicle_id)
        return self._to_response(vehicle)

    def get_all_vehicles(self) -> List[VehicleResponse]:
        vehicles = self.vehicle_repository.find_all()
        return [self._to_response(vehicle) for vehicle in vehicles]

    def get_vehicles_by_filters(
        self,
        veiculo: Optional[str] = None,
        marca: Optional[str] = None,
        ano: Optional[int] = None,
        cor: Optional[str] = None
    ) -> List[VehicleResponse]:
        vehicles = self.vehicle_repository.find_by_filters(veiculo, marca, ano, cor)
        return [self._to_response(vehicle) for vehicle in vehicles]

    def get_statistics(self) -> VehicleStatisticsResponse:
        total = self.vehicle_repository.count_all()
        unsold = self.vehicle_repository.count_unsold()
        sold = self.vehicle_repository.count_sold()

        return VehicleStatisticsResponse(
            totalVehicles=total,
            unsoldVehicles=unsold,
            soldVehicles=sold
        )

    def get_vehicles_by_decade(self) -> VehiclesByDecadeResponse:
        results = self.vehicle_repository.count_by_decade()
        vehicles_by_decade = dict(results)
        return VehiclesByDecadeResponse(vehiclesByDecade=vehicles_by_decade)

    def get_vehicles_by_brand(self) -> VehiclesByBrandResponse:
        results = self.vehicle_repository.count_by_brand()
        vehicles_by_brand = dict(results)
        return VehiclesByBrandResponse(vehiclesByBrand=vehicles_by_brand)

    def get_vehicles_registered_last_week(self) -> VehiclesRegisteredLastWeekResponse:
        vehicles = self.vehicle_repository.find_registered_last_week()
        vehicle_responses = [self._to_response(vehicle) for vehicle in vehicles]
        return VehiclesRegisteredLastWeekResponse(
            vehicles=vehicle_responses,
            total=len(vehicle_responses)
        )

    def _find_vehicle_by_id(self, vehicle_id: UUID) -> Vehicle:
        vehicle = self.vehicle_repository.find_by_id(vehicle_id)
        if not vehicle:
            raise VehicleNotFoundException(str(vehicle_id))
        return vehicle

    def _validate_brand(self, brand_name: str) -> None:
        if not self.brand_service.is_brand_valid(brand_name):
            raise InvalidBrandException(brand_name)

    def _to_response(self, vehicle: Vehicle) -> VehicleResponse:
        return VehicleResponse(
            id=uuid.UUID(bytes=vehicle.id),
            veiculo=vehicle.veiculo,
            marca=vehicle.brand.name,
            ano=vehicle.ano,
            descricao=vehicle.descricao,
            cor=vehicle.cor,
            vendido=vehicle.vendido,
            created=vehicle.created,
            updated=vehicle.updated
        )
