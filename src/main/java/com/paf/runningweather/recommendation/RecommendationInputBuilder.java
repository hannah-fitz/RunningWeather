package com.paf.runningweather.recommendation;

import org.springframework.stereotype.Service;
import com.paf.runningweather.fatigueFactor.FatigueFactor;
import com.paf.runningweather.fatigueFactor.FatigueFactorService;
import com.paf.runningweather.goal.Goal;
import com.paf.runningweather.runner.Runner;
import com.paf.runningweather.runner.RunnerService;
import com.paf.runningweather.trainingTimes.TrainingTime;
import com.paf.runningweather.weather.OpenMeteoAdapter;
import com.paf.runningweather.weather.WeatherForecast;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationInputBuilder {
    private final OpenMeteoAdapter weatherAdapter;
    private final FatigueFactorService fatigueFactorService;

    public RecommendationInputBuilder(OpenMeteoAdapter weatherAdapter, FatigueFactorService fatigueFactorService) {
        this.weatherAdapter = weatherAdapter;
        this.fatigueFactorService = fatigueFactorService;
    }

    /**
     * Performs all necassary steps to collect data for the recommendation.
     * @return new RecommendationInput object
     */
    public RecommendationInput build(Runner runner) {

        // 1. get FatigueFactors
        LocalDateTime now = LocalDateTime.now();
        List<FatigueFactor> fatiguesOfPastWeek = fatigueFactorService.findBetween(now.minusDays(7), now);

        // 2. get Goal and TrainingTimes
        Goal goal = runner.getGoal();
        List<TrainingTime> trainingTimes = runner.getTrainingTimes();

        // 3. request hourly weather forecast and create possible time candidates
        List<WeatherForecast> fullForecast = weatherAdapter.getHourlyForecast(runner.getLocation());
        List<TrainingHourCandidate> candidates = new ArrayList<>();

        for (WeatherForecast forecast : fullForecast) {
            LocalDateTime slot = forecast.time();

            if (!isWithinNext7Days(slot) || !isDaytime(forecast)) continue;

            TrainingHourCandidate candidate = new TrainingHourCandidate(slot, slot.plusHours(1), forecast);
            candidates.add(candidate);
        }

        return new RecommendationInput(runner, trainingTimes, fatiguesOfPastWeek, goal, candidates);
    }

    private boolean isDaytime(WeatherForecast forecast) {
        LocalTime time = forecast.time().toLocalTime();
        return !time.isBefore(LocalTime.of(6, 0)) && time.isBefore(LocalTime.of(22, 0));
    }

    private boolean isWithinNext7Days(LocalDateTime slot) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusDays(7);

        return !slot.isBefore(now) && slot.isBefore(end);
    }
}
