import pytest
from app.services.brand_service import BrandService
from app.repositories.brand_repository import BrandRepository


class TestBrandService:
    """Testes unitários para BrandService"""

    @pytest.fixture
    def brand_service(self, test_db):
        """Fixture para criar instância do BrandService"""
        return BrandService(test_db)

    def test_get_all_brands_empty(self, brand_service):
        """Testa buscar todas as marcas quando não há nenhuma"""
        brands = brand_service.get_all_brands()
        assert len(brands) == 0

    def test_get_all_brands(self, brand_service, multiple_brands):
        """Testa buscar todas as marcas"""
        brands = brand_service.get_all_brands()
        assert len(brands) == 5
        assert brands[0].name in ["Fiat", "Ford", "Chevrolet", "Volkswagen", "Toyota"]

    def test_is_brand_valid_true(self, brand_service, sample_brand):
        """Testa validação de marca válida"""
        assert brand_service.is_brand_valid("Fiat") is True

    def test_is_brand_valid_false(self, brand_service):
        """Testa validação de marca inválida"""
        assert brand_service.is_brand_valid("MarcaInexistente") is False

    def test_is_brand_valid_case_insensitive(self, brand_service, sample_brand):
        """Testa validação de marca ignorando case"""
        assert brand_service.is_brand_valid("fiat") is True
        assert brand_service.is_brand_valid("FIAT") is True
        assert brand_service.is_brand_valid("FiAt") is True
