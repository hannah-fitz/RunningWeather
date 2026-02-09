package com.paf.runningweather.recommendation;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class RecommendationService {

    private final WeightedScoreAggregator aggregator;

    public RecommendationService(WeightedScoreAggregator aggregator) {
        this.aggregator = aggregator;
    }


    /**
     * Generates a ranked list of recommended training times by scoring each candidate,
     * collecting tags, filtering low scores, and limiting to the top 3 per day.
     *
     * @return sorted and filtered list of recommended training times
     */
    public List<RecommendedTrainingTime> recommend(RecommendationInput input) {

        List<RecommendedTrainingTime> recommendations = new ArrayList<>();

        for (TrainingHourCandidate candidate : input.getCandidates()) {
            double score = aggregator.aggregateScores(candidate, input);

            if (score > 0.2) {

                List<String> tags = new ArrayList<>();
                for (ScoringStrategy strategy : aggregator.getStrategies()) {
                    tags.addAll(strategy.getTags(candidate, input));
                }

                RecommendedTrainingTime recommendedTime = new RecommendedTrainingTime(candidate, score, tags);
                recommendations.add(recommendedTime);
            }
        }

        Map<LocalDate, List<RecommendedTrainingTime>> byDay = recommendations.stream()
                        .collect(Collectors.groupingBy(
                                r -> r.getTrainingHourCandidate().start().toLocalDate()
                        ));

        List<RecommendedTrainingTime> bestTimesOfDays = new ArrayList<>();
        for (List<RecommendedTrainingTime> daily : byDay.values()) {
            daily.sort(Comparator.comparingDouble(RecommendedTrainingTime::getScore).reversed());
            bestTimesOfDays.addAll(daily.stream().limit(3).toList());
        }

        bestTimesOfDays.sort(Comparator.comparingDouble(RecommendedTrainingTime::getScore).reversed());

        return bestTimesOfDays;
    }

}
