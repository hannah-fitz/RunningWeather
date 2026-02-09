package com.paf.runningweather.fatigueFactor;

import com.paf.runningweather.runner.Runner;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class FatigueFactor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private int fatigueLevel;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime datetime;

    @ManyToOne
    private Runner runner;

    public FatigueFactor(){};

    public FatigueFactor(String category, int fatigueLevel, LocalDateTime datetime, Runner runner) {
        this.category = category;
        this.fatigueLevel = fatigueLevel;
        this.datetime = datetime;
        this.runner = runner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getFatigueLevel() {
        return fatigueLevel;
    }

    public void setFatigueLevel(int fatigueLevel) {
        this.fatigueLevel = fatigueLevel;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public Runner getRunner() {
        return runner;
    }

    public void setRunner(Runner runner) {
        this.runner = runner;
    }
}
