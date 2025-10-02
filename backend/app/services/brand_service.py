from sqlalchemy.orm import Session
from app.repositories.brand_repository import BrandRepository
from app.dto.response import BrandResponse
from app.domain.brand import Brand
from typing import List
import uuid


class BrandService:
    def __init__(self, db: Session):
        self.brand_repository = BrandRepository(db)

    def get_all_brands(self) -> List[BrandResponse]:
        brands = self.brand_repository.find_all()
        return [
            BrandResponse(
                id=uuid.UUID(bytes=brand.id),
                name=brand.name
            )
            for brand in brands
        ]

    def is_brand_valid(self, brand_name: str) -> bool:
        return self.brand_repository.exists_by_name(brand_name)

    def find_brand_by_name(self, brand_name: str) -> Brand:
        return self.brand_repository.find_by_name(brand_name)
