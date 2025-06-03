package com.ibroximjon.gym.controller;

import com.ibroximjon.gym.model.Trainee;
import com.ibroximjon.gym.model.Trainer;
import com.ibroximjon.gym.model.Training;
import com.ibroximjon.gym.service.TraineeService;
import com.ibroximjon.gym.service.TrainingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/trainees")
public class TraineeController {

    private final TraineeService traineeService;
    private final  TrainingService trainingService;

    public TraineeController(TraineeService traineeService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    @PostMapping("/register")
    public Trainee register(@RequestBody Trainee trainee) {
        return traineeService.registerTrainee(trainee);
    }

    @GetMapping("/{username}")
    public Trainee getProfile(@PathVariable String username) {
        return traineeService.getProfile(username);
    }

    @PutMapping("/{username}")
    public Trainee updateProfile(@PathVariable String username, @RequestBody Trainee updated) {
        return traineeService.updateProfile(username, updated);
    }

    @DeleteMapping("/{username}")
    public void delete(@PathVariable String username) {
        traineeService.deleteProfile(username);
    }

    @PatchMapping("/{username}/status")
    public void changeStatus(@PathVariable String username, @RequestParam boolean isActive) {
        traineeService.setActiveStatus(username, isActive);
    }

    @GetMapping("/{username}/trainers/unassigned")
    public List<Trainer> getUnassigned(@PathVariable String username) {
        return traineeService.getNotAssignedActiveTrainers(username);
    }

    @PutMapping("/{username}/trainers")
    public List<Trainer> updateTrainers(@PathVariable String username, @RequestBody List<String> trainerUsernames) {
        return traineeService.updateTrainerList(username, trainerUsernames);
    }

    @GetMapping("/{username}/trainings")
    public List<Training> getTrainings(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) String trainingType
    ) {
        return trainingService.getTraineeTrainings(username, from, to, trainerName, trainingType);
    }
}
