package com.paf.runningweather.weather;

import com.paf.runningweather.location.Location;

import java.util.List;

public interface WeatherProvider {
    /**
     * @param location request location, saved with the runner
     * @return WeatherSnapshot at this moment of request
     */
    WeatherSnapshot getCurrentWeather(Location location);

    /**
     * @param location request location, saved with the runner
     * @return hourly weather forecast for the next seven days
     */
    List<WeatherForecast> getHourlyForecast(Location location);
}
