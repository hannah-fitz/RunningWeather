package com.paf.runningweather.trainingTimes;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;

@Controller
@RequestMapping("/times")
public class TrainingTimeController {

    private final TrainingTimeService trainingTimeService;

    public TrainingTimeController(TrainingTimeService trainingTimeService) {
        this.trainingTimeService = trainingTimeService;
    }

    @GetMapping("/new")
    public String showCreatePreferenceForm(Model model) {
        model.addAttribute("trainingTime", new TrainingTime());
        model.addAttribute("days", DayOfWeek.values());
        return "create_edit_times";
    }

    @PostMapping
    public String saveTrainingTime(@ModelAttribute("trainingTime") TrainingTime preference) {
        trainingTimeService.save(preference);
        return "redirect:/settings";
    }

    @GetMapping("/delete/{id}")
    public String deleteTrainingTime (@PathVariable Long id) {
        trainingTimeService.deleteTrainingTime(id);
        return "redirect:/settings";
    }


}
