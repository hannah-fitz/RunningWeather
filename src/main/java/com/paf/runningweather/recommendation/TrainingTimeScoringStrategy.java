package com.paf.runningweather.recommendation;

import com.paf.runningweather.trainingTimes.TrainingTime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TrainingTimeScoringStrategy implements ScoringStrategy {

    @Override
    public double score(TrainingHourCandidate candidate, RecommendationInput input) {
        return isPreferred(candidate, input.getPreferredTrainingTimes()) ? 1.0 : 0.5;
    }

    @Override
    public double weight() {
        return 0.15;
    }

    @Override
    public List<String> getTags(TrainingHourCandidate candidate, RecommendationInput input) {
        List<String> tags = new ArrayList<>();
        if (isPreferred(candidate, input.getPreferredTrainingTimes())) tags.add("bevorzugte Trainingszeit");
        return tags;
    }

    /**
     * Checks if the TrainingHourCandidate is at a time saved as a preferred TrainingTime by the user.
     */
    private boolean isPreferred(TrainingHourCandidate candidate, List<TrainingTime> preferredTrainingTimes) {
        if (preferredTrainingTimes == null || preferredTrainingTimes.isEmpty()) return false;

        for (TrainingTime preference : preferredTrainingTimes) {
            if (candidate.start().getDayOfWeek() == preference.getDayOfWeek()
                    && !candidate.start().toLocalTime().isBefore(preference.getStartTime())
                    && !candidate.end().toLocalTime().isAfter(preference.getEndTime())) {
                return true;
            }
        }
        return false;
    }
}
