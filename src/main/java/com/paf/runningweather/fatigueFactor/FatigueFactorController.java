package com.paf.runningweather.fatigueFactor;

import com.paf.runningweather.runner.RunnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/fatigue")
public class FatigueFactorController {
    public final FatigueFactorService fatigueService;
    public final RunnerService runnerService;

    public FatigueFactorController(FatigueFactorService fatigueService, RunnerService runnerService) {
        this.fatigueService = fatigueService;
        this.runnerService = runnerService;
    }

    /**
     * Builds a 40-day calendar view by grouping fatigue-related entries per day
     * and preparing them for rendering in the runs view.
     */
    @GetMapping("/calendar")
    public String getCalendarView(Model model) {

        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(39);

        List<FatigueFactor> fatigueFactors = fatigueService.findBetween(start.atStartOfDay(), today.atTime(23, 59));

        Map<LocalDate, List<FatigueFactor>> fatigueFactorsByDay = new HashMap<>();

        for (FatigueFactor fatigueFactor : fatigueFactors) {
            LocalDate day = fatigueFactor.getDatetime().toLocalDate();
            fatigueFactorsByDay.computeIfAbsent(day, d -> new ArrayList<>()).add(fatigueFactor);
        }

        List <DayView> days = new ArrayList<>();

        for(int i = 0; i < 40; i++) {
            LocalDate date = start.plusDays(i);
            List <FatigueFactor> dayEntries = fatigueFactorsByDay.getOrDefault(date, List.of());

            days.add(new DayView(date, dayEntries));
        }

        model.addAttribute("days", days.reversed());
        return "runs";
    }

    @GetMapping("/new")
    public String showCreateFatigueForm(Model model) {
        FatigueFactor fatigueFactor = new FatigueFactor();
        fatigueFactor.setDatetime(LocalDateTime.now());

        model.addAttribute("fatigueFactor", fatigueFactor);
        return "create_edit_fatigue";
    }

    @PostMapping
    public String createFatigue(@ModelAttribute("fatigueFactor") FatigueFactor fatigueFactor) {
        fatigueService.save(fatigueFactor);
        return "redirect:/fatigue/calendar";
    }

    @GetMapping("/edit/{id}")
    public String showEditFatigueForm(@PathVariable Long id, Model model) {
        FatigueFactor fatigue = fatigueService.getFatigueById(id);
        model.addAttribute("fatigueFactor", fatigue);
        return "create_edit_fatigue";
    }

    @PostMapping("/update/{id}")
    public String updateFatigue(@PathVariable Long id, @ModelAttribute("fatigueFactor") FatigueFactor fatigueFactor) {
        fatigueService.updateFatigue(id, fatigueFactor);
        return "redirect:/fatigue/calendar";
    }

    @GetMapping("/delete/{id}")
    public String deleteFatigue(@PathVariable Long id) {
        fatigueService.deleteFatigue(id);
        return "redirect:/fatigue/calendar";
    }
}