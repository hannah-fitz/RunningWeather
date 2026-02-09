package com.paf.runningweather.fatigueFactor.run;

import com.paf.runningweather.goal.GoalService;
import com.paf.runningweather.runner.Runner;
import com.paf.runningweather.runner.RunnerService;
import com.paf.runningweather.weather.WeatherProvider;
import com.paf.runningweather.weather.WeatherSnapshot;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RunService {
    private final RunRepository runRepository;
    private final RunnerService runnerService;
    private final GoalService goalService;
    private final WeatherProvider weatherProvider;

    public RunService (RunRepository runRepository, RunnerService runnerService, GoalService goalService, WeatherProvider weatherProvider) {
        this.runRepository = runRepository;
        this.runnerService = runnerService;
        this.goalService = goalService;
        this.weatherProvider = weatherProvider;
    }

    public List<Run> findAllRuns (Runner runner) {
        return runRepository.findByRunner(runner);
    }

    public Run save(Run run) {
        Runner runner = runnerService.getCurrentRunner();
        WeatherSnapshot weatherSnapshot = weatherProvider.getCurrentWeather(runner.getLocation());

        run.setRunner(runner);
        run.setCategory("run");
        run.setLocation(runner.getLocation());
        run.setWeatherSnapshot(weatherSnapshot);
        run.setFatigueLevel(calculateFatigueLevel(runner, run));

        Run savedRun = runRepository.save(run);
        goalService.updateProgress(runner.getGoal());
        return savedRun;
    }


    /**
     * Calculates the fatigue level (attribute of FatiguteFactor superclass) of a run on creation.
     * distanceFactor: how much impact on the fatigue does the distance of the run (compared to maxComfortableDistance of the runner) have?
     * paceFactor: how much impact on the fatigue does the pace of the run (compared to comfortablePace of the runner) have?
     *
     * @return fatigueLevel between 1 and 10
     */
    public int calculateFatigueLevel(Runner runner, Run run) {
        double relativeDistance = run.getDistance() / runner.getMaxComfortableDistance(); // z.B. 10km / 15km = 0.66
        double distanceFactor = Math.min(relativeDistance, 1.0);

        double relativePace = (run.getDuration() * 60.0 / run.getDistance()) / runner.getComfortablePace();
        double paceFactor = Math.max(0.5, Math.min(relativePace, 2.0)); // >1 → easy, <1 → hart

        double fatigueScore = distanceFactor * 0.7 + (1.0 / paceFactor) * 0.3;

        int fatigueLevel = (int) Math.round(fatigueScore * 10);
        return Math.max(1, Math.min(fatigueLevel, 10));
    }

    public Run getRunById(Long id) {
        return runRepository.findById(id).orElseThrow(() -> new RuntimeException("Run not found: " + id));
    }

    public Run updateRun (Long id, Run newRun) {
        Run run = runRepository.findById(id).orElseThrow(() -> new RuntimeException(("Run not found")));
        run.setDistance(newRun.getDistance());
        run.setDuration(newRun.getDuration());
        run.setDatetime(newRun.getDatetime());
        run.setFatigueLevel(calculateFatigueLevel(run.getRunner(), newRun));
        return runRepository.save(run);
    }

    public void deleteRun (Long id) {
        runRepository.deleteById(id);
    }
}


