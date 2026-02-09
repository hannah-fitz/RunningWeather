package com.paf.runningweather.fatigueFactor;

import java.time.LocalDate;
import java.util.List;

public record DayView(LocalDate date, List<FatigueFactor> fatigueFactors) {

    public boolean isEmpty() {
        return fatigueFactors.isEmpty();
    }
}