package com.paf.runningweather.recommendation;

import com.paf.runningweather.trainingTimes.TrainingTime;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TemporalProximityScoringStrategy implements ScoringStrategy {

    /**
     * Calculates a score based on how close the candidate is to the current time.
     * Candidates tomorrow will be scored higher than candidates in five days.
     *
     * @return score between 0 and 1
     */
    @Override
    public double score(TrainingHourCandidate candidate, RecommendationInput input) {

        long hoursUntil = Duration.between(LocalDateTime.now(), candidate.start()).toHours();

        if (hoursUntil < 0) { return 0.0; }
        if (hoursUntil <= 24) { return 1.0; }
        if (hoursUntil <= 48) { return 0.8; }
        if (hoursUntil <= 72) { return 0.6; }

        return 0.3;
    }

    @Override
    public double weight() {
        return 0.15;
    }

    @Override
    public List<String> getTags(TrainingHourCandidate candidate, RecommendationInput input) {
        List<String> tags = new ArrayList<>();

        LocalDateTime start = candidate.start();
        LocalDateTime now = LocalDateTime.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        if (!start.isBefore(now) && start.toLocalDate().equals(now.toLocalDate())) tags.add("noch heute");
        if (start.toLocalDate().equals(tomorrow)) tags.add("morgen");

        return tags;
    }


}
