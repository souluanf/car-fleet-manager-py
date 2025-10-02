from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from typing import List

from app.config.database import get_db
from app.services.brand_service import BrandService
from app.dto.response import BrandResponse

router = APIRouter(prefix="/api/v1/brands", tags=["Brands"])


@router.get(
    "",
    response_model=List[BrandResponse],
    summary="Listar todas as marcas disponíveis",
    description="Retorna todas as marcas de veículos cadastradas no sistema",
    operation_id="getAllBrands",
    responses={
        200: {"description": "Marcas obtidas com sucesso"}
    }
)
def get_all_brands(db: Session = Depends(get_db)):
    service = BrandService(db)
    return service.get_all_brands()
