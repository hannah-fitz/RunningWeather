package com.paf.runningweather.location;

import java.util.List;

public interface GeocodingProvider {

    /**
     * @param query current user input in the location update form field at /settings
     * @return a list of LocationSuggestions for the user to choose the location to be saved
     */
    List<LocationSuggestion> suggest(String query);

    /**
     * Resolves a human-readable address to a concrete Location by taking the first suggestion returned by the geocoding API.
     * @param address
     * @return
     */
    Location geocode(String address);
}

