package com.paf.runningweather.recommendation;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeightedScoreAggregator {

    private final List<ScoringStrategy> strategies;

    public WeightedScoreAggregator(List<ScoringStrategy> strategies) {
        this.strategies = strategies;
    }

    /**
     *
     * @param candidate TrainingHourCandidate to be evaluated
     * @param input everything to be taken into consideration for the recommendation (wrapped inside RecommendationInput)
     * @return total score for this TrainingHourCandidate between 0 and 1
     */
    public double aggregateScores(TrainingHourCandidate candidate, RecommendationInput input) {
        double total = 0.0;

        // Dependency Injection: all classes implementing ScoringStrategy are being collected
        for (ScoringStrategy strategy : strategies) {
            total += strategy.score(candidate, input) * strategy.weight();
        }

        return total;
    }

    public List<ScoringStrategy> getStrategies() {
        return strategies;
    }
}

