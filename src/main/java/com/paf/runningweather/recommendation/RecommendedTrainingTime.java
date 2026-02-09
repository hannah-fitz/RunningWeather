package com.paf.runningweather.recommendation;

import com.paf.runningweather.weather.WeatherCondition;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO to transfer a recommended training time to the frontend to be shown in the list.
 */
public class RecommendedTrainingTime {

    private final TrainingHourCandidate trainingHourCandidate;
    private double score;
    private final List<String> tags;

    public RecommendedTrainingTime(TrainingHourCandidate trainingHourCandidate, double score, List<String> tags) {
        this.trainingHourCandidate = trainingHourCandidate;
        this.score = score;
        this.tags = tags;
    }


    public TrainingHourCandidate getTrainingHourCandidate() {
        return trainingHourCandidate;
    }

    public double getScore() {
        return score;
    }

    public int getScorePercent() {
        return (int) (score * 100);
    }

    public void setScore(double score) {
        this.score = score;
    }

    public WeatherCondition getWeatherCondition() {
        return getTrainingHourCandidate().weather().condition();
    }

    public LocalDateTime getTime() {
        return getTrainingHourCandidate().start();
    }

    public int getRoundedTemperature() {
        return getTrainingHourCandidate().weather().roundedTemperature();
    }

    public int getRoundedWindSpeed() {
        return getTrainingHourCandidate().weather().roundedWindSpeed();
    }

    public int getRoundedRain() {
        return getTrainingHourCandidate().weather().roundedRain();
    }

    public int getRoundedClouds() {
        return getTrainingHourCandidate().weather().roundedClouds();
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public List<String> getTags() {
        return tags;
    }

}
