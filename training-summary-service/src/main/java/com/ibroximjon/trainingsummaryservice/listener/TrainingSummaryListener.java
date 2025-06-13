package com.ibroximjon.trainingsummaryservice.listener;


import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@Component
public class TrainingSummaryListener {

    private final Map<String, Map<YearMonth, Integer>> summaryMap = new HashMap<>();


    @JmsListener(destination = "training-summary-queue")
    public void receive(TrainingEventRequest event) {
        System.out.println("Received training event: " + event.getTrainerUsername());

        String username = event.getTrainerUsername();
        LocalDate date = event.getTrainingDate();
        YearMonth yearMonth = YearMonth.from(date);
        int hours = event.getTrainingDuration();

        Map<YearMonth,Integer> trainerStats = summaryMap.getOrDefault(username, new HashMap<>());

        int updatedHours = trainerStats.getOrDefault(yearMonth, 0)+hours;
        trainerStats.put(yearMonth, updatedHours);
        summaryMap.put(username, trainerStats);
        System.out.println("Updated trainer stats: " + trainerStats);
    }


    //for testing
    public Map<String, Map<YearMonth, Integer>> getSummaryMap() {
        return summaryMap;
    }
}
