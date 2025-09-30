package dev.luanfernandes.service.impl;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

import dev.luanfernandes.domain.exception.InvalidVotingDataException;
import dev.luanfernandes.dto.request.BubbleSortRequest;
import dev.luanfernandes.dto.request.FatorialRequest;
import dev.luanfernandes.dto.request.MultiploRequest;
import dev.luanfernandes.dto.request.VotingDataRequest;
import dev.luanfernandes.dto.response.BubbleSortResponse;
import dev.luanfernandes.dto.response.FatorialResponse;
import dev.luanfernandes.dto.response.MultiploResponse;
import dev.luanfernandes.dto.response.VotingPercentageResponse;
import dev.luanfernandes.service.ExercisesService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExercisesServiceImpl implements ExercisesService {

    @Override
    public VotingPercentageResponse calculateVotingPercentages(VotingDataRequest request) {
        validateVotingData(request);
        BigDecimal percentualVotosValidos = calculateValidVotesPercentage(request);
        BigDecimal percentualVotosBrancos = calculateBlankVotesPercentage(request);
        BigDecimal percentualVotosNulos = calculateNullVotesPercentage(request);
        return new VotingPercentageResponse(percentualVotosValidos, percentualVotosBrancos, percentualVotosNulos);
    }

    @Override
    public BubbleSortResponse sortArray(BubbleSortRequest request) {
        List<Integer> vetorOriginal = new ArrayList<>(request.vetor());
        List<Integer> vetorOrdenado = new ArrayList<>(request.vetor());
        bubbleSort(vetorOrdenado);
        return new BubbleSortResponse(vetorOriginal, vetorOrdenado);
    }

    @Override
    public FatorialResponse calculateFactorial(FatorialRequest request) {
        BigInteger fatorial = calculateFatorial(request.numero());
        return new FatorialResponse(request.numero(), fatorial);
    }

    @Override
    public MultiploResponse calculateMultiples(MultiploRequest request) {
        int soma = 0;
        int limite = request.numero();
        for (int i = 1; i < limite; i++) {
            if (i % 3 == 0 || i % 5 == 0) {
                soma += i;
            }
        }
        return new MultiploResponse(limite, soma);
    }

    private BigDecimal calculateValidVotesPercentage(VotingDataRequest request) {
        return calculatePercentage(request.votosValidos(), request.totalEleitores());
    }

    private BigDecimal calculateBlankVotesPercentage(VotingDataRequest request) {
        return calculatePercentage(request.votosBrancos(), request.totalEleitores());
    }

    private BigDecimal calculateNullVotesPercentage(VotingDataRequest request) {
        return calculatePercentage(request.votosNulos(), request.totalEleitores());
    }

    private BigDecimal calculatePercentage(int votos, int totalEleitores) {
        BigDecimal votosDecimal = valueOf(votos);
        BigDecimal totalDecimal = valueOf(totalEleitores);
        BigDecimal cem = valueOf(100);
        return votosDecimal.divide(totalDecimal, 4, HALF_UP).multiply(cem).setScale(2, HALF_UP);
    }

    private void validateVotingData(VotingDataRequest request) {
        int totalVotos = request.votosValidos() + request.votosBrancos() + request.votosNulos();
        if (totalVotos != request.totalEleitores()) {
            throw new InvalidVotingDataException(String.format(
                    "A soma dos votos (%d) deve ser igual ao total de eleitores (%d)",
                    totalVotos, request.totalEleitores()));
        }
    }

    private void bubbleSort(List<Integer> vetor) {
        int n = vetor.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (vetor.get(j) > vetor.get(j + 1)) {
                    swap(vetor, j, j + 1);
                }
            }
        }
    }

    private void swap(List<Integer> vetor, int i, int j) {
        Integer temp = vetor.get(i);
        vetor.set(i, vetor.get(j));
        vetor.set(j, temp);
    }

    private BigInteger calculateFatorial(int n) {
        if (n == 0 || n == 1) {
            return BigInteger.ONE;
        }
        return BigInteger.valueOf(n).multiply(calculateFatorial(n - 1));
    }
}
