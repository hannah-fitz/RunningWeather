package com.paf.runningweather.weather;

import com.paf.runningweather.location.Location;
import com.paf.runningweather.runner.Runner;
import com.paf.runningweather.runner.RunnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/weather")
public class WeatherForecastController {

    public final WeatherForecastService weatherForecastService;
    public final RunnerService runnerService;

    public WeatherForecastController(WeatherForecastService weatherForecastService, RunnerService runnerService) {
        this.weatherForecastService = weatherForecastService;
        this.runnerService = runnerService;
    }

    @GetMapping
    public String forecast(Model model) {
        return "weather";
    }

    /**
     * Used for showing the daily weather forecasts in the frontend (ajax).
     * @param dayOffset 0 -> today, 1 -> tomorrow, 2 -> day after tomorrow
     */
    @GetMapping("/{dayOffset}")
    public String forecastForDay(@PathVariable int dayOffset, Model model) {
        Location location = runnerService.getCurrentRunner().getLocation();

        DailyForecast dailyForecast = weatherForecastService.getForecastForDay(location, dayOffset);

        model.addAttribute("day", dailyForecast);
        return "fragments/forecast_day :: forecast_day";
    }

}
