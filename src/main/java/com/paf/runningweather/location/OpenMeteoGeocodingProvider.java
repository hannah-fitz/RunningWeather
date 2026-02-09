package com.paf.runningweather.location;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Adapter implementation that connects the application’s geocoding abstraction to the external Open-Meteo Geocoding API.
 */
@Service
public class OpenMeteoGeocodingProvider implements GeocodingProvider {

    private final WebClient webClient;

    // Initializes a WebClient configured with the Open-Meteo geocoding base URL.
    public OpenMeteoGeocodingProvider(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://geocoding-api.open-meteo.com/v1/search").build();
    }

    /**
     * Calls the Open-Meteo search endpoint to retrieve location suggestions matching the given query string.
     * @param query current user input in the location update form field at /settings
     * @return an empty list if the query is blank or the API response contains no results;
     */
    @Override
    public List<LocationSuggestion> suggest(String query) {
        GeocodingResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("name", query).build())
                .retrieve()
                .bodyToMono(GeocodingResponse.class)
                .block();

        if (query.isBlank() || response == null || response.results == null || response.results.isEmpty()) {
            return List.of();
        }

        return response.results.stream()
                .map(r -> new LocationSuggestion(r.name + ", " + r.country, r.latitude, r.longitude))
                .toList();

    }

    /**
     * Resolves a human-readable address to a concrete Location by taking the first suggestion returned by the geocoding API.
     * @param address
     * @return
     */
    @Override
    public Location geocode(String address) {
        List<LocationSuggestion> suggestions = suggest(address);
        if (suggestions.isEmpty()) throw new IllegalArgumentException("Adresse nicht gefunden.");
        LocationSuggestion first = suggestions.getFirst();
        return new Location(first.latitude(), first.longitude(), first.address());
    }

    /**
     *  Internal DTO used to deserialize the Open-Meteo API response.
     */
    public static class GeocodingResponse {
        public List<Result> results;
        public static class Result {
            public String name;
            public String country;
            public double latitude;
            public double longitude;
        }
    }
}
