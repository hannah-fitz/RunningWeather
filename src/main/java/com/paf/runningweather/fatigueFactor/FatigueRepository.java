package com.paf.runningweather.fatigueFactor;
import com.paf.runningweather.fatigueFactor.FatigueFactor;
import com.paf.runningweather.runner.Runner;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FatigueRepository extends JpaRepository<FatigueFactor, Long> {

    List<FatigueFactor> findByRunner(Runner runner);

    @Query("""
            SELECT f FROM FatigueFactor f
            WHERE f.runner = :runner
            AND f.datetime BETWEEN :start AND :end
            """)
    List<FatigueFactor> findBetween(Runner runner, LocalDateTime start, LocalDateTime end);
}
