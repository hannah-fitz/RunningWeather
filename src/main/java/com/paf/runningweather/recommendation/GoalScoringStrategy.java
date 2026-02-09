package com.paf.runningweather.recommendation;

import com.paf.runningweather.goal.Goal;
import com.paf.runningweather.goal.GoalType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GoalScoringStrategy implements ScoringStrategy{

    /**
     * Calculates a score based on if the weekly goal is achieved or not.
     *
     * @return score between 0 and 1
     */
    @Override
    public double score(TrainingHourCandidate candidate, RecommendationInput input) {
        Goal goal = input.getWeeklyGoal();

        if(goal == null) {
            return 1.0;
        }

        return goal.isAchieved() ? 0.6 : 1.0;
    }

    @Override
    public double weight() {
        return 0.1;
    }

    @Override
    public List<String> getTags(TrainingHourCandidate candidate, RecommendationInput input) {
        List<String> tags = new ArrayList<>();
        Goal goal = input.getWeeklyGoal();

        if (goal == null || goal.isAchieved() || candidate.start().toLocalDate().isAfter(goal.getWeekEnd())) {
            return tags;
        }

        if (goal.getType() == GoalType.DISTANCE) tags.add("Kilometerziel offen");
        if (goal.getType() == GoalType.NUMBER) tags.add("Wochenziel offen");
        if (goal.getType() == GoalType.NUMBER && goal.getProgress() == goal.getTargetAmount()-1) tags.add("schließt Wochenziel ab");

        return tags;
    }


}
