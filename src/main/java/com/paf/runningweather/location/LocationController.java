package com.paf.runningweather.location;

import com.paf.runningweather.runner.Runner;
import com.paf.runningweather.runner.RunnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class LocationController {
    private final RunnerService runnerService;
    private final GeocodingProvider geocodingProvider;

    public LocationController (RunnerService runnerService, GeocodingProvider geocodingProvider) {
        this.runnerService = runnerService;
        this.geocodingProvider = geocodingProvider;
    }

    @PostMapping("/settings/location")
    public String updateLocation(@ModelAttribute LocationUpdateDTO locationDto) {
        Runner runner = runnerService.getCurrentRunner();
        Location newLocation = new Location(locationDto.getLatitude(), locationDto.getLongitude(), locationDto.getAddress());
        runner.setLocation(newLocation);
        runnerService.save(runner);
        return "redirect:/settings";
    }

    // Autocomplete API
    @GetMapping("/geocode")
    @ResponseBody
    public List<LocationSuggestion> geocode(@RequestParam String query) {
        return geocodingProvider.suggest(query);
    }

}
