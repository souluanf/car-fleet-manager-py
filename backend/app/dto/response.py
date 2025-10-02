from pydantic import BaseModel, ConfigDict
from typing import Optional
from datetime import datetime
from uuid import UUID


class VehicleResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: UUID
    veiculo: str
    marca: str
    ano: int
    descricao: str
    cor: Optional[str]
    vendido: bool
    created: datetime
    updated: Optional[datetime]


class BrandResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: UUID
    name: str


class VehicleStatisticsResponse(BaseModel):
    totalVehicles: int
    unsoldVehicles: int
    soldVehicles: int


class VehiclesByDecadeResponse(BaseModel):
    vehiclesByDecade: dict[str, int]


class VehiclesByBrandResponse(BaseModel):
    vehiclesByBrand: dict[str, int]


class VehiclesRegisteredLastWeekResponse(BaseModel):
    vehicles: list[VehicleResponse]
    total: int


class BubbleSortResponse(BaseModel):
    vetorOriginal: list[int]
    vetorOrdenado: list[int]


class FatorialResponse(BaseModel):
    numero: int
    fatorial: int


class MultiploResponse(BaseModel):
    numeroLimite: int
    somaMultiplos: int


class VotingPercentageResponse(BaseModel):
    percentualVotosValidos: float
    percentualVotosBrancos: float
    percentualVotosNulos: float
