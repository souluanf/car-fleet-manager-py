package dev.luanfernandes.controller.impl;

import static dev.luanfernandes.constants.PathConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luanfernandes.config.web.ExceptionHandlerAdvice;
import dev.luanfernandes.dto.request.BubbleSortRequest;
import dev.luanfernandes.dto.request.FatorialRequest;
import dev.luanfernandes.dto.request.MultiploRequest;
import dev.luanfernandes.dto.request.VotingDataRequest;
import dev.luanfernandes.dto.response.BubbleSortResponse;
import dev.luanfernandes.dto.response.FatorialResponse;
import dev.luanfernandes.dto.response.MultiploResponse;
import dev.luanfernandes.dto.response.VotingPercentageResponse;
import dev.luanfernandes.service.ExercisesService;
import java.math.BigInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for ExercisesControllerImpl")
class ExerciseControllerImplTest {

    private MockMvc mockMvc;

    @Mock
    private ExercisesService exercisesService;

    @InjectMocks
    private ExerciseControllerImpl exercisesController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(exercisesController)
                .setControllerAdvice(new ExceptionHandlerAdvice())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    @DisplayName("Should return 200 when calculating voting percentages")
    void shouldReturnOk_WhenCalculatingVotingPercentages() throws Exception {
        var request = new VotingDataRequest(1000, 800, 150, 50);
        var response = new VotingPercentageResponse(
                java.math.BigDecimal.valueOf(80.0),
                java.math.BigDecimal.valueOf(15.0),
                java.math.BigDecimal.valueOf(5.0));

        when(exercisesService.calculateVotingPercentages(any(VotingDataRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post(EXERCISES_VOTING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.percentualVotosValidos").value(80.0))
                .andExpect(jsonPath("$.percentualVotosBrancos").value(15.0))
                .andExpect(jsonPath("$.percentualVotosNulos").value(5.0));
    }

    @Test
    @DisplayName("Should return 200 when sorting array with bubble sort")
    void shouldReturnOk_WhenSortingArray() throws Exception {
        var request = new BubbleSortRequest(java.util.Arrays.asList(5, 3, 8, 1, 2));
        var response =
                new BubbleSortResponse(java.util.Arrays.asList(5, 3, 8, 1, 2), java.util.Arrays.asList(1, 2, 3, 5, 8));

        when(exercisesService.sortArray(any(BubbleSortRequest.class))).thenReturn(response);

        mockMvc.perform(post(EXERCISES_BUBBLE_SORT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vetorOrdenado[0]").value(1))
                .andExpect(jsonPath("$.vetorOrdenado[4]").value(8));
    }

    @Test
    @DisplayName("Should return 200 when calculating factorial")
    void shouldReturnOk_WhenCalculatingFactorial() throws Exception {
        var request = new FatorialRequest(5);
        var response = new FatorialResponse(5, BigInteger.valueOf(120));

        when(exercisesService.calculateFactorial(any(FatorialRequest.class))).thenReturn(response);

        mockMvc.perform(post(EXERCISES_FATORIAL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fatorial").value(120));
    }

    @Test
    @DisplayName("Should return 200 when calculating multiples sum")
    void shouldReturnOk_WhenCalculatingMultiplesSum() throws Exception {
        var request = new MultiploRequest(10);
        var response = new MultiploResponse(10, 23);

        when(exercisesService.calculateMultiples(any(MultiploRequest.class))).thenReturn(response);

        mockMvc.perform(post(EXERCISES_MULTIPLOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.somaMultiplos").value(23));
    }
}
