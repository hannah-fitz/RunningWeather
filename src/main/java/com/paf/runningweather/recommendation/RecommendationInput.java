package com.paf.runningweather.recommendation;

import com.paf.runningweather.fatigueFactor.FatigueFactor;
import com.paf.runningweather.goal.Goal;
import com.paf.runningweather.runner.Runner;
import com.paf.runningweather.trainingTimes.TrainingTime;

import java.util.List;

/**
 * Collects all input data that is needed for recommendation.
 */
public class RecommendationInput {

    private Runner runner;
    private List<TrainingTime> preferredTrainingTimes;
    private List<FatigueFactor> fatigueFactors;
    private Goal weeklyGoal;
    private List<TrainingHourCandidate> candidates;

    public RecommendationInput(Runner runner, List<TrainingTime> preferredTrainingTimes, List<FatigueFactor> fatigueFactors, Goal weeklyGoal, List<TrainingHourCandidate> candidates) {
        this.runner = runner;
        this.preferredTrainingTimes = preferredTrainingTimes;
        this.fatigueFactors = fatigueFactors;
        this.weeklyGoal = weeklyGoal;
        this.candidates = candidates;
    }

    public Runner getRunner() {
        return runner;
    }

    public void setRunner(Runner runner) {
        this.runner = runner;
    }

    public List<TrainingHourCandidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<TrainingHourCandidate> candidates) {
        this.candidates = candidates;
    }

    public List<TrainingTime> getPreferredTrainingTimes() {
        return preferredTrainingTimes;
    }

    public void setPreferredTrainingTimes(List<TrainingTime> preferredTrainingTimes) {
        this.preferredTrainingTimes = preferredTrainingTimes;
    }

    public List<FatigueFactor> getFatigueFactors() {
        return fatigueFactors;
    }

    public void setFatigueFactors(List<FatigueFactor> fatigueFactors) {
        this.fatigueFactors = fatigueFactors;
    }

    public Goal getWeeklyGoal() {
        return weeklyGoal;
    }

    public void setWeeklyGoal(Goal weeklyGoal) {
        this.weeklyGoal = weeklyGoal;
    }
}
