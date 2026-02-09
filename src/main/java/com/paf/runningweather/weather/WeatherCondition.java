package com.paf.runningweather.weather;

/**
 * Possible weather conditions to be shown as symbols in frontend.
 */
public enum  WeatherCondition {
    CLEAR("sonne.png"),
    CLOUDY("cloudy.png"),
    RAIN("rain.png"),
    HEAVY_RAIN("heavy-rain.png"),
    WINDY("wind.png"),
    OVERCAST("overcast.png"),
    CLOUDY_NIGHT("cloudy_night.png"),
    CLEAR_NIGHT("clear_night.png");

    private final String icon;

    WeatherCondition(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }
}

