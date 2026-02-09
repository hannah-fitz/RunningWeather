package com.paf.runningweather.runner;

import org.springframework.stereotype.Service;

@Service
public class RunnerService {

    private final RunnerRepository runnerRepository;

    public RunnerService(RunnerRepository runnerRepository) {
        this.runnerRepository = runnerRepository;
    }

    public Runner getCurrentRunner() {
        return runnerRepository.findAll().getFirst();
    }

    public Runner save(Runner runner) {
        return runnerRepository.save(runner);
    }

    public Runner updateRunner(Runner newRunner) {
        Runner current = getCurrentRunner();

        current.setLocation(newRunner.getLocation());
        current.setComfortablePace(newRunner.getComfortablePace());
        current.setMaxComfortableDistance(newRunner.getMaxComfortableDistance());

        return runnerRepository.save(current);
    }
}
