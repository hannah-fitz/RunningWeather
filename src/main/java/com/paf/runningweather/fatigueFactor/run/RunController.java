package com.paf.runningweather.fatigueFactor.run;

import com.paf.runningweather.goal.GoalService;
import com.paf.runningweather.runner.Runner;
import com.paf.runningweather.runner.RunnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@Controller
@RequestMapping("/runs")
public class RunController {

    public final RunService runService;
    public final GoalService goalService;
    public final RunnerService runnerService;

    public RunController(RunService runService, GoalService goalService, RunnerService runnerService) {
        this.runService = runService;
        this.goalService = goalService;
        this.runnerService = runnerService;
    }

    /**
     * Groups all runs of the current runner by month,
     * sorts them by date descending,
     * and prepares them for display in the view.
     */
    @GetMapping
    public String getAllRuns(Model model) {
        Runner runner = runnerService.getCurrentRunner();
        List<Run> runs = runService.findAllRuns(runner);

        Map<YearMonth, List<Run>> runsByMonth = new LinkedHashMap<>();

        for (Run run : runs) {
            YearMonth month = YearMonth.from(run.getDatetime());
            if(!runsByMonth.containsKey(month)) {
                runsByMonth.put(month, new ArrayList<>());
            }
            runsByMonth.get(month).add(run);
        }

        for (List<Run> monthRuns : runsByMonth.values()) {
            monthRuns.sort(Comparator.comparing(Run::getDatetime).reversed());
        }

        model.addAttribute("runsByMonth", runsByMonth);
        return "runs";
    }

    @GetMapping("/new")
    public String showCreateRunForm(Model model) {
        Run run = new Run();
        run.setDatetime(LocalDateTime.now());

        model.addAttribute("run", run);
        return "create_edit_run";
    }

    @PostMapping
    public String createRun(@ModelAttribute("run") Run run) {
        runService.save(run);
        return "redirect:/fatigue/calendar";
    }

    @GetMapping("/edit/{id}")
    public String showEditRunForm (@PathVariable Long id, Model model) {
        Run run = runService.getRunById(id);
        model.addAttribute("run", run);
        return "create_edit_run";
    }

    @PostMapping("/update/{id}")
    public String updateRun (@PathVariable Long id, @ModelAttribute("run") Run run) {
        runService.updateRun(id, run);
        return "redirect:/fatigue/calendar";
    }

    @GetMapping("/delete/{id}")
    public String deleteRun (@PathVariable Long id) {
        runService.deleteRun(id);
        return "redirect:/fatigue/calendar";
    }
}
