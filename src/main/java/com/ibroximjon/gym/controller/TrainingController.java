package com.ibroximjon.gym.controller;


import com.ibroximjon.gym.dto.TrainingRequestDto;
import com.ibroximjon.gym.model.Training;
import com.ibroximjon.gym.model.TrainingType;
import com.ibroximjon.gym.repository.TrainingTypeRepository;
import com.ibroximjon.gym.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trainings")
public class TrainingController {

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @PostMapping("/add")
    public void addTraining(@RequestBody TrainingRequestDto request) {
        Training training = new Training();
        training.setTrainingName(request.getTrainingName());
        training.setTrainingDate(request.getTrainingDate());
        training.setDuration(request.getDuration());

        TrainingType type = trainingTypeRepository.findById(request.getTrainingTypeId())
                .orElseThrow(() -> new RuntimeException("TrainingType not found"));

        training.setTrainingType(type);

        trainingService.addTraining(
                request.getTraineeUsername(),
                request.getTrainerUsername(),
                training
        );
    }
}