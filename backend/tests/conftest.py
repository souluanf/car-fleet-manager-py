import pytest
import uuid
from datetime import datetime
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.pool import StaticPool
from fastapi.testclient import TestClient

from app.config.database import Base, get_db
from app.domain.brand import Brand
from app.domain.vehicle import Vehicle


@pytest.fixture(scope="function")
def test_db():
    """Cria um banco de dados em memória para testes"""
    engine = create_engine(
        "sqlite:///:memory:",
        connect_args={"check_same_thread": False},
        poolclass=StaticPool,
    )
    Base.metadata.create_all(bind=engine)
    TestingSessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

    db = TestingSessionLocal()
    try:
        yield db
    finally:
        db.close()
        Base.metadata.drop_all(bind=engine)


@pytest.fixture(scope="function")
def client(test_db):
    """Cliente de teste do FastAPI"""
    from fastapi import FastAPI, APIRouter, Request, status
    from fastapi.responses import JSONResponse
    from app.routers import vehicle_router, brand_router, exercises_router
    from app.exceptions.business_exceptions import BusinessException

    # Cria uma nova instância do app para testes sem lifecycle events
    test_app = FastAPI()

    # Adiciona exception handlers
    @test_app.exception_handler(BusinessException)
    async def business_exception_handler(request: Request, exc: BusinessException):
        return JSONResponse(
            status_code=exc.status_code,
            content={"message": exc.message}
        )

    @test_app.exception_handler(ValueError)
    async def value_error_handler(request: Request, exc: ValueError):
        return JSONResponse(
            status_code=status.HTTP_400_BAD_REQUEST,
            content={"message": str(exc)}
        )

    # Adiciona routers
    main_router = APIRouter(prefix="/car-fleet-manager")
    main_router.include_router(brand_router.router)
    main_router.include_router(vehicle_router.router)
    main_router.include_router(exercises_router.router)
    test_app.include_router(main_router)

    # Override da dependência do banco
    def override_get_db():
        try:
            yield test_db
        finally:
            pass

    test_app.dependency_overrides[get_db] = override_get_db

    with TestClient(test_app) as test_client:
        yield test_client

    test_app.dependency_overrides.clear()


@pytest.fixture
def sample_brand(test_db):
    """Cria uma marca de exemplo"""
    brand = Brand(
        id=uuid.uuid4().bytes,
        name="Fiat"
    )
    test_db.add(brand)
    test_db.commit()
    test_db.refresh(brand)
    return brand


@pytest.fixture
def sample_vehicle(test_db, sample_brand):
    """Cria um veículo de exemplo"""
    vehicle = Vehicle(
        id=uuid.uuid4().bytes,
        veiculo="Uno",
        brand_id=sample_brand.id,
        ano=2020,
        descricao="Carro popular",
        cor="Branco",
        vendido=False
    )
    test_db.add(vehicle)
    test_db.commit()
    test_db.refresh(vehicle)
    return vehicle


@pytest.fixture
def multiple_brands(test_db):
    """Cria múltiplas marcas"""
    brands = []
    brand_names = ["Fiat", "Ford", "Chevrolet", "Volkswagen", "Toyota"]

    for name in brand_names:
        brand = Brand(
            id=uuid.uuid4().bytes,
            name=name
        )
        test_db.add(brand)
        brands.append(brand)

    test_db.commit()
    return brands


@pytest.fixture
def multiple_vehicles(test_db, sample_brand):
    """Cria múltiplos veículos"""
    vehicles = []
    vehicle_data = [
        {"veiculo": "Uno", "ano": 2020, "descricao": "Compacto", "cor": "Branco", "vendido": False},
        {"veiculo": "Palio", "ano": 2015, "descricao": "Hatch", "cor": "Preto", "vendido": True},
        {"veiculo": "Argo", "ano": 2022, "descricao": "Moderno", "cor": "Vermelho", "vendido": False},
    ]

    for data in vehicle_data:
        vehicle = Vehicle(
            id=uuid.uuid4().bytes,
            brand_id=sample_brand.id,
            **data
        )
        test_db.add(vehicle)
        vehicles.append(vehicle)

    test_db.commit()
    return vehicles
