package com.paf.runningweather.weather;

import java.time.LocalDate;
import java.util.List;

/**
 * Used to group forecast hours according to the days in the weather forecast frontend.
 * @param date
 * @param hourlyForecasts list of all hourly forecasts of that day
 */
public record DailyForecast(LocalDate date, List<WeatherForecast> hourlyForecasts) {

}
