package com.paf.runningweather.fatigueFactor.run;
import com.paf.runningweather.fatigueFactor.FatigueFactor;
import com.paf.runningweather.runner.Runner;
import com.paf.runningweather.location.Location;
import com.paf.runningweather.weather.WeatherSnapshot;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Run extends FatigueFactor {

    private double distance;
    private int duration; // in minutes

    @Embedded
    private Location location;

    @Embedded
    private WeatherSnapshot weatherSnapshot;

    public Run() {}

    public Run(Runner runner, int fatigueLevel, LocalDateTime datetime, Location location, double distance, int duration, WeatherSnapshot weatherSnapshot) {
        super("run", fatigueLevel, datetime, runner);
        this.distance = distance;
        this.duration = duration;
        this.location = location;
        this.weatherSnapshot = weatherSnapshot;
    }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public WeatherSnapshot getWeatherSnapshot() { return weatherSnapshot; }
    public void setWeatherSnapshot(WeatherSnapshot weatherSnapshot) {
        this.weatherSnapshot = weatherSnapshot;
    }
}
