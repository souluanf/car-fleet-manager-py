from pydantic import BaseModel, Field, field_validator
from typing import Optional
from datetime import datetime


class VehicleRequest(BaseModel):
    veiculo: str = Field(..., min_length=1, max_length=100, description="Nome do veículo", examples=["Gol"])
    marca: str = Field(..., min_length=1, max_length=100, description="Marca do veículo", examples=["Volkswagen"])
    ano: int = Field(..., ge=1900, description="Ano do veículo", examples=[2020])
    descricao: str = Field(..., min_length=1, max_length=1000, description="Descrição do veículo", examples=["Carro compacto, econômico e ideal para cidade"])
    cor: Optional[str] = Field(None, max_length=50, description="Cor do veículo", examples=["Branco"])
    vendido: bool = Field(..., description="Status de vendido", examples=[False])

    @field_validator('veiculo', 'marca', 'descricao')
    @classmethod
    def not_empty(cls, v: str) -> str:
        if not v or not v.strip():
            raise ValueError('Campo não pode estar vazio')
        return v


class VehicleUpdateRequest(BaseModel):
    veiculo: Optional[str] = Field(None, min_length=1, max_length=100)
    marca: Optional[str] = Field(None, min_length=1, max_length=100)
    ano: Optional[int] = Field(None, ge=1900)
    descricao: Optional[str] = Field(None, min_length=1, max_length=1000)
    cor: Optional[str] = Field(None, max_length=50)
    vendido: Optional[bool] = None


class BubbleSortRequest(BaseModel):
    vetor: list[int] = Field(..., min_length=1, description="Vetor de números para ordenar", examples=[[5, 3, 8, 1, 2, 50, 7]])


class FatorialRequest(BaseModel):
    numero: int = Field(..., ge=0, description="Número para calcular fatorial", examples=[5])


class MultiploRequest(BaseModel):
    numero: int = Field(..., ge=1, description="Número limite para calcular múltiplos", examples=[10])


class VotingDataRequest(BaseModel):
    totalEleitores: int = Field(..., gt=0, description="Total de eleitores", examples=[1000])
    votosValidos: int = Field(..., ge=0, description="Votos válidos", examples=[800])
    votosBrancos: int = Field(..., ge=0, description="Votos em branco", examples=[150])
    votosNulos: int = Field(..., ge=0, description="Votos nulos", examples=[50])

    @field_validator('votosBrancos', 'votosNulos', 'votosValidos')
    @classmethod
    def validate_votes(cls, v: int, info) -> int:
        if 'totalEleitores' in info.data and v > info.data['totalEleitores']:
            raise ValueError('Número de votos não pode exceder o total de eleitores')
        return v
