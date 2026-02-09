package com.paf.runningweather.weather;

import java.time.LocalDateTime;

public record WeatherForecast(
        double temperature,
        double rain,
        double windSpeed,
        double clouds,
        LocalDateTime time,
        WeatherCondition condition,
        boolean hasDaylight
) {

    public int roundedTemperature() {
        return (int) Math.round(temperature);
    }

    public int roundedRain() {
        return (int) Math.round(rain);
    }

    public int roundedWindSpeed() {
        return (int) Math.round(windSpeed);
    }

    public int roundedClouds() {
        return (int) Math.round(clouds);
    }
}