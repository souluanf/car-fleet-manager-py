package dev.luanfernandes.service;

import dev.luanfernandes.dto.request.BubbleSortRequest;
import dev.luanfernandes.dto.request.FatorialRequest;
import dev.luanfernandes.dto.request.MultiploRequest;
import dev.luanfernandes.dto.request.VotingDataRequest;
import dev.luanfernandes.dto.response.BubbleSortResponse;
import dev.luanfernandes.dto.response.FatorialResponse;
import dev.luanfernandes.dto.response.MultiploResponse;
import dev.luanfernandes.dto.response.VotingPercentageResponse;

public interface ExercisesService {

    VotingPercentageResponse calculateVotingPercentages(VotingDataRequest request);

    BubbleSortResponse sortArray(BubbleSortRequest request);

    FatorialResponse calculateFactorial(FatorialRequest request);

    MultiploResponse calculateMultiples(MultiploRequest request);
}
