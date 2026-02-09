package com.paf.runningweather.trainingTimes;

import com.paf.runningweather.runner.Runner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingTimeRepository  extends JpaRepository<TrainingTime, Long> {
    List<TrainingTime> findByRunner(Runner runner);
}
