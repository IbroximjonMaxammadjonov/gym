package com.ibroximjon.gym.controller;

import com.ibroximjon.gym.model.Trainee;
import com.ibroximjon.gym.model.Trainer;
import com.ibroximjon.gym.model.Training;
import com.ibroximjon.gym.service.TrainerService;
import com.ibroximjon.gym.service.TrainingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public TrainerController(TrainerService trainerService, TrainingService trainingService) {
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    @PostMapping("/register")
    public Trainer register(@RequestBody Trainer trainer) {
        return trainerService.registerTrainer(trainer);
    }

    @GetMapping("/{username}")
    public Trainer getProfile(@PathVariable String username) {
        return trainerService.getProfile(username);
    }

    @PutMapping("/{username}")
    public Trainer updateProfile(@PathVariable String username, @RequestBody Trainer trainer) {
        return trainerService.updateTrainer(username, trainer);
    }

    @PatchMapping("/{username}/status")
    public void changeStatus(@PathVariable String username, @RequestParam boolean isActive) {
        trainerService.setActiveStatus(username, isActive);
    }

    @GetMapping("/{username}/trainees")
      public List<Trainee> getTrainees(@PathVariable String username) {
        return trainerService.getTrainees(username);
    }

    @GetMapping("/{username}/trainings")
    public List<Training> getTrainings(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
            @RequestParam(required = false) String traineeName
    ) {
        return trainingService.getTrainerTrainings(username, from, to, traineeName);
    }
}
