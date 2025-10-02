import pytest


class TestExercisesRouter:
    """Testes de integração para ExercisesRouter"""

    def test_calculate_voting_percentages(self, client):
        """Testa POST /api/v1/exercises/voting"""
        voting_data = {
            "totalEleitores": 1000,
            "votosValidos": 800,
            "votosBrancos": 150,
            "votosNulos": 50
        }

        response = client.post("/car-fleet-manager/api/v1/exercises/voting", json=voting_data)

        assert response.status_code == 200
        data = response.json()
        assert data["percentualVotosValidos"] == 80.0
        assert data["percentualVotosBrancos"] == 15.0
        assert data["percentualVotosNulos"] == 5.0

    def test_calculate_voting_invalid_data(self, client):
        """Testa POST /api/v1/exercises/voting com dados inválidos"""
        voting_data = {
            "totalEleitores": 100,
            "votosValidos": 80,
            "votosBrancos": 30,
            "votosNulos": 20
        }

        response = client.post("/car-fleet-manager/api/v1/exercises/voting", json=voting_data)

        assert response.status_code == 400

    def test_bubble_sort(self, client):
        """Testa POST /api/v1/exercises/bubble-sort"""
        sort_data = {
            "vetor": [5, 3, 8, 1, 9, 2]
        }

        response = client.post("/car-fleet-manager/api/v1/exercises/bubble-sort", json=sort_data)

        assert response.status_code == 200
        data = response.json()
        assert data["vetorOriginal"] == [5, 3, 8, 1, 9, 2]
        assert data["vetorOrdenado"] == [1, 2, 3, 5, 8, 9]

    def test_calculate_factorial(self, client):
        """Testa POST /api/v1/exercises/fatorial"""
        factorial_data = {
            "numero": 5
        }

        response = client.post("/car-fleet-manager/api/v1/exercises/fatorial", json=factorial_data)

        assert response.status_code == 200
        data = response.json()
        assert data["numero"] == 5
        assert data["fatorial"] == 120

    def test_calculate_factorial_zero(self, client):
        """Testa fatorial de zero"""
        factorial_data = {
            "numero": 0
        }

        response = client.post("/car-fleet-manager/api/v1/exercises/fatorial", json=factorial_data)

        assert response.status_code == 200
        data = response.json()
        assert data["fatorial"] == 1

    def test_calculate_multiples(self, client):
        """Testa POST /api/v1/exercises/multiplos"""
        multiples_data = {
            "numero": 10
        }

        response = client.post("/car-fleet-manager/api/v1/exercises/multiplos", json=multiples_data)

        assert response.status_code == 200
        data = response.json()
        assert data["numeroLimite"] == 10
        assert data["somaMultiplos"] == 23

    def test_calculate_multiples_large(self, client):
        """Testa cálculo de múltiplos com número maior"""
        multiples_data = {
            "numero": 100
        }

        response = client.post("/car-fleet-manager/api/v1/exercises/multiplos", json=multiples_data)

        assert response.status_code == 200
        data = response.json()
        assert data["numeroLimite"] == 100
        assert data["somaMultiplos"] == 2318
