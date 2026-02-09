package com.paf.runningweather.trainingTimes;

import com.paf.runningweather.runner.RunnerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingTimeService {

    private final TrainingTimeRepository timeRepository;
    private final RunnerService runnerService;

    public TrainingTimeService(TrainingTimeRepository timeRepository, RunnerService runnerService) {
        this.timeRepository = timeRepository;
        this.runnerService = runnerService;
    }

    public TrainingTime save(TrainingTime preference) {
        preference.setRunner(runnerService.getCurrentRunner());
        return timeRepository.save(preference);
    }

    public List<TrainingTime> findForCurrentRunner() {
        return timeRepository.findByRunner(runnerService.getCurrentRunner());
    }

    public void deleteTrainingTime(Long id) {
        timeRepository.deleteById(id);
    }
}
