package com.ibroximjon.trainingsummaryservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlySummary {
    private int month; // 1-12
    private int trainingDurationSummary;
}
