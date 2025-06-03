package com.ibroximjon.gym.controller;




import com.ibroximjon.gym.model.TrainingType;
import com.ibroximjon.gym.service.TrainingTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-types")
public class TrainingTypeController {


    private final TrainingTypeService trainingTypeService;

    public TrainingTypeController(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @GetMapping
    public List<TrainingType> getAllTrainingTypes() {
        return trainingTypeService.getAllTypes();
    }
}