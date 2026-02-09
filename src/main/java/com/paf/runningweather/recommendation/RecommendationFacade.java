package com.paf.runningweather.recommendation;

import com.paf.runningweather.runner.Runner;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Enables the controller to just call recommendFor(runner), hiding all logic behind.
 */
@Service
public class RecommendationFacade {

    private final RecommendationInputBuilder inputBuilder;
    private final RecommendationService recommendationService;

    public RecommendationFacade(RecommendationInputBuilder inputBuilder, RecommendationService recommendationService) {
        this.inputBuilder = inputBuilder;
        this.recommendationService = recommendationService;
    }

    public List<RecommendedTrainingTime> recommendFor(Runner runner) {
        RecommendationInput input = inputBuilder.build(runner);
        List<RecommendedTrainingTime> recommendations = recommendationService.recommend(input);

        if (recommendations.isEmpty()) {
            throw new RuntimeException("Ooops... no recommendations found.");
        }
        return recommendations;
    }
}
