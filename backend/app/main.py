from fastapi import FastAPI, APIRouter, Request, status
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from app.config.settings import settings
from app.routers import vehicle_router, brand_router, exercises_router
from app.exceptions.business_exceptions import BusinessException
from alembic.config import Config
from alembic import command
import logging

logger = logging.getLogger(__name__)

tags_metadata = [
    {
        "name": "Brands",
        "description": "Listagem de marcas disponíveis"
    },
    {
        "name": "Vehicles",
        "description": "Endpoints para gerenciamento de veículos"
    },
    {
        "name": "Exercises",
        "description": "Endpoints para exercícios de lógica de programação"
    }
]

app = FastAPI(
    title="Car Fleet API",
    description="API para gerenciamento de frotas de veículos.",
    version="0.0.1",
    contact={
        "name": "Luan Fernandes",
        "url": "https://linkedin.com/in/souluanf",
        "email": "contact@luanfernandes.dev"
    },
    openapi_tags=tags_metadata,
    docs_url="/car-fleet-manager/swagger-ui",
    redoc_url="/car-fleet-manager/redoc",
    openapi_url="/car-fleet-manager/v3/api-docs",
    servers=[
        {"url": "http://localhost:8080/car-fleet-manager"},
        {"url": "http://127.0.0.1:8080/car-fleet-manager"}
    ]
)

# Adicionar externalDocs manualmente ao schema OpenAPI
from fastapi.openapi.utils import get_openapi

def custom_openapi():
    if app.openapi_schema:
        return app.openapi_schema

    openapi_schema = get_openapi(
        title=app.title,
        version=app.version,
        description=app.description,
        routes=app.routes,
        tags=app.openapi_tags,
        servers=app.servers,
        contact=app.contact
    )

    openapi_schema["externalDocs"] = {
        "description": "Git repository",
        "url": "https://github.com/souluanf/car-fleet-manager"
    }

    # Remover o prefixo /car-fleet-manager dos paths
    new_paths = {}
    for path, path_item in openapi_schema["paths"].items():
        new_path = path.replace("/car-fleet-manager", "")
        new_paths[new_path] = path_item
    openapi_schema["paths"] = new_paths

    app.openapi_schema = openapi_schema
    return app.openapi_schema

app.openapi = custom_openapi


@app.on_event("startup")
async def run_migrations():
    """Executa as migrations do Alembic na inicialização da aplicação"""
    try:
        logger.info("Executando migrations do Alembic...")
        alembic_cfg = Config("alembic.ini")
        command.upgrade(alembic_cfg, "head")
        logger.info("Migrations executadas com sucesso!")
    except Exception as e:
        logger.error(f"Erro ao executar migrations: {e}")
        raise


app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.cors_origins_list,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.exception_handler(BusinessException)
async def business_exception_handler(request: Request, exc: BusinessException):
    return JSONResponse(
        status_code=exc.status_code,
        content={"message": exc.message}
    )


@app.exception_handler(ValueError)
async def value_error_handler(request: Request, exc: ValueError):
    return JSONResponse(
        status_code=status.HTTP_400_BAD_REQUEST,
        content={"message": str(exc)}
    )


# Router principal com prefixo /car-fleet-manager
main_router = APIRouter(prefix="/car-fleet-manager")

main_router.include_router(brand_router.router)
main_router.include_router(vehicle_router.router)
main_router.include_router(exercises_router.router)

app.include_router(main_router)


@app.get("/car-fleet-manager/health", include_in_schema=False)
def health_check():
    return {"status": "UP"}
