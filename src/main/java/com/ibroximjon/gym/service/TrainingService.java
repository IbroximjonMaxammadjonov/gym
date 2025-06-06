package com.ibroximjon.gym.service;

import com.ibroximjon.gym.dto.TrainingRequestDto;
import com.ibroximjon.gym.events.TrainingEventRequest;
import com.ibroximjon.gym.model.Trainee;
import com.ibroximjon.gym.model.Trainer;
import com.ibroximjon.gym.model.Training;
import com.ibroximjon.gym.model.TrainingType;
import com.ibroximjon.gym.repository.TraineeRepository;
import com.ibroximjon.gym.repository.TrainerRepository;
import com.ibroximjon.gym.repository.TrainingRepository;
import com.ibroximjon.gym.repository.TrainingTypeRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TrainingService {

    private final TrainingRepository trainingRepository;

    private final TraineeRepository traineeRepository;

    private final TrainerRepository trainerRepository;

    private final TrainingTypeRepository trainingTypeRepository;
    private final RestTemplate restTemplate;

    public TrainingService(TrainingRepository trainingRepository, TraineeRepository traineeRepository,
                           TrainingTypeRepository trainingTypeRepository, TrainerRepository trainerRepository
    , RestTemplate restTemplate) {
        this.trainingRepository = trainingRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.restTemplate = restTemplate;
    }

    public void addTraining(String trainerUsername, String traineeUsername, Training training) {
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
        trainingRepository.save(training);

        TrainingEventRequest dto = new TrainingEventRequest();
        dto.setTrainerUsername(trainerUsername);
        dto.setFirstName(training.getTrainers().get(0).getFirstName());
        dto.setLastName(training.getTrainers().get(0).getLastName());
        dto.setActive(training.getTrainers().get(0).getActive());
        dto.setTrainingDate(training.getTrainingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        dto.setTrainingDuration(training.getDuration());
        dto.setActionType("ADD");

        // 4. Token olish (masalan frontenddan controller orqali uzatilgan bo‘lishi kerak)
        String jwtToken = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();

        // 5. So‘rov yuborish
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);

        HttpEntity<TrainingEventRequest> request = new HttpEntity<>(dto, headers);

        restTemplate.postForEntity(
                "http://localhost:8082/api/trainings/event",
                request,
                Void.class
        );

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
