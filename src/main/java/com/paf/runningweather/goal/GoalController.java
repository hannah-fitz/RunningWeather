package com.paf.runningweather.goal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/goal")
public class GoalController {

    public final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping("/new")
    public String showCreateGoalForm(Model model) {
        model.addAttribute("goal", new Goal());
        return "create_edit_goal";
    }

    @PostMapping
    public String createGoal(@ModelAttribute("goal") Goal goal) {
        goalService.createGoal(goal);
        return "redirect:/settings";
    }

    @GetMapping("/edit/{id}")
    public String showEditGoalForm (@PathVariable Long id, Model model) {
        Goal goal = goalService.getGoalById(id);
        model.addAttribute("goal", goal);
        return "create_edit_goal";
    }

    @PostMapping("/update/{id}")
    public String updateGoal(@PathVariable Long id, @ModelAttribute("goal") Goal goal){
        goalService.updateGoal(id, goal);
        return "redirect:/settings";
    }
}
