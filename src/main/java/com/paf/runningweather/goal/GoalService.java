package com.paf.runningweather.goal;

import com.paf.runningweather.fatigueFactor.run.Run;
import com.paf.runningweather.fatigueFactor.run.RunRepository;
import com.paf.runningweather.runner.Runner;
import com.paf.runningweather.runner.RunnerRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final RunnerRepository runnerRepository;
    private final RunRepository runRepository;

    public GoalService(GoalRepository goalRepository, RunnerRepository runnerRepository, RunRepository runRepository) {
        this.goalRepository = goalRepository;
        this.runnerRepository = runnerRepository;
        this.runRepository = runRepository;
    }

    public Goal createGoal(Goal goal) {
        LocalDate now = LocalDate.now();
        goal.setWeekStart(now.with(DayOfWeek.MONDAY));
        goal.setWeekEnd(now.with(DayOfWeek.SUNDAY));

        goal.setRunner(runnerRepository.findAll().getFirst());
        goal.setProgress(0);
        updateProgress(goal);

        Goal savedGoal = goalRepository.save(goal);
        return savedGoal;
    }

    public Goal updateGoal(Long goalId, Goal updatedGoal) {
        Goal existingGoal = goalRepository.findById(goalId).orElseThrow(() -> new RuntimeException("Goal nicht gefunden"));

        existingGoal.setType(updatedGoal.getType());
        existingGoal.setTargetAmount(updatedGoal.getTargetAmount());

        updateProgress(existingGoal);
        return goalRepository.save(existingGoal);
    }

    /**
     * Updates goal progress, for example after a new run was saved or an existing run was edited.
     */
    public void updateProgress(Goal goal) {
        if(goal == null){ return; }

        Runner runner = goal.getRunner();

        List<Run> weeklyRuns = runRepository.findWeeklyRuns(runner.getId(), goal.getWeekStart().atStartOfDay(), goal.getWeekEnd().atTime(23, 59, 59));
        GoalType typeOfGoal = goal.getType();
        double progress = 0;

        for (Run run : weeklyRuns) {
            if (typeOfGoal == GoalType.NUMBER) {
                progress++;
            } else if (typeOfGoal == GoalType.DISTANCE) {
                progress += run.getDistance();
            }
        }

        goal.setProgress(progress);
        goalRepository.save(goal);
    }

    public List<Goal> getAllGoals() {
        return goalRepository.findAll();
    }

    public Goal getGoalById(Long id) {
        return goalRepository.findById(id).orElseThrow(() -> new RuntimeException("Run not found: " + id));
    }
}
