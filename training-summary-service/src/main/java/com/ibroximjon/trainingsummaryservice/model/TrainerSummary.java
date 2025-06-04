package com.ibroximjon.trainingsummaryservice.model;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TrainerSummary {
    private String trainerUsername;
    private String firstName;
    private String lastName;
    private boolean status;
    private List<YearlySummary> years = new ArrayList<>();

    public YearlySummary getOrCreateYear(int year) {
        return years.stream()
                .filter(y -> y.getYear() == year)
                .findFirst()
                .orElseGet(() -> {
                    YearlySummary newYear = new YearlySummary(year);
                    years.add(newYear);
                    return newYear;
                });
    }
}
