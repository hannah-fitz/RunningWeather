package com.paf.runningweather.recommendation;

import java.util.List;

public interface ScoringStrategy {

    /**
     * @return score between 0.0 (bad) and 1.0 (optimal)
     */
    double score(TrainingHourCandidate candidate, RecommendationInput input);

    /**
     * @return influence of the scoring strategy in the final score calculation of a recommendation (between 0 and 1)
     */
    double weight();

    /**
     * Necassary for generating tags like "no rain", "perfect temperature", "preferred daytime".
     *
     * @return empty list, in case the strategy doesn't return any tags
     */
    default List<String> getTags(TrainingHourCandidate candidate, RecommendationInput input) {
        return List.of();
    }
}
