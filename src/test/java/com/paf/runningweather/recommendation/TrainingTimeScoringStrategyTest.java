package com.paf.runningweather.recommendation;

import com.paf.runningweather.fatigueFactor.FatigueFactor;
import com.paf.runningweather.trainingTimes.TrainingTime;
import com.paf.runningweather.weather.WeatherCondition;
import com.paf.runningweather.weather.WeatherForecast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrainingTimeScoringStrategyTest {

    private TrainingTimeScoringStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new TrainingTimeScoringStrategy();
    }


    @Test
    void preferredTimeShouldScoreHigher() {

        TrainingTime pref = new TrainingTime();
        pref.setDayOfWeek(LocalDate.now().getDayOfWeek());
        pref.setStartTime(LocalTime.of(10, 0));
        pref.setEndTime(LocalTime.of(12, 0));

        RecommendationInput input =
                new RecommendationInput(
                        null,
                        List.of(pref),
                        List.of(),
                        null,
                        null
                );

        TrainingHourCandidate candidate =
                new TrainingHourCandidate(
                        LocalDateTime.now().withHour(10),
                        LocalDateTime.now().withHour(11),
                        dummyWeather()
                );

        TrainingTimeScoringStrategy strategy = new TrainingTimeScoringStrategy();

        double score = strategy.score(candidate, input);

        assertEquals(1.0, score);
    }

    private WeatherForecast dummyWeather() {
        return new WeatherForecast(
                20.0, 0.0, 5.0, 30.0,
                LocalDateTime.now(),
                WeatherCondition.CLEAR,
                true
        );
    }
}
