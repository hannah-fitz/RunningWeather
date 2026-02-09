package com.paf.runningweather.config;

import com.paf.runningweather.runner.Runner;
import com.paf.runningweather.runner.RunnerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    private final RunnerService runnerService;
    public GlobalModelAttributes(RunnerService runnerService) {
        this.runnerService = runnerService;
    }

    @ModelAttribute("currentPath")
    public String currentPath(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("runner")
    public Runner addRunnerToModel() {
        return runnerService.getCurrentRunner();
    }

}
