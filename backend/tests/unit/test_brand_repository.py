import pytest
import uuid
from app.repositories.brand_repository import BrandRepository
from app.domain.brand import Brand


class TestBrandRepository:
    """Testes unitários para BrandRepository"""

    @pytest.fixture
    def brand_repository(self, test_db):
        """Fixture para criar instância do BrandRepository"""
        return BrandRepository(test_db)

    def test_find_all_empty(self, brand_repository):
        """Testa buscar todas as marcas quando não há nenhuma"""
        brands = brand_repository.find_all()
        assert len(brands) == 0

    def test_find_all(self, brand_repository, multiple_brands):
        """Testa buscar todas as marcas"""
        brands = brand_repository.find_all()
        assert len(brands) == 5

    def test_find_by_name(self, brand_repository, sample_brand):
        """Testa buscar marca por nome"""
        brand = brand_repository.find_by_name("Fiat")
        assert brand is not None
        assert brand.name == "Fiat"

    def test_find_by_name_not_found(self, brand_repository):
        """Testa buscar marca inexistente"""
        brand = brand_repository.find_by_name("MarcaInexistente")
        assert brand is None

    def test_find_by_name_case_insensitive(self, brand_repository, sample_brand):
        """Testa busca case-insensitive"""
        brand_lower = brand_repository.find_by_name("fiat")
        brand_upper = brand_repository.find_by_name("FIAT")
        brand_mixed = brand_repository.find_by_name("FiAt")

        assert brand_lower is not None
        assert brand_upper is not None
        assert brand_mixed is not None
        assert brand_lower.name == "Fiat"
