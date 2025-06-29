package com.ibroximjon.trainingsummaryservice.integration;


import com.ibroximjon.gym.dto.TrainingEventRequest;
import com.ibroximjon.gym.messaging.TrainingSummaryProducer;
import com.ibroximjon.summary.model.TrainerSummary;
import com.ibroximjon.summary.repository.TrainerSummaryRepository;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IntegrationStepDefs {

    @Autowired
    private TrainingSummaryProducer producer; // training-management-service component

    @Autowired
    private TrainerSummaryRepository repository; // training-summary-service component

    private final String txId = UUID.randomUUID().toString();

    @Given("training summary does not exist for {string}")
    public void training_summary_does_not_exist_for(String username) {
        repository.findByTrainerUsername(username).ifPresent(r -> repository.delete(r));
    }

    @When("training-management-service sends training event for {string}")
    public void training_management_service_sends_training_event_for(String username) {
        TrainingEventRequest event = new TrainingEventRequest();
        event.setTrainerUsername(username);
        event.setFirstName("Integration");
        event.setLastName("Test");
        event.setIsActive(true);
        event.setTrainingDate(LocalDate.of(2025, 6, 1));
        event.setTrainingDuration(3);
        event.setActionType("ADD");

        producer.sendTrainingEvent(event);
    }

    @Then("training-summary-service should store {int} hours for {string} in June 2025")
    public void training_summary_service_should_store_hours(int hours, String username) throws InterruptedException {
        // Wait 1-2 sec for async delivery
        Thread.sleep(1500);

        TrainerSummary summary = repository.findByTrainerUsername(username)
                .orElseThrow(() -> new AssertionError("Trainer summary not found"));

        int actual = summary.getYears().get(2025).get(6);
        assertEquals(hours, actual);
    }
}
