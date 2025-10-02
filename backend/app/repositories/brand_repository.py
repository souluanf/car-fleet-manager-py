from sqlalchemy.orm import Session
from sqlalchemy import select, func
from app.domain.brand import Brand
from typing import List, Optional


class BrandRepository:
    def __init__(self, db: Session):
        self.db = db

    def find_all(self) -> List[Brand]:
        stmt = select(Brand).order_by(Brand.name)
        return self.db.execute(stmt).scalars().all()

    def find_by_name(self, name: str) -> Optional[Brand]:
        stmt = select(Brand).where(func.lower(Brand.name) == func.lower(name))
        return self.db.execute(stmt).scalar_one_or_none()

    def exists_by_name(self, name: str) -> bool:
        return self.find_by_name(name) is not None
