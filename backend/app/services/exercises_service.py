from app.dto.request import BubbleSortRequest, FatorialRequest, MultiploRequest, VotingDataRequest
from app.dto.response import BubbleSortResponse, FatorialResponse, MultiploResponse, VotingPercentageResponse
from app.exceptions.business_exceptions import InvalidVotingDataException
import math


class ExercisesService:
    def calculate_voting_percentages(self, request: VotingDataRequest) -> VotingPercentageResponse:
        self._validate_voting_data(request)

        total = request.totalEleitores

        percentualVotosValidos = round((request.votosValidos / total) * 100, 2)
        percentualVotosBrancos = round((request.votosBrancos / total) * 100, 2)
        percentualVotosNulos = round((request.votosNulos / total) * 100, 2)

        return VotingPercentageResponse(
            percentualVotosValidos=percentualVotosValidos,
            percentualVotosBrancos=percentualVotosBrancos,
            percentualVotosNulos=percentualVotosNulos
        )

    def sort_array(self, request: BubbleSortRequest) -> BubbleSortResponse:
        vetorOriginal = request.vetor.copy()
        vetorOrdenado = request.vetor.copy()
        n = len(vetorOrdenado)

        for i in range(n - 1):
            for j in range(n - i - 1):
                if vetorOrdenado[j] > vetorOrdenado[j + 1]:
                    vetorOrdenado[j], vetorOrdenado[j + 1] = vetorOrdenado[j + 1], vetorOrdenado[j]

        return BubbleSortResponse(vetorOriginal=vetorOriginal, vetorOrdenado=vetorOrdenado)

    def bubble_sort(self, request: BubbleSortRequest) -> BubbleSortResponse:
        """Alias para sort_array"""
        return self.sort_array(request)

    def calculate_factorial(self, request: FatorialRequest) -> FatorialResponse:
        fatorial = math.factorial(request.numero)
        return FatorialResponse(numero=request.numero, fatorial=fatorial)

    def calculate_multiples(self, request: MultiploRequest) -> MultiploResponse:
        soma = 0
        limite = request.numero
        for i in range(1, limite):
            if i % 3 == 0 or i % 5 == 0:
                soma += i

        return MultiploResponse(
            numeroLimite=limite,
            somaMultiplos=soma
        )

    def _validate_voting_data(self, request: VotingDataRequest) -> None:
        total_votes = request.votosBrancos + request.votosNulos + request.votosValidos
        if total_votes != request.totalEleitores:
            raise InvalidVotingDataException(
                f"A soma dos votos ({total_votes}) deve ser igual ao total de eleitores ({request.totalEleitores})"
            )
