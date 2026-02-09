package com.paf.runningweather.recommendation;

import com.paf.runningweather.fatigueFactor.FatigueFactor;
import com.paf.runningweather.goal.Goal;
import com.paf.runningweather.weather.WeatherCondition;
import com.paf.runningweather.weather.WeatherForecast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class GoalScoringStrategyTest {

    private GoalScoringStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new GoalScoringStrategy();
    }

    @Test
    void achievedGoalShouldLowerScore() {

        Goal goal = Mockito.mock(Goal.class);
        when(goal.isAchieved()).thenReturn(true);

        RecommendationInput input =
                new RecommendationInput(
                        null,
                        List.of(),
                        List.of(),
                        goal,
                        null
                );

        GoalScoringStrategy strategy = new GoalScoringStrategy();

        double score = strategy.score(dummyCandidate(), input);

        assertEquals(0.3, score);
    }

    private TrainingHourCandidate dummyCandidate() {
        return new TrainingHourCandidate(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                dummyWeather()
        );
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
