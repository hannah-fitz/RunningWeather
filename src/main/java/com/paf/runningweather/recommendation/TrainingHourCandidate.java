package com.paf.runningweather.recommendation;

import com.paf.runningweather.weather.WeatherForecast;

import java.time.LocalDateTime;

public record TrainingHourCandidate (

    LocalDateTime start,
    LocalDateTime end,

    WeatherForecast weather
) {}
