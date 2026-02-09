package com.paf.runningweather.trainingTimes;

import com.paf.runningweather.runner.Runner;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Preferred training time slots to be saved by the runner.
 */
@Entity
public class TrainingTime {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne
    private Runner runner;

    public TrainingTime(){};
    public TrainingTime(Runner runner, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.runner = runner;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Runner getRunner() {
        return runner;
    }

    public void setRunner(Runner runner) {
        this.runner = runner;
    }

    public String getDayOfWeekLabel() {
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.GERMAN);
    }
}
