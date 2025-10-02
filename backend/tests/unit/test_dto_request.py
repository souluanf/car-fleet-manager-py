import pytest
from pydantic import ValidationError
from app.dto.request import VehicleRequest, VotingDataRequest


class TestVehicleRequest:
    """Testes para VehicleRequest"""

    def test_vehicle_request_empty_veiculo(self):
        """Testa validação de campo veiculo vazio (min_length)"""
        with pytest.raises(ValidationError) as exc_info:
            VehicleRequest(
                veiculo="",
                marca="Fiat",
                ano=2020,
                descricao="Teste",
                vendido=False
            )

        # Pydantic valida min_length antes do validador customizado
        assert "String should have at least 1 character" in str(exc_info.value)

    def test_vehicle_request_whitespace_marca(self):
        """Testa validação de campo marca com apenas espaços"""
        with pytest.raises(ValidationError) as exc_info:
            VehicleRequest(
                veiculo="Uno",
                marca="   ",
                ano=2020,
                descricao="Teste",
                vendido=False
            )

        # Validador customizado detecta string vazia após strip()
        assert "Campo não pode estar vazio" in str(exc_info.value)

    def test_vehicle_request_empty_descricao(self):
        """Testa validação de campo descricao vazio (min_length)"""
        with pytest.raises(ValidationError) as exc_info:
            VehicleRequest(
                veiculo="Uno",
                marca="Fiat",
                ano=2020,
                descricao="",
                vendido=False
            )

        # Pydantic valida min_length antes do validador customizado
        assert "String should have at least 1 character" in str(exc_info.value)

    def test_vehicle_request_whitespace_descricao(self):
        """Testa validação de campo descricao com apenas espaços"""
        with pytest.raises(ValidationError) as exc_info:
            VehicleRequest(
                veiculo="Uno",
                marca="Fiat",
                ano=2020,
                descricao="  ",
                vendido=False
            )

        # Validador customizado detecta string vazia após strip()
        assert "Campo não pode estar vazio" in str(exc_info.value)


class TestVotingDataRequest:
    """Testes para VotingDataRequest"""

    def test_voting_data_votos_validos_exceeds_total(self):
        """Testa validação quando votos válidos excedem total de eleitores"""
        with pytest.raises(ValidationError) as exc_info:
            VotingDataRequest(
                totalEleitores=100,
                votosValidos=150,
                votosBrancos=0,
                votosNulos=0
            )

        assert "Número de votos não pode exceder o total de eleitores" in str(exc_info.value)

    def test_voting_data_votos_brancos_exceeds_total(self):
        """Testa validação quando votos brancos excedem total de eleitores"""
        with pytest.raises(ValidationError) as exc_info:
            VotingDataRequest(
                totalEleitores=100,
                votosValidos=50,
                votosBrancos=150,
                votosNulos=0
            )

        assert "Número de votos não pode exceder o total de eleitores" in str(exc_info.value)

    def test_voting_data_votos_nulos_exceeds_total(self):
        """Testa validação quando votos nulos excedem total de eleitores"""
        with pytest.raises(ValidationError) as exc_info:
            VotingDataRequest(
                totalEleitores=100,
                votosValidos=50,
                votosBrancos=0,
                votosNulos=150
            )

        assert "Número de votos não pode exceder o total de eleitores" in str(exc_info.value)
