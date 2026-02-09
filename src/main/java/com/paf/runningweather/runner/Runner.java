package com.paf.runningweather.runner;

import com.paf.runningweather.fatigueFactor.FatigueFactor;
import com.paf.runningweather.goal.Goal;
import com.paf.runningweather.trainingTimes.TrainingTime;
import com.paf.runningweather.location.Location;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Runner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double maxComfortableDistance; // in km
    private int comfortablePace; // in sec/km (for example: 384 sec/km for a pace of 6:40 min/km)

    @Embedded
    private Location location;

    public Runner(){}
    public Runner(String name, Location location, double maxComfortableDistance, int comfortablePace) {
        this.name = name;
        this.location = location;
        this.maxComfortableDistance = maxComfortableDistance;
        this.comfortablePace = comfortablePace;
    }

    @OneToMany(mappedBy = "runner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FatigueFactor> fatigueFactors = new ArrayList<>();

    @OneToOne(mappedBy = "runner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Goal goal;

    @OneToMany(mappedBy = "runner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingTime> trainingTimes = new ArrayList<>();

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Goal getGoal() { return goal; }
    public void setGoal(Goal goal) { this.goal = goal; }

    public List<FatigueFactor> getFatigueFactors() {
        return fatigueFactors;
    }

    public void setFatigueFactors(List<FatigueFactor> fatigueFactors) {
        this.fatigueFactors = fatigueFactors;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<TrainingTime> getTrainingTimes() {
        return trainingTimes;
    }

    public void setTrainingTimes(List<TrainingTime> trainingTimes) {
        this.trainingTimes = trainingTimes;
    }

    public double getMaxComfortableDistance() {
        return maxComfortableDistance;
    }

    public void setMaxComfortableDistance(double maxComfortableDistance) {
        this.maxComfortableDistance = maxComfortableDistance;
    }

    public int getComfortablePace() {
        return comfortablePace;
    }

    public void setComfortablePace(int comfortablePace) {
        this.comfortablePace = comfortablePace;
    }

}
