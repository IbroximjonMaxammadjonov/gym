package com.ibroximjon.trainingsummaryservice.controller;
import com.ibroximjon.trainingsummaryservice.dto.TrainingEventRequest;
import com.ibroximjon.trainingsummaryservice.model.TrainerSummary;
import com.ibroximjon.trainingsummaryservice.service.TrainingSummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trainings")
public class TrainingSummaryController {

    private final TrainingSummaryService service;

    public TrainingSummaryController(TrainingSummaryService service) {
        this.service = service;
    }

    @PostMapping("/event")
    public ResponseEntity<?> receiveEvent(@RequestBody TrainingEventRequest request) {
        service.processTrainingEvent(request);
        return ResponseEntity.ok("Processed");
    }

    @GetMapping("/summary/{username}")
    public ResponseEntity<TrainerSummary> getSummary(@PathVariable String username) {
        TrainerSummary summary = service.getTrainerSummary(username);
        if (summary == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(summary);
    }
}
