from fastapi import APIRouter
from app.services.exercises_service import ExercisesService
from app.dto.request import BubbleSortRequest, FatorialRequest, MultiploRequest, VotingDataRequest
from app.dto.response import BubbleSortResponse, FatorialResponse, MultiploResponse, VotingPercentageResponse

router = APIRouter(prefix="/api/v1/exercises", tags=["Exercises"])
service = ExercisesService()


@router.post(
    "/voting",
    response_model=VotingPercentageResponse,
    summary="Calcular percentuais de votos",
    description="Calcula o percentual de votos válidos, brancos e nulos em relação ao total de eleitores",
    operation_id="calculateVotingPercentages",
    responses={
        200: {"description": "Percentuais calculados com sucesso"},
        400: {"description": "Dados de entrada inválidos"}
    }
)
def calculate_voting_percentages(request: VotingDataRequest):
    return service.calculate_voting_percentages(request)


@router.post(
    "/bubble-sort",
    response_model=BubbleSortResponse,
    summary="Ordenar vetor usando Bubble Sort",
    description="Ordena um vetor de inteiros utilizando o algoritmo Bubble Sort",
    operation_id="sortArray",
    responses={
        200: {"description": "Vetor ordenado com sucesso"},
        400: {"description": "Vetor inválido"}
    }
)
def sort_array(request: BubbleSortRequest):
    return service.sort_array(request)


@router.post(
    "/fatorial",
    response_model=FatorialResponse,
    summary="Calcular fatorial de um número",
    description="Calcula o fatorial de um número natural usando recursão",
    operation_id="calculateFactorial",
    responses={
        200: {"description": "Fatorial calculado com sucesso"},
        400: {"description": "Número inválido"}
    }
)
def calculate_factorial(request: FatorialRequest):
    return service.calculate_factorial(request)


@router.post(
    "/multiplos",
    response_model=MultiploResponse,
    summary="Calcular soma de múltiplos de 3 ou 5",
    description="Calcula a soma de todos os números múltiplos de 3 ou 5 abaixo de um número X",
    operation_id="calculateMultiples",
    responses={
        200: {"description": "Soma calculada com sucesso"},
        400: {"description": "Número inválido"}
    }
)
def calculate_multiples(request: MultiploRequest):
    return service.calculate_multiples(request)
