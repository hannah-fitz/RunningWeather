package com.paf.runningweather.weather;

import com.paf.runningweather.location.Location;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherForecastService {

    private final WeatherProvider weatherProvider;

    public WeatherForecastService (WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    /**
     * Groups hourly weather forecasts into daily forecasts for displaying the forecast in the frontend.
     * @return list of DailyForecast records
     */
    public List<DailyForecast> getForecastForNextDays (Location location) {
        // Fetch all hourly forecasts for the given location
        List<WeatherForecast> allForecasts = weatherProvider.getHourlyForecast(location);
        LocalDateTime now = LocalDateTime.now();

        // Map to group hourly forecasts by date
        Map<LocalDate, List<WeatherForecast>> upcomingHours = new LinkedHashMap<>();

        for (WeatherForecast forecast : allForecasts) {
            // Include only future or current forecasts (skip past hours)
            if (!forecast.time().isBefore(now)) {
                LocalDate date = forecast.time().toLocalDate();
                upcomingHours.computeIfAbsent(date, d -> new ArrayList<>()).add(forecast);
            }
        }

        // Transform the grouped forecasts into DailyForecast objects
        List<DailyForecast> dailyForecasts = new ArrayList<>();
        for(Map.Entry<LocalDate, List<WeatherForecast>> entry : upcomingHours.entrySet()) {
            dailyForecasts.add(new DailyForecast(entry.getKey(), entry.getValue()));
        }

        return dailyForecasts;
    }

    public DailyForecast getForecastForDay(Location location, int dayOffset) {
        List<DailyForecast> days = getForecastForNextDays(location);
        return days.get(dayOffset);
    }
}
