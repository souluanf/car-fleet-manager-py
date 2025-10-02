from sqlalchemy import Column, String
from sqlalchemy.dialects.mysql import BINARY
from sqlalchemy.orm import relationship
from app.config.database import Base
import uuid


class Brand(Base):
    __tablename__ = "brands"

    id = Column(BINARY(16), primary_key=True, default=lambda: uuid.uuid4().bytes)
    name = Column(String(100), nullable=False, unique=True)

    vehicles = relationship("Vehicle", back_populates="brand")
