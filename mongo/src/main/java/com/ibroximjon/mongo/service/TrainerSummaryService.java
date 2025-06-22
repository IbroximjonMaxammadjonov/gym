package com.ibroximjon.mongo.service;

import ch.qos.logback.classic.Logger;
import com.ibroximjon.mongo.dto.TrainingEventRequest;
import com.ibroximjon.mongo.model.TrainerSummary;
import com.ibroximjon.mongo.repository.TrainerSummaryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class TrainerSummaryService {

    private final TrainerSummaryRepository repository;

    public TrainerSummaryService(TrainerSummaryRepository repository) {
        this.repository = repository;
    }
    Logger log;

    public void processTrainingEvent(TrainingEventRequest request, String transactionId) {

        log.info("[{}] Processing training event for {}", transactionId, request.getTrainerUsername());

        TrainerSummary summary = repository.findByTrainerUsername(request.getTrainerUsername())
                .orElseGet(() -> {
                    TrainerSummary newSummary = new TrainerSummary();
                    newSummary.setTrainerUsername(request.getTrainerUsername());
                    newSummary.setFirstName(request.getFirstName());
                    newSummary.setLastName(request.getLastName());
                    newSummary.setActive(request.isActive());
                    return newSummary;
                });

        LocalDate date = request.getTrainingDate();
        int year = date.getYear();
        int month = date.getMonthValue();
        int duration = request.getTrainingDuration();

        Map<Integer, Map<Integer, Integer>> yearsMap = summary.getYears();
        Map<Integer, Integer> months = yearsMap.getOrDefault(year, new HashMap<>());
        int currentDuration = months.getOrDefault(month, 0);
        months.put(month, currentDuration + duration);
        yearsMap.put(year, months);

        summary.setYears(yearsMap);
        repository.save(summary);

        log.info("[{}] Updated summary saved for {}", transactionId, summary.getTrainerUsername());
    }
}
