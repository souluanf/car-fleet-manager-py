import pytest
import uuid


class TestVehicleRouter:
    """Testes de integração para VehicleRouter"""

    def test_create_vehicle_success(self, client, sample_brand):
        """Testa POST /api/v1/vehicles com sucesso"""
        vehicle_data = {
            "veiculo": "Uno",
            "marca": "Fiat",
            "ano": 2020,
            "descricao": "Carro popular",
            "cor": "Branco",
            "vendido": False
        }

        response = client.post("/car-fleet-manager/api/v1/vehicles", json=vehicle_data)

        assert response.status_code == 201
        data = response.json()
        assert data["veiculo"] == "Uno"
        assert data["marca"] == "Fiat"
        assert data["ano"] == 2020
        assert "id" in data

    def test_create_vehicle_invalid_brand(self, client):
        """Testa POST /api/v1/vehicles com marca inválida"""
        vehicle_data = {
            "veiculo": "Uno",
            "marca": "MarcaInvalida",
            "ano": 2020,
            "descricao": "Teste",
            "cor": "Branco",
            "vendido": False
        }

        response = client.post("/car-fleet-manager/api/v1/vehicles", json=vehicle_data)

        assert response.status_code == 400
        assert "MarcaInvalida" in response.json()["message"]

    def test_get_all_vehicles(self, client, multiple_vehicles):
        """Testa GET /api/v1/vehicles"""
        response = client.get("/car-fleet-manager/api/v1/vehicles")

        assert response.status_code == 200
        vehicles = response.json()
        assert len(vehicles) == 3

    def test_get_vehicle_by_id(self, client, sample_vehicle):
        """Testa GET /api/v1/vehicles/{id}"""
        vehicle_id = str(uuid.UUID(bytes=sample_vehicle.id))
        response = client.get(f"/car-fleet-manager/api/v1/vehicles/{vehicle_id}")

        assert response.status_code == 200
        data = response.json()
        assert data["veiculo"] == "Uno"

    def test_get_vehicle_by_id_not_found(self, client):
        """Testa GET /api/v1/vehicles/{id} com ID inexistente"""
        fake_id = str(uuid.uuid4())
        response = client.get(f"/car-fleet-manager/api/v1/vehicles/{fake_id}")

        assert response.status_code == 404

    def test_update_vehicle(self, client, sample_vehicle, sample_brand):
        """Testa PUT /api/v1/vehicles/{id}"""
        vehicle_id = str(uuid.UUID(bytes=sample_vehicle.id))
        update_data = {
            "veiculo": "Uno Vivace",
            "marca": "Fiat",
            "ano": 2021,
            "descricao": "Atualizado",
            "cor": "Prata",
            "vendido": True
        }

        response = client.put(
            f"/car-fleet-manager/api/v1/vehicles/{vehicle_id}",
            json=update_data
        )

        assert response.status_code == 200
        data = response.json()
        assert data["veiculo"] == "Uno Vivace"
        assert data["ano"] == 2021
        assert data["vendido"] is True

    def test_patch_vehicle(self, client, sample_vehicle):
        """Testa PATCH /api/v1/vehicles/{id}"""
        vehicle_id = str(uuid.UUID(bytes=sample_vehicle.id))
        response = client.patch(f"/car-fleet-manager/api/v1/vehicles/{vehicle_id}")

        assert response.status_code == 200
        data = response.json()
        assert data["vendido"] is True

    def test_delete_vehicle(self, client, sample_vehicle):
        """Testa DELETE /api/v1/vehicles/{id}"""
        vehicle_id = str(uuid.UUID(bytes=sample_vehicle.id))
        response = client.delete(f"/car-fleet-manager/api/v1/vehicles/{vehicle_id}")

        assert response.status_code == 204

        # Verifica que foi realmente deletado
        get_response = client.get(f"/car-fleet-manager/api/v1/vehicles/{vehicle_id}")
        assert get_response.status_code == 404

    def test_get_statistics(self, client, multiple_vehicles):
        """Testa GET /api/v1/vehicles/statistics"""
        response = client.get("/car-fleet-manager/api/v1/vehicles/statistics")

        assert response.status_code == 200
        stats = response.json()
        assert stats["totalVehicles"] == 3
        assert stats["unsoldVehicles"] == 2
        assert stats["soldVehicles"] == 1

    def test_get_vehicles_by_decade(self, client, multiple_vehicles):
        """Testa GET /api/v1/vehicles/statistics/by-decade"""
        response = client.get("/car-fleet-manager/api/v1/vehicles/statistics/by-decade")

        assert response.status_code == 200
        data = response.json()
        assert "vehiclesByDecade" in data
        assert isinstance(data["vehiclesByDecade"], dict)

    def test_get_vehicles_by_brand(self, client, multiple_vehicles):
        """Testa GET /api/v1/vehicles/statistics/by-brand"""
        response = client.get("/car-fleet-manager/api/v1/vehicles/statistics/by-brand")

        assert response.status_code == 200
        data = response.json()
        assert "vehiclesByBrand" in data
        assert isinstance(data["vehiclesByBrand"], dict)

    def test_get_vehicles_registered_last_week(self, client, sample_vehicle):
        """Testa GET /api/v1/vehicles/statistics/last-week"""
        response = client.get("/car-fleet-manager/api/v1/vehicles/statistics/last-week")

        assert response.status_code == 200
        data = response.json()
        assert "vehicles" in data
        assert "total" in data
        assert isinstance(data["vehicles"], list)

    def test_search_vehicles_all_filters(self, client, multiple_vehicles):
        """Testa GET /api/v1/vehicles/search com todos os filtros"""
        response = client.get(
            "/car-fleet-manager/api/v1/vehicles/search",
            params={
                "veiculo": "Uno",
                "marca": "Fiat",
                "ano": 2020,
                "cor": "Branco"
            }
        )

        assert response.status_code == 200
        vehicles = response.json()
        assert len(vehicles) == 1
        assert vehicles[0]["veiculo"] == "Uno"

    def test_search_vehicles_partial_filters(self, client, multiple_vehicles):
        """Testa GET /api/v1/vehicles/search com filtros parciais"""
        response = client.get(
            "/car-fleet-manager/api/v1/vehicles/search",
            params={"marca": "Fiat"}
        )

        assert response.status_code == 200
        vehicles = response.json()
        assert len(vehicles) == 3
