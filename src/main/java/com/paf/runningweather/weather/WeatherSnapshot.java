package com.paf.runningweather.weather;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class WeatherSnapshot {
    private final double temperature;
    private final double windSpeed;
    private final double rain;
    private final double clouds;
    private final LocalDateTime timestamp;

    protected WeatherSnapshot(){
        this.temperature = 0;
        this.rain = 0;
        this.clouds = 0;
        this.windSpeed = 0;
        this.timestamp = null;
    };

    public WeatherSnapshot(double temperature, double windSpeed, double rain, double clouds, LocalDateTime timestamp) {
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.rain = rain;
        this.clouds = clouds;
        this.timestamp = timestamp;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getRain() {
        return rain;
    }

    public double getClouds() {
        return clouds;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
