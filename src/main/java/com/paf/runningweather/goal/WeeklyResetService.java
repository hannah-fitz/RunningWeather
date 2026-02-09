package com.paf.runningweather.goal;

import com.paf.runningweather.runner.RunnerService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Service responsible for resetting a runner’s weekly goal progress at the start of a new training week.
 */
@Service
public class WeeklyResetService {

    private final RunnerService runnerService;

    public WeeklyResetService(RunnerService runnerService) {
        this.runnerService = runnerService;
    }

    /**
     * Resets the given goal if a new calendar week has started since the last reset.
     */
    public void resetNow(Goal goal) {
        LocalDate now = LocalDate.now();
        LocalDate currentMonday = now.with(DayOfWeek.MONDAY);

        if(goal != null &&
                (goal.getWeekStart() == null || goal.getWeekStart().isBefore(currentMonday))) {
            goal.setWeekStart(currentMonday);
            goal.setWeekEnd(now.with(DayOfWeek.SUNDAY));
            goal.setProgress(0);
        }
    }

    /**
     * Automatically resets the current runner’s goal every Monday at 01:00 (Europe/Berlin).
     * This will only work if the application is running at that time.
     */
    @Transactional
    @Scheduled(cron = "0 0 1 * * MON", zone = "Europe/Berlin") // jeden Montag um 01:00
    public void scheduledReset() {
        resetNow(runnerService.getCurrentRunner().getGoal());
    }

    /**
     * Ensures the weekly goal is consistent when the application starts,
     * even if the application was not running at scheduled reset time.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void resetOnAppStart() {
        resetNow(runnerService.getCurrentRunner().getGoal());
    }
}
