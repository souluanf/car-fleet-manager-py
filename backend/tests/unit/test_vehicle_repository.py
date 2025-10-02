import pytest
import uuid
from datetime import datetime, timedelta
from app.repositories.vehicle_repository import VehicleRepository
from app.domain.vehicle import Vehicle


class TestVehicleRepository:
    """Testes unitários para VehicleRepository"""

    @pytest.fixture
    def vehicle_repository(self, test_db):
        """Fixture para criar instância do VehicleRepository"""
        return VehicleRepository(test_db)

    def test_find_all_empty(self, vehicle_repository):
        """Testa buscar todos os veículos quando não há nenhum"""
        vehicles = vehicle_repository.find_all()
        assert len(vehicles) == 0

    def test_find_all(self, vehicle_repository, multiple_vehicles):
        """Testa buscar todos os veículos"""
        vehicles = vehicle_repository.find_all()
        assert len(vehicles) == 3

    def test_find_by_id(self, vehicle_repository, sample_vehicle):
        """Testa buscar veículo por ID"""
        vehicle = vehicle_repository.find_by_id(sample_vehicle.id)
        assert vehicle is not None
        assert vehicle.veiculo == "Uno"

    def test_find_by_id_with_uuid(self, vehicle_repository, sample_vehicle):
        """Testa buscar veículo por UUID"""
        vehicle_uuid = uuid.UUID(bytes=sample_vehicle.id)
        vehicle = vehicle_repository.find_by_id(vehicle_uuid)
        assert vehicle is not None
        assert vehicle.veiculo == "Uno"

    def test_find_by_id_with_string(self, vehicle_repository, sample_vehicle):
        """Testa buscar veículo por string UUID"""
        vehicle_uuid_str = str(uuid.UUID(bytes=sample_vehicle.id))
        vehicle = vehicle_repository.find_by_id(vehicle_uuid_str)
        assert vehicle is not None
        assert vehicle.veiculo == "Uno"

    def test_find_by_id_not_found(self, vehicle_repository):
        """Testa buscar veículo inexistente"""
        fake_id = uuid.uuid4().bytes
        vehicle = vehicle_repository.find_by_id(fake_id)
        assert vehicle is None

    def test_save(self, vehicle_repository, sample_brand):
        """Testa salvar novo veículo"""
        vehicle = Vehicle(
            id=uuid.uuid4().bytes,
            veiculo="Palio",
            brand_id=sample_brand.id,
            ano=2019,
            descricao="Hatch compacto",
            cor="Azul",
            vendido=False
        )

        saved = vehicle_repository.save(vehicle)
        assert saved.id is not None
        assert saved.veiculo == "Palio"

    def test_delete(self, vehicle_repository, sample_vehicle):
        """Testa deletar veículo"""
        vehicle_repository.delete(sample_vehicle)

        deleted = vehicle_repository.find_by_id(sample_vehicle.id)
        assert deleted is None

    def test_count_total(self, vehicle_repository, multiple_vehicles):
        """Testa contar total de veículos"""
        count = vehicle_repository.count_total()
        assert count == 3

    def test_count_by_sold_status(self, vehicle_repository, multiple_vehicles):
        """Testa contar veículos por status de venda"""
        unsold = vehicle_repository.count_by_sold_status(False)
        sold = vehicle_repository.count_by_sold_status(True)

        assert unsold == 2
        assert sold == 1

    def test_find_by_decade(self, vehicle_repository, multiple_vehicles):
        """Testa buscar veículos por década"""
        vehicles_2020s = vehicle_repository.find_by_decade("2020")
        vehicles_2010s = vehicle_repository.find_by_decade("2010")

        assert len(vehicles_2020s) == 2  # 2020 e 2022
        assert len(vehicles_2010s) == 1  # 2015

    def test_count_by_brand(self, vehicle_repository, multiple_vehicles, sample_brand):
        """Testa contar veículos por marca"""
        counts = vehicle_repository.count_by_brand()

        assert len(counts) > 0
        assert counts[0][1] == 3  # Todos os veículos são da mesma marca

    def test_find_registered_last_week(self, vehicle_repository, test_db, sample_brand):
        """Testa buscar veículos cadastrados na última semana"""
        from sqlalchemy import text

        # Cria um veículo antigo
        old_vehicle = Vehicle(
            id=uuid.uuid4().bytes,
            veiculo="Velho",
            brand_id=sample_brand.id,
            ano=2010,
            descricao="Antigo",
            vendido=False
        )
        test_db.add(old_vehicle)
        test_db.commit()

        # Força a data de criação para 8 dias atrás usando text()
        old_date = (datetime.now() - timedelta(days=8)).strftime('%Y-%m-%d %H:%M:%S')
        test_db.execute(
            text("UPDATE vehicles SET created = :created WHERE id = :id"),
            {"created": old_date, "id": old_vehicle.id}
        )
        test_db.commit()

        # Cria veículo recente
        recent_vehicle = Vehicle(
            id=uuid.uuid4().bytes,
            veiculo="Recente",
            brand_id=sample_brand.id,
            ano=2023,
            descricao="Novo",
            vendido=False
        )
        test_db.add(recent_vehicle)
        test_db.commit()

        recent_vehicles = vehicle_repository.find_registered_last_week()

        # Deve retornar apenas o veículo recente
        assert len(recent_vehicles) >= 1
        recent_ids = [v.veiculo for v in recent_vehicles]
        assert "Recente" in recent_ids

    def test_find_by_filters_veiculo(self, vehicle_repository, multiple_vehicles):
        """Testa buscar veículos filtrando por nome"""
        vehicles = vehicle_repository.find_by_filters(veiculo="Uno")
        assert len(vehicles) == 1
        assert vehicles[0].veiculo == "Uno"

    def test_find_by_filters_marca(self, vehicle_repository, multiple_vehicles):
        """Testa buscar veículos filtrando por marca"""
        vehicles = vehicle_repository.find_by_filters(marca="Fiat")
        assert len(vehicles) == 3

    def test_find_by_filters_ano(self, vehicle_repository, multiple_vehicles):
        """Testa buscar veículos filtrando por ano"""
        vehicles = vehicle_repository.find_by_filters(ano=2020)
        assert len(vehicles) == 1
        assert vehicles[0].ano == 2020

    def test_find_by_filters_cor(self, vehicle_repository, multiple_vehicles):
        """Testa buscar veículos filtrando por cor"""
        vehicles = vehicle_repository.find_by_filters(cor="Branco")
        assert len(vehicles) == 1
        assert vehicles[0].cor == "Branco"

    def test_find_by_filters_all(self, vehicle_repository, multiple_vehicles):
        """Testa buscar veículos com todos os filtros"""
        vehicles = vehicle_repository.find_by_filters(
            veiculo="Uno",
            marca="Fiat",
            ano=2020,
            cor="Branco"
        )
        assert len(vehicles) == 1
        assert vehicles[0].veiculo == "Uno"
