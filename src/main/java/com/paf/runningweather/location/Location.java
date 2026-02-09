package com.paf.runningweather.location;

import jakarta.persistence.Embeddable;

/**
 * Location to be saved with the runner.
 * @param latitude
 * @param longitude
 * @param address
 */
@Embeddable
public record Location (double latitude, double longitude, String address){
}
