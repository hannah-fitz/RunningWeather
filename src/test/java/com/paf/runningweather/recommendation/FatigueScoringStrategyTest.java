package com.paf.runningweather.recommendation;

import com.paf.runningweather.fatigueFactor.FatigueFactor;
import com.paf.runningweather.weather.WeatherCondition;
import com.paf.runningweather.weather.WeatherForecast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FatigueScoringStrategyTest {

    private FatigueScoringStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new FatigueScoringStrategy();
    }

    @Test
    void runYesterdayShouldReduceScoreMoreThanOldManualFactor() {

        FatigueFactor runYesterday =
                new FatigueFactor("run", 8, LocalDateTime.now().minusDays(1), null);

        FatigueFactor badSleep =
                new FatigueFactor("bad sleep", 6, LocalDateTime.now().minusDays(4), null);

        RecommendationInput input =
                new RecommendationInput(
                        null,
                        List.of(),
                        List.of(runYesterday, badSleep),
                        null,
                        null
                );

        double scoreWithRun = strategy.score(dummyCandidate(), input);

        RecommendationInput manualOnlyInput =
                new RecommendationInput(
                        null,
                        List.of(),
                        List.of(badSleep),
                        null,
                        null
                );

        double scoreManualOnly = strategy.score(dummyCandidate(), manualOnlyInput);

        assertTrue(
                scoreWithRun < scoreManualOnly,
                "Ein Lauf von gestern sollte stärker wirken als ältere manuelle Ermüdung"
        );
    }

    @Test
    void fatigueOlderThanSevenDaysShouldBeIgnored() {

        FatigueFactor oldRun =
                new FatigueFactor("run", 10, LocalDateTime.now().minusDays(10), null);

        RecommendationInput input =
                new RecommendationInput(
                        null,
                        List.of(),
                        List.of(oldRun),
                        null,
                        null
                );

        double score = strategy.score(dummyCandidate(), input);

        assertEquals(1.0, score, 0.0001);
    }

    @Test
    void scoreIsAlwaysClampedBetweenZeroAndOne() {

        FatigueFactor extremeRun =
                new FatigueFactor("run", 10, LocalDateTime.now(), null);

        RecommendationInput input =
                new RecommendationInput(
                        null,
                        List.of(),
                        List.of(extremeRun, extremeRun),
                        null,
                        null
                );

        double score = strategy.score(dummyCandidate(), input);

        assertTrue(score >= 0.0 && score <= 1.0);
    }

    @Test
    void noFatigueFactorsShouldReturnPerfectScore() {
        RecommendationInput input =
                new RecommendationInput(null, List.of(), List.of(), null, null);

        double score = strategy.score(dummyCandidate(), input);

        assertEquals(1.0, score, 0.001);
    }

    @Test
    void runYesterdayShouldStronglyReduceScore() {
        FatigueFactor runYesterday =
                fatigue("run", 8, 1);

        RecommendationInput input =
                new RecommendationInput(null, List.of(), List.of(runYesterday), null, null);

        double score = strategy.score(dummyCandidate(), input);

        assertTrue(score < 0.6);
    }

    @Test
    void oldRunShouldOnlySlightlyReduceScore() {
        FatigueFactor runSixDaysAgo =
                fatigue("run", 7, 6);

        RecommendationInput input =
                new RecommendationInput(null, List.of(), List.of(runSixDaysAgo), null, null);

        double score = strategy.score(dummyCandidate(), input);

        assertTrue(score > 0.7);
    }

    @Test
    void manualFatigueIsWeakerThanRun() {
        RecommendationInput runInput =
                new RecommendationInput(null, List.of(),
                        List.of(fatigue("run", 6, 1)), null, null);

        RecommendationInput manualInput =
                new RecommendationInput(null, List.of(),
                        List.of(fatigue("stress", 6, 1)), null, null);

        double runScore = strategy.score(dummyCandidate(), runInput);
        double manualScore = strategy.score(dummyCandidate(), manualInput);

        assertTrue(runScore < manualScore);
    }

    @Test
    void recentRunAddsDistanceTag() {
        RecommendationInput input =
                new RecommendationInput(null, List.of(),
                        List.of(fatigue("run", 5, 3)), null, null);

        List<String> tags = strategy.getTags(dummyCandidate(), input);

        assertTrue(tags.contains("Abstand zur letzten Einheit"));
    }

    @Test
    void strongManualFatigueAddsHighFatigueTag() {
        RecommendationInput input =
                new RecommendationInput(null, List.of(),
                        List.of(fatigue("sleep", 8, 1)), null, null);

        List<String> tags = strategy.getTags(dummyCandidate(), input);

        assertTrue(tags.contains("hohe Ermüdung"));
    }

    @Test
    void lowFatigueAddsPositiveFatigueTag() {
        RecommendationInput input =
                new RecommendationInput(null, List.of(),
                        List.of(fatigue("sleep", 3, 1)), null, null);

        List<String> tags = strategy.getTags(dummyCandidate(), input);

        assertTrue(tags.contains("wenig Ermüdung"));
    }

    @Test
    void highFatigueYesterdayAddsNegativeFatigueTag() {
        RecommendationInput input =
                new RecommendationInput(null, List.of(),
                        List.of(fatigue("strength training", 8, 1)), null, null);

        List<String> tags = strategy.getTags(dummyCandidate(), input);

        assertTrue(tags.contains("hohe Ermüdung"));
    }

    @Test
    void highFatigueTodayAddsNegativeFatigueTag() {
        RecommendationInput input =
                new RecommendationInput(null, List.of(),
                        List.of(fatigue("strength training", 7, 0)), null, null);

        List<String> tags = strategy.getTags(dummyCandidate(), input);

        assertTrue(tags.contains("hohe Ermüdung"));
    }

    @Test
    void mixedFatigueStillProducesConsistentTags() {
        RecommendationInput input =
                new RecommendationInput(null, List.of(),
                        List.of(
                                fatigue("run", 6, 3),
                                fatigue("stress", 7, 2)
                        ), null, null);

        List<String> tags = strategy.getTags(dummyCandidate(), input);

        assertTrue(tags.contains("hohe Ermüdung"));
        assertTrue(tags.contains("Abstand zur letzten Einheit"));
    }

    // -------------------------
    // Testdaten
    // -------------------------

    private FatigueFactor fatigue(String category, int level, int daysAgo) {
        return new FatigueFactor(
                category,
                level,
                LocalDateTime.now().minusDays(daysAgo),
                null
        );
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
