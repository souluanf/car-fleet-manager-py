package dev.luanfernandes.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.luanfernandes.domain.exception.InvalidVotingDataException;
import dev.luanfernandes.dto.request.BubbleSortRequest;
import dev.luanfernandes.dto.request.FatorialRequest;
import dev.luanfernandes.dto.request.MultiploRequest;
import dev.luanfernandes.dto.request.VotingDataRequest;
import dev.luanfernandes.dto.response.BubbleSortResponse;
import dev.luanfernandes.dto.response.FatorialResponse;
import dev.luanfernandes.dto.response.MultiploResponse;
import dev.luanfernandes.dto.response.VotingPercentageResponse;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for ExercisesServiceImpl")
class ExercisesServiceImplTest {

    private final ExercisesServiceImpl service = new ExercisesServiceImpl();

    @Test
    @DisplayName("Should calculate voting percentages correctly")
    void shouldCalculateVotingPercentagesCorrectly() {
        VotingDataRequest request = new VotingDataRequest(1000, 800, 150, 50);

        VotingPercentageResponse response = service.calculateVotingPercentages(request);

        assertThat(response.percentualVotosValidos()).isEqualByComparingTo(BigDecimal.valueOf(80.00));
        assertThat(response.percentualVotosBrancos()).isEqualByComparingTo(BigDecimal.valueOf(15.00));
        assertThat(response.percentualVotosNulos()).isEqualByComparingTo(BigDecimal.valueOf(5.00));
    }

    @Test
    @DisplayName("Should throw exception when voting data sum does not match total")
    void shouldThrowExceptionWhenVotingDataInvalid() {
        VotingDataRequest request = new VotingDataRequest(1000, 800, 150, 100);

        assertThatThrownBy(() -> service.calculateVotingPercentages(request))
                .isInstanceOf(InvalidVotingDataException.class)
                .hasMessageContaining("A soma dos votos (1050) deve ser igual ao total de eleitores (1000)");
    }

    @Test
    @DisplayName("Should sort array using bubble sort")
    void shouldSortArrayUsingBubbleSort() {
        BubbleSortRequest request = new BubbleSortRequest(Arrays.asList(5, 3, 8, 1, 2));

        BubbleSortResponse response = service.sortArray(request);

        assertThat(response.vetorOriginal()).containsExactly(5, 3, 8, 1, 2);
        assertThat(response.vetorOrdenado()).containsExactly(1, 2, 3, 5, 8);
    }

    @Test
    @DisplayName("Should sort already sorted array")
    void shouldSortAlreadySortedArray() {
        BubbleSortRequest request = new BubbleSortRequest(Arrays.asList(1, 2, 3, 4, 5));

        BubbleSortResponse response = service.sortArray(request);

        assertThat(response.vetorOrdenado()).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    @DisplayName("Should calculate factorial of 5")
    void shouldCalculateFactorialOfFive() {
        FatorialRequest request = new FatorialRequest(5);

        FatorialResponse response = service.calculateFactorial(request);

        assertThat(response.fatorial()).isEqualTo(BigInteger.valueOf(120));
    }

    @Test
    @DisplayName("Should calculate factorial of 0")
    void shouldCalculateFactorialOfZero() {
        FatorialRequest request = new FatorialRequest(0);

        FatorialResponse response = service.calculateFactorial(request);

        assertThat(response.fatorial()).isEqualTo(BigInteger.ONE);
    }

    @Test
    @DisplayName("Should calculate factorial of 1")
    void shouldCalculateFactorialOfOne() {
        FatorialRequest request = new FatorialRequest(1);

        FatorialResponse response = service.calculateFactorial(request);

        assertThat(response.fatorial()).isEqualTo(BigInteger.ONE);
    }

    @Test
    @DisplayName("Should calculate sum of multiples of 3 or 5 below 10")
    void shouldCalculateSumOfMultiplesBelow10() {
        MultiploRequest request = new MultiploRequest(10);

        MultiploResponse response = service.calculateMultiples(request);

        assertThat(response.somaMultiplos()).isEqualTo(23);
    }

    @Test
    @DisplayName("Should return 0 for multiples below 3")
    void shouldReturnZeroForMultiplesBelow3() {
        MultiploRequest request = new MultiploRequest(3);

        MultiploResponse response = service.calculateMultiples(request);

        assertThat(response.somaMultiplos()).isZero();
    }
}
