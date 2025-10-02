import pytest


class TestBrandRouter:
    """Testes de integração para BrandRouter"""

    def test_get_all_brands_empty(self, client):
        """Testa GET /api/v1/brands quando não há marcas"""
        response = client.get("/car-fleet-manager/api/v1/brands")

        assert response.status_code == 200
        assert response.json() == []

    def test_get_all_brands(self, client, multiple_brands):
        """Testa GET /api/v1/brands com múltiplas marcas"""
        response = client.get("/car-fleet-manager/api/v1/brands")

        assert response.status_code == 200
        brands = response.json()
        assert len(brands) == 5
        brand_names = [b["name"] for b in brands]
        assert "Fiat" in brand_names
        assert "Ford" in brand_names

    def test_get_all_brands_returns_sorted(self, client, multiple_brands):
        """Testa se as marcas retornam ordenadas por nome"""
        response = client.get("/car-fleet-manager/api/v1/brands")

        assert response.status_code == 200
        brands = response.json()
        brand_names = [b["name"] for b in brands]
        assert brand_names == sorted(brand_names)
