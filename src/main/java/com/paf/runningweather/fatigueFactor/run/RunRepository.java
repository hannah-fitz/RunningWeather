package com.paf.runningweather.fatigueFactor.run;
import com.paf.runningweather.runner.Runner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RunRepository extends JpaRepository<Run, Long> {

    List<Run> findByRunner(Runner runner);

    @Query("""
            SELECT run FROM Run run
            WHERE run.runner.id = :runnerId
            AND run.datetime BETWEEN :start AND :end
            """)
    List<Run> findWeeklyRuns(Long runnerId, LocalDateTime start, LocalDateTime end);
}
