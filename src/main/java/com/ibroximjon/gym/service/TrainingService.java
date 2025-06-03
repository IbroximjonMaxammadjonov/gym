package com.ibroximjon.gym.service;

import com.ibroximjon.gym.model.Trainee;
import com.ibroximjon.gym.model.Trainer;
import com.ibroximjon.gym.model.Training;
import com.ibroximjon.gym.model.TrainingType;
import com.ibroximjon.gym.repository.TraineeRepository;
import com.ibroximjon.gym.repository.TrainerRepository;
import com.ibroximjon.gym.repository.TrainingRepository;
import com.ibroximjon.gym.repository.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TrainingService {

    private TrainingRepository trainingRepository;

    private TraineeRepository traineeRepository;

    private TrainerRepository trainerRepository;

    private TrainingTypeRepository trainingTypeRepository;

    public TrainingService(TrainingRepository trainingRepository, TraineeRepository traineeRepository, TrainingTypeRepository trainingTypeRepository, TrainerRepository trainerRepository) {
        this.trainingRepository = trainingRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    public Training addTraining(String trainerUsername, String traineeUsername, Training training) {
        Trainee trainee = traineeRepository.findByUsername(traineeUsername).orElseThrow(()->new NoSuchElementException("trainee not found"));

        Trainer trainer = trainerRepository.findByUsername(trainerUsername)
                .orElseThrow(() -> new NoSuchElementException("Trainer not found"));

        training.setTrainees(List.of(trainee));
        training.setTrainers(List.of(trainer));

        TrainingType type = training.getTrainingType();
        if (type != null) {
            TrainingType validType = trainingTypeRepository.findById(type.getId()).orElseThrow(()-> new NoSuchElementException("Training Type Not Found"));
            training.setTrainingType(validType);
        }
        return trainingRepository.save(training);
    }

    public List<Training> getTraineeTrainings(String username, Date from, Date to, String trainerName, String trainingTypeName) {
        List<Training> trainings = trainingRepository.findByTrainees_Username(username);

        return trainings.stream()
                .filter(t -> from == null || !t.getTrainingDate().before(from))
                .filter(t -> to == null || !t.getTrainingDate().after(to))
                .filter(t -> trainerName == null || t.getTrainers().stream()
                        .anyMatch(tr -> (tr.getFirstName() + " " + tr.getLastName()).toLowerCase().contains(trainerName.toLowerCase())))
                .filter(t -> trainingTypeName == null || (
                        t.getTrainingType() != null &&
                                t.getTrainingType().getTypeName().equalsIgnoreCase(trainingTypeName)))
                .collect(Collectors.toList());
    }

    public List<Training> getTrainerTrainings(String username, Date from, Date to, String traineeName) {
        List<Training> trainings = trainingRepository.findByTrainers_Username(username);

        return trainings.stream()
                .filter(t -> from == null || !t.getTrainingDate().before(from))
                .filter(t -> to == null || !t.getTrainingDate().after(to))
                .filter(t -> traineeName == null || t.getTrainees().stream()
                        .anyMatch(tr -> (tr.getFirstName() + " " + tr.getLastName()).toLowerCase().contains(traineeName.toLowerCase())))
                .collect(Collectors.toList());
    }
}
