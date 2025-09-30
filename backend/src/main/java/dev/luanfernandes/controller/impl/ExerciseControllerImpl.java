package dev.luanfernandes.controller.impl;

import dev.luanfernandes.controller.ExerciseController;
import dev.luanfernandes.dto.request.BubbleSortRequest;
import dev.luanfernandes.dto.request.FatorialRequest;
import dev.luanfernandes.dto.request.MultiploRequest;
import dev.luanfernandes.dto.request.VotingDataRequest;
import dev.luanfernandes.dto.response.BubbleSortResponse;
import dev.luanfernandes.dto.response.FatorialResponse;
import dev.luanfernandes.dto.response.MultiploResponse;
import dev.luanfernandes.dto.response.VotingPercentageResponse;
import dev.luanfernandes.service.ExercisesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExerciseControllerImpl implements ExerciseController {

    private final ExercisesService exercisesService;

    @Override
    public ResponseEntity<VotingPercentageResponse> calculateVotingPercentages(VotingDataRequest request) {
        return ResponseEntity.ok(exercisesService.calculateVotingPercentages(request));
    }

    @Override
    public ResponseEntity<BubbleSortResponse> sortArray(BubbleSortRequest request) {
        return ResponseEntity.ok(exercisesService.sortArray(request));
    }

    @Override
    public ResponseEntity<FatorialResponse> calculateFactorial(FatorialRequest request) {
        return ResponseEntity.ok(exercisesService.calculateFactorial(request));
    }

    @Override
    public ResponseEntity<MultiploResponse> calculateMultiples(MultiploRequest request) {
        return ResponseEntity.ok(exercisesService.calculateMultiples(request));
    }
}
