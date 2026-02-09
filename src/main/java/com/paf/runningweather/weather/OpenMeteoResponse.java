package com.paf.runningweather.weather;

import java.util.List;

/**
 * Internal DTO used to deserialize the Open-Meteo API response.
 */
public class OpenMeteoResponse {

    private CurrentWeather current_weather;
    private Hourly hourly;
    private Daily daily;

    public CurrentWeather getCurrent_weather() {
        return current_weather;
    }

    public Hourly getHourly() {
        return hourly;
    }

    public Daily getDaily() {
        return daily;
    }

    public static class CurrentWeather {
        private double temperature;
        private double windspeed;
        private String time;

        public double getTemperature() {
            return temperature;
        }

        public double getWindspeed() {
            return windspeed;
        }

        public String getTime() {
            return time;
        }
    }

    public static class Hourly {
        private List<String> time;
        private List<Double> temperature_2m;
        private List<Double> precipitation;
        private List<Double> wind_speed_10m;
        private List<Double> cloudcover;

        public List<String> getTime() {
            return time;
        }

        public List<Double> getTemperature_2m() {
            return temperature_2m;
        }

        public List<Double> getPrecipitation() {
            return precipitation;
        }

        public List<Double> getWind_speed_10m() {
            return wind_speed_10m;
        }

        public List<Double> getCloudcover() {
            return cloudcover;
        }
    }

    public static class Daily {
        private List<String> sunrise;
        private List<String> sunset;
        private List<String> time;

        public List<String> getSunrise() {
            return sunrise;
        }

        public List<String> getSunset() {
            return sunset;
        }

        public List<String> getTime() {
            return time;
        }
    }

}
