package com.paf.runningweather.recommendation;

import com.paf.runningweather.weather.WeatherCondition;
import com.paf.runningweather.weather.WeatherForecast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WeatherScoringStrategyTest {

    private WeatherScoringStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new WeatherScoringStrategy();
    }

    @Test
    void shouldReturnLowScoreForHeavyRain() {

        WeatherForecast forecast = new WeatherForecast(
                15.0,
                5.0,
                5.0,
                50.0,
                LocalDateTime.now(),
                WeatherCondition.HEAVY_RAIN,
                true
        );

        TrainingHourCandidate candidate =
                new TrainingHourCandidate(
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(1),
                        forecast
                );

        RecommendationInput input =
                new RecommendationInput(
                        null,
                        List.of(),
                        List.of(),
                        null,
                        null
                );

        double score = strategy.score(candidate, input);

        assertTrue(score < 0.5);
    }

    @Test
    void shouldPenalizeHighTemperature() {

        WeatherForecast forecast = new WeatherForecast(
                35.0,
                0.0,
                5.0,
                10.0,
                LocalDateTime.now(),
                WeatherCondition.CLEAR,
                true
        );

        TrainingHourCandidate candidate =
                new TrainingHourCandidate(
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(1),
                        forecast
                );

        RecommendationInput input =
                new RecommendationInput(
                        null,
                        List.of(),
                        List.of(),
                        null,
                        null
                );

        double score = strategy.score(candidate, input);

        assertTrue(score < 0.7);
    }

    @Test
    void shouldReturnHighScoreForIdealRunningWeather() {

        WeatherForecast forecast = new WeatherForecast(
                14.0,
                0.0,
                5.0,
                20.0,
                LocalDateTime.now(),
                WeatherCondition.CLEAR,
                true
        );

        TrainingHourCandidate candidate =
                new TrainingHourCandidate(
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(1),
                        forecast
                );

        RecommendationInput input =
                new RecommendationInput(
                        null,
                        List.of(),
                        List.of(),
                        null,
                        null
                );

        double score = strategy.score(candidate, input);

        assertTrue(score > 0.8,
                "Ideal weather should produce a high score");
    }


    @Test
    void explainScoresIndividually() {

        // Testdaten
        WeatherForecast forecast = new WeatherForecast(
                35.0, 0.0, 5.0, 20.0, LocalDateTime.now().plusHours(2), WeatherCondition.CLEAR, true
        );
        TrainingHourCandidate candidate =
                new TrainingHourCandidate(forecast.time(), forecast.time().plusHours(1), forecast);

        RecommendationInput input = new RecommendationInput(
                null,
                List.of(),
                List.of(),
                null,
                List.of(candidate)
        );

        // Instanziiere einzelne Strategies
        ScoringStrategy weatherStrategy = new WeatherScoringStrategy();
        ScoringStrategy temporalStrategy = new TemporalProximityScoringStrategy();

        double weatherScore = weatherStrategy.score(candidate, input);
        double temporalScore = temporalStrategy.score(candidate, input);

        System.out.println("Weather Score: " + weatherScore);
        System.out.println("Temporal Score: " + temporalScore);

        double aggregated = weatherScore * weatherStrategy.weight()
                + temporalScore * temporalStrategy.weight();

        System.out.println("Aggregated Score: " + aggregated);

        assertTrue(aggregated > 0.6);
    }


}
