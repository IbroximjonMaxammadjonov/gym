package com.ibroximjon.trainingsummaryservice;

import com.ibroximjon.mongo.service.TrainerSummaryService;
import com.ibroximjon.trainingsummaryservice.dto.TrainingEventRequest;
import com.ibroximjon.trainingsummaryservice.model.TrainerSummary;
import com.ibroximjon.trainingsummaryservice.model.YearlySummary;
import com.ibroximjon.trainingsummaryservice.repository.InMemoryTrainerSummaryRepository;
import edu.umd.cs.findbugs.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SummaryStepsTest {

    @Autowired
    private InMemoryTrainerSummaryRepository repository;

    @Autowired
    private TrainerSummaryService service;

    private TrainingEventRequest request;
    private final String txId = UUID.randomUUID().toString();

    @Given("trainer {string} has no summary")
    public void trainer_has_no_summary(String username) {
        repository.deleteByTrainerUsername(username);
    }

    @Given("trainer {string} has summary with {int} hours in June 2025")
    public void trainer_has_summary(String username, int hours) {
        TrainerSummary s = new TrainerSummary();
        s.setTrainerUsername(username);
        s.setFirstName("Test");
        s.setLastName("User");
        s.setStatus(true);
        Map<Integer, Integer> june = new HashMap<>();
        june.put(6, hours);
        Map<Integer, Map<Integer, Integer>> years = new HashMap<>();
        years.put(2025, june);
        s.setYears((List<YearlySummary>) years);
        repository.save(s);
    }

    @When("a training event with {int} hours in June 2025 is received")
    public void training_event_received(int hours) {
        request = new TrainingEventRequest();
        request.setTrainerUsername("trainer1");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setActive(true);
        request.setTrainingDate(LocalDate.of(2025, 6, 1));
        request.setTrainingDuration(hours);
        request.setActionType("ADD");

        service.processTrainingEvent(request, txId);
    }

    @Then("trainer {string} should have {int} hours for June 2025")
    public void check_duration(String username, int expected) {
        TrainerSummary s = (TrainerSummary) repository.findByTrainerUsername(username).orElseThrow();
        int actual = s.getYears().get(2025).get(6);
        assertEquals(expected, actual);
    }
}
