package com.paf.runningweather;

import com.paf.runningweather.goal.Goal;
import com.paf.runningweather.location.Location;
import com.paf.runningweather.location.LocationUpdateDTO;
import com.paf.runningweather.runner.Runner;
import com.paf.runningweather.runner.RunnerService;
import com.paf.runningweather.trainingTimes.TrainingTime;
import com.paf.runningweather.trainingTimes.TrainingTimeService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class SettingsController {

    private final RunnerService runnerService;
    private final TrainingTimeService timeService;

    public SettingsController(RunnerService runnerService, TrainingTimeService timeService) {
        this.runnerService = runnerService;
        this.timeService = timeService;
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        Runner runner = runnerService.getCurrentRunner();
        Goal goal = runner.getGoal();
        List<TrainingTime> trainingTimes = timeService.findForCurrentRunner();

        // prefill DTO to show currently saved location in frontend input field
        LocationUpdateDTO locationDto = new LocationUpdateDTO();
        Location runnerLoc = runner.getLocation();
        if(runnerLoc != null) locationDto.setAddress(runnerLoc.address());

        model.addAttribute("goal", goal);
        model.addAttribute("trainingTimes", trainingTimes);
        model.addAttribute("locationUpdateDTO", locationDto);

        return "settings";
    }

    @PostMapping("/settings/update")
    public String updateRunner (@ModelAttribute("runner") Runner runner) {
        runnerService.updateRunner(runner);
        return "redirect:/settings";
    }

}
