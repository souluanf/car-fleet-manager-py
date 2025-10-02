from sqlalchemy.orm import Session
from sqlalchemy import select, func, and_
from app.domain.vehicle import Vehicle
from app.domain.brand import Brand
from typing import List, Optional, Union
from uuid import UUID
from datetime import datetime, timedelta


class VehicleRepository:
    def __init__(self, db: Session):
        self.db = db

    def save(self, vehicle: Vehicle) -> Vehicle:
        self.db.add(vehicle)
        self.db.commit()
        self.db.refresh(vehicle)
        return vehicle

    def find_by_id(self, vehicle_id: Union[UUID, bytes]) -> Optional[Vehicle]:
        if isinstance(vehicle_id, UUID):
            vehicle_id_bytes = vehicle_id.bytes
        elif isinstance(vehicle_id, bytes):
            vehicle_id_bytes = vehicle_id
        else:
            vehicle_id_bytes = UUID(vehicle_id).bytes

        stmt = select(Vehicle).where(Vehicle.id == vehicle_id_bytes)
        return self.db.execute(stmt).scalar_one_or_none()

    def find_all(self) -> List[Vehicle]:
        stmt = select(Vehicle)
        return self.db.execute(stmt).scalars().all()

    def delete(self, vehicle: Vehicle) -> None:
        self.db.delete(vehicle)
        self.db.commit()

    def find_by_filters(
        self,
        veiculo: Optional[str] = None,
        marca: Optional[str] = None,
        ano: Optional[int] = None,
        cor: Optional[str] = None
    ) -> List[Vehicle]:
        stmt = select(Vehicle).join(Brand)

        if veiculo:
            stmt = stmt.where(Vehicle.veiculo.ilike(f"%{veiculo}%"))
        if marca:
            stmt = stmt.where(Brand.name.ilike(f"%{marca}%"))
        if ano:
            stmt = stmt.where(Vehicle.ano == ano)
        if cor:
            stmt = stmt.where(Vehicle.cor.ilike(f"%{cor}%"))

        return self.db.execute(stmt).scalars().all()

    def count_total(self) -> int:
        stmt = select(func.count(Vehicle.id))
        return self.db.execute(stmt).scalar()

    def count_all(self) -> int:
        return self.count_total()

    def count_sold(self) -> int:
        stmt = select(func.count(Vehicle.id)).where(Vehicle.vendido == True)
        return self.db.execute(stmt).scalar()

    def count_unsold(self) -> int:
        stmt = select(func.count(Vehicle.id)).where(Vehicle.vendido == False)
        return self.db.execute(stmt).scalar()

    def count_by_sold_status(self, sold: bool) -> int:
        if sold:
            return self.count_sold()
        return self.count_unsold()

    def find_by_decade(self, decade: str) -> List[Vehicle]:
        decade_start = int(decade)
        decade_end = decade_start + 10
        stmt = select(Vehicle).where(
            and_(Vehicle.ano >= decade_start, Vehicle.ano < decade_end)
        )
        return self.db.execute(stmt).scalars().all()

    def count_by_decade(self) -> List[tuple]:
        stmt = (
            select(
                func.concat(func.floor(Vehicle.ano / 10) * 10, 's').label('decade'),
                func.count(Vehicle.id).label('count')
            )
            .group_by('decade')
            .order_by('decade')
        )
        return self.db.execute(stmt).all()

    def count_by_brand(self) -> List[tuple]:
        stmt = (
            select(
                Brand.name.label('brand'),
                func.count(Vehicle.id).label('count')
            )
            .join(Brand, Vehicle.brand_id == Brand.id)
            .group_by(Brand.name)
            .order_by(func.count(Vehicle.id).desc())
        )
        return self.db.execute(stmt).all()

    def find_registered_last_week(self) -> List[Vehicle]:
        one_week_ago = datetime.now() - timedelta(days=7)
        stmt = select(Vehicle).where(Vehicle.created >= one_week_ago)
        return self.db.execute(stmt).scalars().all()
