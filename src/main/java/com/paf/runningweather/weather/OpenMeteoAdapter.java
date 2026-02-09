package com.paf.runningweather.weather;

import com.paf.runningweather.location.Location;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adapter implementation that connects the application’s weather abstraction to the external Open-Meteo Weather API.
 */
@Service
public class OpenMeteoAdapter implements WeatherProvider {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Saved together with a run.
     * @return WeatherSnapshot at the current moment of request
     */
    @Override
    public WeatherSnapshot getCurrentWeather(Location location) {

        String url = buildUrl(location);

        // Perform HTTP GET request to fetch weather data for the built url
        OpenMeteoResponse response = restTemplate.getForObject(url, OpenMeteoResponse.class);

        // Extract current weather; throw if not available
        OpenMeteoResponse.CurrentWeather cw = response.getCurrent_weather();
        if (cw == null) {
            throw new IllegalStateException("Open-Meteo lieferte kein aktuelles Wetter für URL: " + url);
        }

        // Extract hourly data; used for precipitation and cloud cover
        OpenMeteoResponse.Hourly hourly = response.getHourly();
        if (hourly == null) {
            throw new IllegalStateException("Open-Meteo lieferte kein aktuelles Wetter für URL: " + url);
        }

        // data is not part of CurrentWeather, so get it from 'hourly' instead
        double rain = hourly.getPrecipitation().getFirst();
        double clouds = hourly.getCloudcover().getFirst();

        return new WeatherSnapshot(
                cw.getTemperature(),
                cw.getWindspeed(),
                rain,
                clouds,
                LocalDateTime.parse(cw.getTime())
        );
    }

    /**
     * Used to display weather forecast in the frontend, also for recommendations.
     * @return hourly weather forecast
     */
    @Override
    public List<WeatherForecast> getHourlyForecast(Location location) {

        // Fetch the full forecast data for hourly and daily information
        String url = buildUrl(location);
        OpenMeteoResponse response = restTemplate.getForObject(url, OpenMeteoResponse.class);

        OpenMeteoResponse.Hourly hourly = response.getHourly();
        OpenMeteoResponse.Daily daily = response.getDaily();

        if (hourly == null || daily == null) { throw new IllegalStateException("Open-Meteo lieferte kein aktuelles Wetter für URL: " + url);}

        // Precompute sunrise and sunset times for each day
        Map<LocalDate, LocalTime[]> dailySunTimes = getDailySunTimes(daily);


        List<WeatherForecast> forecasts = new ArrayList<>();

        // Iterate through hourly data and create WeatherForecast objects
        for (int i = 0; i < hourly.getTime().size(); i++) {

            LocalDateTime hourTime = LocalDateTime.parse(hourly.getTime().get(i));
            LocalDate day = hourTime.toLocalDate();

            LocalTime[] sunTimes = dailySunTimes.get(day);
            if (sunTimes == null) continue; // skip if sunTimes is not available

            // Determine if the current hour is during daylight
            boolean hasDaylight = hasDaylight(hourTime, sunTimes[0], sunTimes[1]);

            // Map raw weather values to an enum representing overall weather condition to display the weather icon in the frontend
            WeatherCondition condition = determineCondition(
                    hourly.getTemperature_2m().get(i),
                    hourly.getPrecipitation().get(i),
                    hourly.getWind_speed_10m().get(i),
                    hourly.getCloudcover().get(i),
                    hasDaylight
            );

            forecasts.add(new WeatherForecast(
                    hourly.getTemperature_2m().get(i),
                    hourly.getPrecipitation().get(i),
                    hourly.getWind_speed_10m().get(i),
                    hourly.getCloudcover().get(i),
                    hourTime,
                    condition,
                    hasDaylight
            ));
        }

        return forecasts;
    }

    /**
     * Parse sunrise and sunset times for each day into a map.
     * @param daily contains sunrise and sunset data
     * @return map of the day with sunrise and sunset times
     */
    private Map<LocalDate, LocalTime[]> getDailySunTimes(OpenMeteoResponse.Daily daily) {
        Map<LocalDate, LocalTime[]> dailySunTimes = new HashMap<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        for (int i = 0; i < daily.getTime().size(); i++) {
            LocalDate date = LocalDate.parse(daily.getTime().get(i));
            LocalTime sunrise = LocalDateTime.parse(daily.getSunrise().get(i), dtf).toLocalTime();
            LocalTime sunset = LocalDateTime.parse(daily.getSunset().get(i), dtf).toLocalTime();
            dailySunTimes.put(date, new LocalTime[]{sunrise, sunset});
        }

        return dailySunTimes;
    }

    /**
     * Construct the full API URL for weather requests with query parameters for the given location
     * @return url
     */
    private String buildUrl(Location location) {
        return "https://api.open-meteo.com/v1/forecast"
                + "?latitude=" + location.latitude()
                + "&longitude=" + location.longitude()
                + "&hourly=temperature_2m,precipitation,wind_speed_10m,cloudcover"
                + "&current_weather=true"
                + "&daily=sunrise,sunset";
    }

    /**
     * Determines if a certain time of the day has daylight.
     */
    private boolean hasDaylight(LocalDateTime time, LocalTime sunrise, LocalTime sunset) {
        LocalTime t = time.toLocalTime();
        return t.isAfter(sunrise) && t.isBefore(sunset);
    }

    /**
     * Picks the right WeatherCondition according to the forecast (to display weather icon in frontend).
     */
    private WeatherCondition determineCondition(double temperature, double precipitation, double windSpeed, double cloudCover, boolean hasDaylight) {

        if (!hasDaylight) {
            if (cloudCover > 80) return WeatherCondition.CLOUDY_NIGHT;
            return WeatherCondition.CLEAR_NIGHT;

        } else {

            if (precipitation > 3.0) return WeatherCondition.HEAVY_RAIN;
            if (precipitation > 0.2) return WeatherCondition.RAIN;
            if (cloudCover > 90) return WeatherCondition.OVERCAST;
            if (cloudCover > 60) return WeatherCondition.CLOUDY;
            if (windSpeed > 25) return WeatherCondition.WINDY;
            return WeatherCondition.CLEAR;
        }
    }

}
