package com.paf.runningweather.config;

import com.paf.runningweather.runner.Runner;
import com.paf.runningweather.runner.RunnerRepository;
import com.paf.runningweather.location.Location;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoDataLoader implements CommandLineRunner {

    private final RunnerRepository runnerRepository;

    public DemoDataLoader(RunnerRepository runnerRepository) {
        this.runnerRepository = runnerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (runnerRepository.count() == 0) {
            Location location = new Location(48.3928, 10.0111, "Ulm");
            Runner demoRunner = new Runner("Demo Runner", location, 15.0, 384);
            runnerRepository.save(demoRunner);
        }
    }
}
