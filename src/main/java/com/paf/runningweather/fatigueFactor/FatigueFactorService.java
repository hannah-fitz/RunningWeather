package com.paf.runningweather.fatigueFactor;

import com.paf.runningweather.runner.Runner;
import com.paf.runningweather.runner.RunnerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FatigueFactorService {
    private final FatigueRepository fatigueRepository;
    private final RunnerService runnerService;

    public FatigueFactorService(FatigueRepository fatigueRepository, RunnerService runnerService) {
        this.fatigueRepository = fatigueRepository;
        this.runnerService = runnerService;
    }

    public List<FatigueFactor> findAllFatigueFactors() {
        Runner runner = runnerService.getCurrentRunner();
        return fatigueRepository.findByRunner(runner);
    }

    public List<FatigueFactor> findBetween(LocalDateTime start, LocalDateTime end) {
        Runner runner = runnerService.getCurrentRunner();
        return fatigueRepository.findBetween(runner, start, end);
    }

    public void save(FatigueFactor fatigueFactor) {
        fatigueFactor.setRunner(runnerService.getCurrentRunner());
        fatigueRepository.save(fatigueFactor);
    }

    public FatigueFactor getFatigueById(Long id) {
        return fatigueRepository.findById(id).orElseThrow(() -> new RuntimeException("Fatigue not found: " + id));
    }

    public FatigueFactor updateFatigue(Long id, FatigueFactor newFatigue) {
        FatigueFactor fatigue = fatigueRepository.findById(id).orElseThrow(() -> new RuntimeException(("Fatigue not found")));
        fatigue.setFatigueLevel(newFatigue.getFatigueLevel());
        fatigue.setCategory(newFatigue.getCategory());
        fatigue.setDatetime(newFatigue.getDatetime());
        return fatigueRepository.save(fatigue);
    }

    public void deleteFatigue(Long id) {
        fatigueRepository.deleteById(id);
    }
}