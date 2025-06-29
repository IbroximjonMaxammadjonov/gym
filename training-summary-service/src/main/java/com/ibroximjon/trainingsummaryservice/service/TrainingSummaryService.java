package com.ibroximjon.trainingsummaryservice.service;



import com.ibroximjon.trainingsummaryservice.dto.TrainingEventRequest;
import com.ibroximjon.trainingsummaryservice.model.MonthlySummary;
import com.ibroximjon.trainingsummaryservice.model.TrainerSummary;
import com.ibroximjon.trainingsummaryservice.model.YearlySummary;
import com.ibroximjon.trainingsummaryservice.repository.InMemoryTrainerSummaryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TrainingSummaryService {

    private final InMemoryTrainerSummaryRepository repository;

    public TrainingSummaryService(InMemoryTrainerSummaryRepository repository) {
        this.repository = repository;
    }

    public void processTrainingEvent(TrainingEventRequest request) {
        TrainerSummary trainer = repository.getOrCreate(request.getTrainerUsername());

        trainer.setTrainerUsername(request.getTrainerUsername());
        trainer.setFirstName(request.getFirstName());
        trainer.setLastName(request.getLastName());
        trainer.setStatus(request.isActive());

        LocalDate date = request.getTrainingDate();
        YearlySummary year = trainer.getOrCreateYear(date.getYear());
        MonthlySummary month = year.getMonth(date.getMonthValue());

        if ("ADD".equalsIgnoreCase(request.getActionType())) {
            month.setTrainingDurationSummary(month.getTrainingDurationSummary() + request.getTrainingDuration());
        } else if ("DELETE".equalsIgnoreCase(request.getActionType())) {
            month.setTrainingDurationSummary(Math.max(0, month.getTrainingDurationSummary() - request.getTrainingDuration()));
        }

        repository.save(trainer);
    }

    public TrainerSummary getTrainerSummary(String username) {
        return repository.find(username).orElse(null);
    }
}
