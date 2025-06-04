package com.ibroximjon.trainingsummaryservice.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class YearlySummary {
    private int year;
    private List<MonthlySummary> months = new ArrayList<>();

    public YearlySummary(int year) {
        this.year = year;
        for (int i = 1; i <= 12; i++) {
            months.add(new MonthlySummary(i, 0));
        }
    }

    public MonthlySummary getMonth(int monthNumber) {
        return months.get(monthNumber - 1);
    }
}
