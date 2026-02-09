package com.paf.runningweather.location;

/**
 * Location suggestion shown when editing the location.
 * The chosen location will be saved with the runner as a Location record.
 */
public record LocationSuggestion(String address, double latitude, double longitude) {}
