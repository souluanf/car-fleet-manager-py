from fastapi import APIRouter, Depends, status, Query
from sqlalchemy.orm import Session
from typing import List, Optional
from uuid import UUID

from app.config.database import get_db
from app.services.vehicle_service import VehicleService
from app.dto.request import VehicleRequest, VehicleUpdateRequest
from app.dto.response import (
    VehicleResponse,
    VehicleStatisticsResponse,
    VehiclesByDecadeResponse,
    VehiclesByBrandResponse,
    VehiclesRegisteredLastWeekResponse
)

router = APIRouter(prefix="/api/v1/vehicles", tags=["Vehicles"])


@router.get(
    "",
    response_model=List[VehicleResponse],
    summary="Listar todos os veículos",
    description="Retorna todos os veículos cadastrados",
    operation_id="getAllVehicles",
    responses={
        200: {"description": "Lista de veículos retornada com sucesso"}
    }
)
def get_all_vehicles(db: Session = Depends(get_db)):
    service = VehicleService(db)
    return service.get_all_vehicles()


@router.get(
    "/search",
    response_model=List[VehicleResponse],
    summary="Buscar veículos com filtros",
    description="Busca veículos por marca, ano, cor ou nome",
    operation_id="searchVehicles",
    responses={
        200: {"description": "Veículos encontrados"}
    }
)
def search_vehicles(
    veiculo: Optional[str] = Query(None, description="Nome do veículo"),
    marca: Optional[str] = Query(None, description="Marca do veículo"),
    ano: Optional[int] = Query(None, description="Ano do veículo"),
    cor: Optional[str] = Query(None, description="Cor do veículo"),
    db: Session = Depends(get_db)
):
    service = VehicleService(db)
    return service.get_vehicles_by_filters(veiculo, marca, ano, cor)


@router.get(
    "/statistics",
    response_model=VehicleStatisticsResponse,
    summary="Obter estatísticas",
    description="Retorna quantidade de veículos vendidos e não vendidos",
    operation_id="getStatistics",
    responses={
        200: {"description": "Estatísticas obtidas com sucesso"}
    }
)
def get_statistics(db: Session = Depends(get_db)):
    service = VehicleService(db)
    return service.get_statistics()


@router.get(
    "/statistics/by-decade",
    response_model=VehiclesByDecadeResponse,
    summary="Obter distribuição por década",
    description="Retorna a quantidade de veículos por década de fabricação",
    operation_id="getVehiclesByDecade",
    responses={
        200: {"description": "Distribuição obtida com sucesso"}
    }
)
def get_vehicles_by_decade(db: Session = Depends(get_db)):
    service = VehicleService(db)
    return service.get_vehicles_by_decade()


@router.get(
    "/statistics/by-brand",
    response_model=VehiclesByBrandResponse,
    summary="Obter distribuição por fabricante",
    description="Retorna a quantidade de veículos por fabricante",
    operation_id="getVehiclesByBrand",
    responses={
        200: {"description": "Distribuição obtida com sucesso"}
    }
)
def get_vehicles_by_brand(db: Session = Depends(get_db)):
    service = VehicleService(db)
    return service.get_vehicles_by_brand()


@router.get(
    "/statistics/last-week",
    response_model=VehiclesRegisteredLastWeekResponse,
    summary="Obter veículos da última semana",
    description="Retorna os veículos cadastrados na última semana",
    operation_id="getVehiclesRegisteredLastWeek",
    responses={
        200: {"description": "Veículos obtidos com sucesso"}
    }
)
def get_vehicles_registered_last_week(db: Session = Depends(get_db)):
    service = VehicleService(db)
    return service.get_vehicles_registered_last_week()


@router.get(
    "/{id}",
    response_model=VehicleResponse,
    summary="Buscar veículo por ID",
    description="Retorna os detalhes de um veículo específico",
    operation_id="getVehicleById",
    responses={
        200: {"description": "Veículo encontrado"},
        404: {"description": "Veículo não encontrado"}
    }
)
def get_vehicle_by_id(id: UUID, db: Session = Depends(get_db)):
    service = VehicleService(db)
    return service.get_vehicle_by_id(id)


@router.post(
    "",
    response_model=VehicleResponse,
    status_code=status.HTTP_201_CREATED,
    summary="Cadastrar novo veículo",
    description="Adiciona um novo veículo ao sistema",
    operation_id="createVehicle",
    responses={
        201: {"description": "Veículo criado com sucesso"},
        400: {"description": "Dados inválidos"}
    }
)
def create_vehicle(request: VehicleRequest, db: Session = Depends(get_db)):
    service = VehicleService(db)
    return service.create_vehicle(request)


@router.put(
    "/{id}",
    response_model=VehicleResponse,
    summary="Atualizar veículo completo",
    description="Atualiza todos os dados de um veículo",
    operation_id="updateVehicle",
    responses={
        200: {"description": "Veículo atualizado com sucesso"},
        404: {"description": "Veículo não encontrado"},
        400: {"description": "Dados inválidos"}
    }
)
def update_vehicle(id: UUID, request: VehicleRequest, db: Session = Depends(get_db)):
    service = VehicleService(db)
    return service.update_vehicle(id, request)


@router.patch(
    "/{id}",
    response_model=VehicleResponse,
    summary="Marcar veículo como vendido",
    description="Marca um veículo como vendido",
    operation_id="patchVehicle",
    responses={
        200: {"description": "Veículo atualizado com sucesso"},
        404: {"description": "Veículo não encontrado"}
    }
)
def patch_vehicle(id: UUID, db: Session = Depends(get_db)):
    service = VehicleService(db)
    return service.patch_vehicle(id)


@router.delete(
    "/{id}",
    status_code=status.HTTP_204_NO_CONTENT,
    summary="Excluir veículo",
    description="Remove um veículo do sistema",
    operation_id="deleteVehicle",
    responses={
        204: {"description": "Veículo excluído com sucesso"},
        404: {"description": "Veículo não encontrado"}
    }
)
def delete_vehicle(id: UUID, db: Session = Depends(get_db)):
    service = VehicleService(db)
    service.delete_vehicle(id)
