import pytest
from app.services.exercises_service import ExercisesService
from app.dto.request import VotingDataRequest, BubbleSortRequest, FatorialRequest, MultiploRequest


class TestExercisesService:
    """Testes unitários para ExercisesService"""

    @pytest.fixture
    def exercises_service(self):
        """Fixture para criar instância do ExercisesService"""
        return ExercisesService()

    def test_calculate_voting_percentages_valid(self, exercises_service):
        """Testa cálculo de porcentagens com dados válidos"""
        request = VotingDataRequest(
            totalEleitores=1000,
            votosValidos=800,
            votosBrancos=150,
            votosNulos=50
        )

        result = exercises_service.calculate_voting_percentages(request)

        assert result.percentualVotosValidos == pytest.approx(80.0)
        assert result.percentualVotosBrancos == pytest.approx(15.0)
        assert result.percentualVotosNulos == pytest.approx(5.0)

    def test_calculate_voting_percentages_zero_eleitores(self, exercises_service):
        """Testa validação com zero eleitores - Pydantic valida no nível de request"""
        from pydantic_core import ValidationError

        with pytest.raises(ValidationError):
            request = VotingDataRequest(
                totalEleitores=0,
                votosValidos=0,
                votosBrancos=0,
                votosNulos=0
            )

    def test_calculate_voting_percentages_invalid_sum(self, exercises_service):
        """Testa validação quando soma dos votos excede total"""
        from app.exceptions.business_exceptions import InvalidVotingDataException

        request = VotingDataRequest(
            totalEleitores=100,
            votosValidos=80,
            votosBrancos=30,
            votosNulos=20
        )

        with pytest.raises(InvalidVotingDataException) as exc_info:
            exercises_service.calculate_voting_percentages(request)

        assert "130" in str(exc_info.value.message)

    def test_bubble_sort(self, exercises_service):
        """Testa ordenação bubble sort"""
        request = BubbleSortRequest(vetor=[5, 3, 8, 1, 9, 2])

        result = exercises_service.bubble_sort(request)

        assert result.vetorOriginal == [5, 3, 8, 1, 9, 2]
        assert result.vetorOrdenado == [1, 2, 3, 5, 8, 9]

    def test_bubble_sort_empty(self, exercises_service):
        """Testa bubble sort com vetor vazio - Pydantic valida min_length"""
        from pydantic_core import ValidationError

        with pytest.raises(ValidationError):
            request = BubbleSortRequest(vetor=[])

    def test_bubble_sort_already_sorted(self, exercises_service):
        """Testa bubble sort com vetor já ordenado"""
        request = BubbleSortRequest(vetor=[1, 2, 3, 4, 5])

        result = exercises_service.bubble_sort(request)

        assert result.vetorOrdenado == [1, 2, 3, 4, 5]

    def test_calculate_factorial(self, exercises_service):
        """Testa cálculo de fatorial"""
        request = FatorialRequest(numero=5)

        result = exercises_service.calculate_factorial(request)

        assert result.numero == 5
        assert result.fatorial == 120

    def test_calculate_factorial_zero(self, exercises_service):
        """Testa fatorial de zero"""
        request = FatorialRequest(numero=0)

        result = exercises_service.calculate_factorial(request)

        assert result.fatorial == 1

    def test_calculate_factorial_one(self, exercises_service):
        """Testa fatorial de um"""
        request = FatorialRequest(numero=1)

        result = exercises_service.calculate_factorial(request)

        assert result.fatorial == 1

    def test_calculate_factorial_large(self, exercises_service):
        """Testa fatorial de número maior"""
        request = FatorialRequest(numero=10)

        result = exercises_service.calculate_factorial(request)

        assert result.fatorial == 3628800

    def test_calculate_multiples(self, exercises_service):
        """Testa cálculo de múltiplos de 3 e 5"""
        request = MultiploRequest(numero=10)

        result = exercises_service.calculate_multiples(request)

        assert result.numeroLimite == 10
        assert result.somaMultiplos == 23  # 3 + 5 + 6 + 9 = 23

    def test_calculate_multiples_large(self, exercises_service):
        """Testa cálculo com número maior"""
        request = MultiploRequest(numero=100)

        result = exercises_service.calculate_multiples(request)

        assert result.numeroLimite == 100
        # Múltiplos de 3: 3,6,9...99 = 1683
        # Múltiplos de 5: 5,10,15...95 = 950
        # Múltiplos de 15: 15,30,45...90 = 315
        # Total = 1683 + 950 - 315 = 2318
        assert result.somaMultiplos == 2318

    def test_calculate_multiples_one(self, exercises_service):
        """Testa com limite 1"""
        request = MultiploRequest(numero=1)

        result = exercises_service.calculate_multiples(request)

        assert result.somaMultiplos == 0
