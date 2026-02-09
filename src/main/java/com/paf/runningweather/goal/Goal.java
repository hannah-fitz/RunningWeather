package com.paf.runningweather.goal;

import com.paf.runningweather.runner.Runner;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int targetAmount;
    private double progress;
    private LocalDate weekStart;
    private LocalDate weekEnd;

    @Enumerated(EnumType.STRING)
    private GoalType type;

    public Goal(){};

    public Goal(int targetAmount, int progress, LocalDate weekStart, LocalDate weekEnd, GoalType type) {
        this.targetAmount = targetAmount;
        this.progress = progress;
        this.weekStart = weekStart;
        this.weekEnd = weekEnd;
        this.type = type;
    }

    @OneToOne
    @JoinColumn(name = "runner_id", nullable = true)
    private Runner runner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(int targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }


    public boolean isAchieved() {
        return progress >= targetAmount;
    }

    public LocalDate getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(LocalDate weekStart) {
        this.weekStart = weekStart;
    }

    public LocalDate getWeekEnd() {
        return weekEnd;
    }

    public void setWeekEnd(LocalDate weekEnd) {
        this.weekEnd = weekEnd;
    }

    public GoalType getType() {
        return type;
    }

    public void setType(GoalType type) {
        this.type = type;
    }

    public Runner getRunner() { return runner; }
    public void setRunner(Runner runner) { this.runner = runner; }

}
