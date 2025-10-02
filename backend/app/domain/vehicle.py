from sqlalchemy import Column, String, Integer, Boolean, Text, ForeignKey, DateTime
from sqlalchemy.dialects.mysql import BINARY
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from app.config.database import Base
import uuid


class Vehicle(Base):
    __tablename__ = "vehicles"

    id = Column(BINARY(16), primary_key=True, default=lambda: uuid.uuid4().bytes)
    veiculo = Column(String(255), nullable=False)
    brand_id = Column(BINARY(16), ForeignKey("brands.id"), nullable=False)
    ano = Column(Integer, nullable=False)
    descricao = Column(Text, nullable=False)
    cor = Column(String(255), nullable=True)
    vendido = Column(Boolean, nullable=False, default=False)
    created = Column(DateTime, nullable=False, server_default=func.now())
    updated = Column(DateTime, nullable=True, onupdate=func.now())

    brand = relationship("Brand", back_populates="vehicles")
