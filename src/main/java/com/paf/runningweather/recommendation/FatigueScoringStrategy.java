package com.paf.runningweather.recommendation;

import com.paf.runningweather.fatigueFactor.FatigueFactor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class FatigueScoringStrategy implements ScoringStrategy {

    private static final Logger log = LoggerFactory.getLogger(FatigueScoringStrategy.class);


    /**
     * Calculates a fatigue-based score for a candidate training hour by weighting recent fatigue factors,
     * normalizing their levels, summing their contributions, and inverting/clamping the total to a 0-1 scale.
     *
     * @return score between 0 and 1
     */
    @Override
    public double score(TrainingHourCandidate candidate, RecommendationInput input) {
        List<FatigueFactor> fatigueFactors = input.getFatigueFactors();

        double fatigueSum = 0.0;

        //log.debug("_________Candidate:  {}", candidate.start());

        for (FatigueFactor fatigue : fatigueFactors) {
            double hoursAgo = Duration.between(fatigue.getDatetime(), candidate.start()).toHours();
            double daysAgo = hoursAgo / 24.0;

            if (daysAgo > 7) {
                //log.debug("Ignoring fatigue factor older than 7 days: {}", fatigue);
                continue;
            }

            double timeWeight = scoreTime(hoursAgo);
            double normalizedFatigue = (double) fatigue.getFatigueLevel() / 10;

            double contribution = normalizedFatigue * timeWeight;
            fatigueSum += contribution;

            /*log.debug(
                    "Fatigue factor [{}]: level={}, daysAgo={}, contribution={}",
                    fatigue.getCategory(), fatigue.getFatigueLevel(), daysAgo, contribution
            );*/
        }

        double score = 1.0 - clamp(fatigueSum);
        //log.info("Final fatigue score = {}", score);
        return score;
    }

    private double scoreTime(double hoursAgo) {
        double hoursUntilHalfRecovered = 12;
        double smoothing = 2.7; // lower score will cause more streched curve (longer effect)
        return 1.0 / (1.0 + Math.pow(hoursAgo / hoursUntilHalfRecovered, smoothing));
    }

    private double clamp (double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }

    @Override
    public double weight() {
        return 0.35;
    }

    @Override
    public List<String> getTags(TrainingHourCandidate candidate, RecommendationInput input) {
        List<String> tags = new ArrayList<>();

        List<FatigueFactor> fatigueFactors = input.getFatigueFactors();
        if (fatigueFactors == null || fatigueFactors.isEmpty()) {
            return tags;
        }

        boolean recentRun = false;
        boolean strongManualFatigue = false;

        for (FatigueFactor fatigue : fatigueFactors) {
            long daysAgo = ChronoUnit.DAYS.between(fatigue.getDatetime(), candidate.start());

            if (daysAgo > 7) continue;

            if("run".equalsIgnoreCase(fatigue.getCategory()) && daysAgo <= 1) {
                recentRun = true;
            }

            if (!"run".equalsIgnoreCase(fatigue.getCategory())
                                    && fatigue.getFatigueLevel() >= 6
                                    && daysAgo <= 1) {
                strongManualFatigue = true;
            }
        }

        if (!recentRun) {
            tags.add("Abstand zur letzten Einheit");
        }

        if (strongManualFatigue) {
            tags.add("hohe Ermüdung");
        }
        if (!strongManualFatigue &&
                ChronoUnit.DAYS.between(LocalDateTime.now(), candidate.start()) < 1) {
            tags.add("wenig Ermüdung");
        }

        return tags;
    }
}
