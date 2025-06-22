package com.ibroximjon.mongo;

import com.ibroximjon.mongo.dto.TrainingEventRequest;
import com.ibroximjon.mongo.model.TrainerSummary;
import com.ibroximjon.mongo.repository.TrainerSummaryRepository;
import com.ibroximjon.mongo.service.TrainerSummaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TrainerSummaryServiceTest {

    @Autowired
    private TrainerSummaryService service;

    @Autowired
    private TrainerSummaryRepository repository;

    @Test
    void testNewTrainerEvent() {
        TrainingEventRequest event = new TrainingEventRequest();
        event.setTrainerUsername("trainer1");
        event.setFirstName("Ali");
        event.setLastName("Valiyev");
        event.setActive(true);
        event.setTrainingDate(LocalDate.of(2025, 6, 4));
        event.setTrainingDuration(3);
        event.setActionType("ADD");

        service.processTrainingEvent(event, UUID.randomUUID().toString());

        Optional<TrainerSummary> opt = repository.findByTrainerUsername("trainer1");
        assertTrue(opt.isPresent());
        assertEquals(3, opt.get().getYears().get(2025).get(6));
    }
}

