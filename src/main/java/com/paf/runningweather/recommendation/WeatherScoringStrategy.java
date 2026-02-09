package com.paf.runningweather.recommendation;

import com.paf.runningweather.weather.WeatherForecast;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WeatherScoringStrategy implements ScoringStrategy {

    /**
     * Calculates a score for the TrainingHourCandidate based on the weather forecast at that time.
     *
     * @return score between 0 and 1
     */
    @Override
    public double score(TrainingHourCandidate candidate, RecommendationInput input) {
        WeatherForecast forecast = candidate.weather();

        double temperatureScore = scoreTemperature(forecast.temperature());
        double rainScore = scoreRain(forecast.rain());
        double windScore = scoreWind(forecast.windSpeed());
        double cloudScore = scoreClouds(forecast.clouds());
        double daylightScore = scoreDaylight(forecast.hasDaylight());

        // temperature will influence the final score just as strong as the other factors all togehter
        double conditionsScore = (rainScore + windScore + cloudScore + daylightScore) / 4.0;
        return (temperatureScore + conditionsScore) / 2.0;
    }

    private double scoreDaylight(boolean hasDaylight) {
        return hasDaylight ? 1.0 : 0.0;
    }

    private double scoreClouds(double clouds) {
        if (clouds > 90) return 0.6;
        if (clouds > 70) return 0.7;
        if (clouds > 40) return 0.85;
        return 1.0; // clouds lower than 40% optimal
    }

    private double scoreWind(double windSpeed) {
        if (windSpeed > 30) return 0.0;
        if (windSpeed > 20) return 0.3;
        if (windSpeed > 15) return 0.5;
        if (windSpeed > 10) return 0.7;
        if (windSpeed > 5) return 0.9;
        return 1.0; // wind lower than 5 km/h optimal
    }

    private double scoreRain(double rain) {
        if (rain > 3.0) return 0.0;
        if (rain > 1.5) return 0.2;
        if (rain > 1.0) return 0.3;
        if (rain > 0.5) return 0.4;
        if (rain > 0.2) return 0.6;
        return 1.0; // 0 mm of rain optimal
    }

    /**
     * Calculates a normalized comfort score for a given temperature using a Gaussian curve.
     * @param temp temperature input to be scored
     * @return score between 0 and 1
     */
    private double scoreTemperature(double temp) {
        if (temp < -10 || temp > 30) {
            return 0.0;
        }

        double optimal = 15.0;   // most comfortable temperature
        double sigma = 7.5;      // width of the curve

        double diff = temp - optimal;
        double score = Math.exp(-(diff * diff) / (2 * sigma * sigma));

        return clamp(score);
    }

    private double clamp(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }

    @Override
    public double weight() {
        return 0.25;
    }

    @Override
    public List<String> getTags(TrainingHourCandidate candidate, RecommendationInput input) {
        List<String> tags = new ArrayList<>();
        WeatherForecast forecast = candidate.weather();

        if (forecast.temperature() >= 11 && forecast.temperature() <= 18) tags.add("perfekte Temperatur");
        if (forecast.temperature() > 15) tags.add("T-Shirt-Wetter");
        if (forecast.temperature() < 4) tags.add("warm einpacken");
        if (forecast.rain() < 0.2) tags.add("kein Regen");
        if (forecast.windSpeed() < 5) tags.add("kaum Wind");
        if (forecast.clouds() < 40 && forecast.clouds() > 20) tags.add("wenig bewölkt");
        if (forecast.clouds() <= 20) tags.add("blauer Himmel");
        if (forecast.hasDaylight()) tags.add("Tageslicht");

        return tags;
    }

}
