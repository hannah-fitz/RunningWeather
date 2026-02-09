package com.paf.runningweather.recommendation;

import com.paf.runningweather.goal.Goal;
import com.paf.runningweather.runner.Runner;
import com.paf.runningweather.runner.RunnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RecommendationController {
    public final RecommendationFacade recommendationFacade;
    public final RunnerService runnerService;

    public RecommendationController (RecommendationFacade recommendationFacade, RunnerService runnerService) {
        this.recommendationFacade = recommendationFacade;
        this.runnerService = runnerService;
    }

    @GetMapping
    public String showRecommendations (Model model) {
        Runner runner = runnerService.getCurrentRunner();
        Goal goal = runner.getGoal();

        List<RecommendedTrainingTime> recommendations = recommendationFacade.recommendFor(runner);

        model.addAttribute("goal", goal);
        model.addAttribute("recommendations", recommendations);
        return "index";
    }
}
