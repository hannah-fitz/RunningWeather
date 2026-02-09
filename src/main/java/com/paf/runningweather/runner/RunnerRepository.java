package com.paf.runningweather.runner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunnerRepository extends JpaRepository<Runner, Long> {
}
