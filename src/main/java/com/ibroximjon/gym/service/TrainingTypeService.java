package com.ibroximjon.gym.service;

import com.ibroximjon.gym.model.TrainingType;
import com.ibroximjon.gym.repository.TrainingTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;
    public TrainingTypeService(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    public List<TrainingType> getAllTypes() {
        return trainingTypeRepository.findAll();
    }
}
