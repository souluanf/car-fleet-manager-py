import pytest
import uuid
from app.services.vehicle_service import VehicleService
from app.services.brand_service import BrandService
from app.repositories.vehicle_repository import VehicleRepository
from app.repositories.brand_repository import BrandRepository
from app.dto.request import VehicleRequest, VehicleUpdateRequest
from app.exceptions.business_exceptions import InvalidBrandException, VehicleNotFoundException


class TestVehicleService:
    """Testes unitários para VehicleService"""

    @pytest.fixture
    def vehicle_service(self, test_db):
        """Fixture para criar instância do VehicleService"""
        return VehicleService(test_db)

    def test_create_vehicle_success(self, vehicle_service, sample_brand):
        """Testa criação de veículo com sucesso"""
        request = VehicleRequest(
            veiculo="Uno",
            marca="Fiat",
            ano=2020,
            descricao="Carro popular",
            cor="Branco",
            vendido=False
        )

        vehicle = vehicle_service.create_vehicle(request)

        assert vehicle.veiculo == "Uno"
        assert vehicle.marca == "Fiat"
        assert vehicle.ano == 2020
        assert vehicle.vendido is False

    def test_create_vehicle_invalid_brand(self, vehicle_service):
        """Testa criação de veículo com marca inválida"""
        request = VehicleRequest(
            veiculo="Uno",
            marca="MarcaInexistente",
            ano=2020,
            descricao="Carro popular",
            cor="Branco",
            vendido=False
        )

        with pytest.raises(InvalidBrandException) as exc_info:
            vehicle_service.create_vehicle(request)

        assert "MarcaInexistente" in str(exc_info.value.message)

    def test_get_all_vehicles(self, vehicle_service, multiple_vehicles):
        """Testa buscar todos os veículos"""
        vehicles = vehicle_service.get_all_vehicles()
        assert len(vehicles) == 3

    def test_get_vehicle_by_id(self, vehicle_service, sample_vehicle):
        """Testa buscar veículo por ID"""
        vehicle_id = uuid.UUID(bytes=sample_vehicle.id)
        vehicle = vehicle_service.get_vehicle_by_id(vehicle_id)

        assert vehicle.id == vehicle_id
        assert vehicle.veiculo == "Uno"

    def test_get_vehicle_by_id_not_found(self, vehicle_service):
        """Testa buscar veículo inexistente"""
        fake_id = uuid.uuid4()

        with pytest.raises(VehicleNotFoundException):
            vehicle_service.get_vehicle_by_id(fake_id)

    def test_update_vehicle(self, vehicle_service, sample_vehicle, sample_brand):
        """Testa atualização de veículo"""
        vehicle_id = uuid.UUID(bytes=sample_vehicle.id)
        request = VehicleRequest(
            veiculo="Uno Vivace",
            marca="Fiat",
            ano=2021,
            descricao="Versão atualizada",
            cor="Prata",
            vendido=True
        )

        updated = vehicle_service.update_vehicle(vehicle_id, request)

        assert updated.veiculo == "Uno Vivace"
        assert updated.ano == 2021
        assert updated.cor == "Prata"
        assert updated.vendido is True

    def test_update_vehicle_not_found(self, vehicle_service, sample_brand):
        """Testa atualização de veículo inexistente"""
        fake_id = uuid.uuid4()
        request = VehicleRequest(
            veiculo="Uno",
            marca="Fiat",
            ano=2020,
            descricao="Teste",
            cor="Branco",
            vendido=False
        )

        with pytest.raises(VehicleNotFoundException):
            vehicle_service.update_vehicle(fake_id, request)

    def test_delete_vehicle(self, vehicle_service, sample_vehicle):
        """Testa exclusão de veículo"""
        vehicle_id = uuid.UUID(bytes=sample_vehicle.id)
        vehicle_service.delete_vehicle(vehicle_id)

        with pytest.raises(VehicleNotFoundException):
            vehicle_service.get_vehicle_by_id(vehicle_id)

    def test_delete_vehicle_not_found(self, vehicle_service):
        """Testa exclusão de veículo inexistente"""
        fake_id = uuid.uuid4()

        with pytest.raises(VehicleNotFoundException):
            vehicle_service.delete_vehicle(fake_id)

    def test_patch_vehicle(self, vehicle_service, sample_vehicle):
        """Testa patch (venda) de veículo"""
        vehicle_id = uuid.UUID(bytes=sample_vehicle.id)
        patched = vehicle_service.patch_vehicle(vehicle_id)

        assert patched.vendido is True

    def test_get_statistics(self, vehicle_service, multiple_vehicles):
        """Testa obtenção de estatísticas"""
        stats = vehicle_service.get_statistics()

        assert stats.totalVehicles == 3
        assert stats.unsoldVehicles == 2
        assert stats.soldVehicles == 1

    def test_get_vehicles_by_filters_all_params(self, vehicle_service, multiple_vehicles):
        """Testa busca com todos os filtros"""
        vehicles = vehicle_service.get_vehicles_by_filters(
            veiculo="Uno",
            marca="Fiat",
            ano=2020,
            cor="Branco"
        )

        assert len(vehicles) == 1
        assert vehicles[0].veiculo == "Uno"

    def test_get_vehicles_by_filters_partial(self, vehicle_service, multiple_vehicles):
        """Testa busca com filtros parciais"""
        vehicles = vehicle_service.get_vehicles_by_filters(marca="Fiat")

        assert len(vehicles) == 3

    def test_get_vehicles_by_filters_no_results(self, vehicle_service, multiple_vehicles):
        """Testa busca sem resultados"""
        vehicles = vehicle_service.get_vehicles_by_filters(marca="Toyota")

        assert len(vehicles) == 0

    def test_partial_update_vehicle(self, vehicle_service, sample_vehicle, sample_brand):
        """Testa atualização parcial de veículo"""
        vehicle_id = uuid.UUID(bytes=sample_vehicle.id)
        request = VehicleUpdateRequest(
            veiculo="Uno Mille",
            cor="Azul"
        )

        updated = vehicle_service.partial_update_vehicle(vehicle_id, request)

        assert updated.veiculo == "Uno Mille"
        assert updated.cor == "Azul"
        assert updated.ano == 2020  # Não alterado

    def test_partial_update_vehicle_with_brand(self, vehicle_service, sample_vehicle, sample_brand):
        """Testa atualização parcial com mudança de marca"""
        vehicle_id = uuid.UUID(bytes=sample_vehicle.id)
        request = VehicleUpdateRequest(
            marca="Fiat",
            vendido=True
        )

        updated = vehicle_service.partial_update_vehicle(vehicle_id, request)

        assert updated.marca == "Fiat"
        assert updated.vendido is True

    def test_partial_update_vehicle_all_fields(self, vehicle_service, sample_vehicle, sample_brand):
        """Testa atualização parcial com todos os campos"""
        vehicle_id = uuid.UUID(bytes=sample_vehicle.id)
        request = VehicleUpdateRequest(
            veiculo="Uno Vivace",
            marca="Fiat",
            ano=2021,
            descricao="Novo modelo",
            cor="Prata",
            vendido=True
        )

        updated = vehicle_service.partial_update_vehicle(vehicle_id, request)

        assert updated.veiculo == "Uno Vivace"
        assert updated.marca == "Fiat"
        assert updated.ano == 2021
        assert updated.descricao == "Novo modelo"
        assert updated.cor == "Prata"
        assert updated.vendido is True
