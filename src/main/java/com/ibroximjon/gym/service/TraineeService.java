package com.ibroximjon.gym.service;

import com.ibroximjon.gym.model.Trainee;
import com.ibroximjon.gym.model.Trainer;
import com.ibroximjon.gym.repository.TraineeRepository;
import com.ibroximjon.gym.repository.TrainerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    public TraineeService(TraineeRepository traineeRepository, TrainerRepository trainerRepository) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    public Trainee registerTrainee(Trainee trainee) {

        String username = generateUsername(trainee.getFirstName(), trainee.getLastName());
        String password = generatePassword();

        trainee.setUsername(username);
        trainee.setPassword(password);
        trainee.setActive(true);

        return traineeRepository.save(trainee);
    }

    public Trainee updateTrainee(String username,Trainee trainee) {
        Trainee existing = getProfile(username);

        existing.setFirstName(trainee.getFirstName());
        existing.setLastName(trainee.getLastName());
        existing.setDateOfBirth(trainee.getDateOfBirth());
        existing.setActive(trainee.getActive());
        existing.setAddress(trainee.getAddress());

        return traineeRepository.save(existing);
    }

    @Transactional
    public void deleteTrainee(String username) {
        if (traineeRepository.existsByUsername(username)) {
            throw new NoSuchElementException("Trainee with username " + username + " does not exist");
        }
        traineeRepository.deleteByUsername(username);
    }

    public List<Trainer> getNotAssignedActiveTrainers(String username) {
        Trainee trainee = getProfile(username);
        Set<Integer> assignedIds = trainee.getTrainers().
                stream().map(Trainer::getId).
                collect(Collectors.toSet());

        return trainerRepository.findByIsActiveTrue().stream()
                .filter(trainer -> !assignedIds.contains(trainer.getId()))
                .collect(Collectors.toList());
    }

    public List<Trainer> updateTrainerList(String traineeUsername, List<String> trainerUsernames) {
        Trainee trainee = getProfile(traineeUsername);

        List<Trainer> trainers = trainerRepository.findAll().stream()
                .filter(trainer -> trainerUsernames.contains(trainer.getUsername()))
                .collect(Collectors.toList());

        trainee.setTrainers(trainers);
        return traineeRepository.save(trainee).getTrainers();
    }

    public void setActiveStatus(String username, boolean isActive) {
        Trainee trainee = getProfile(username);
        trainee.setActive(isActive);
        traineeRepository.save(trainee);
    }

    public Trainee updateProfile(String username, Trainee updated) {
        Trainee existing = getProfile(username);

        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setDateOfBirth(updated.getDateOfBirth());
        existing.setAddress(updated.getAddress());
        existing.setActive(updated.getActive());

        return traineeRepository.save(existing);
    }


    @Transactional
    public void deleteProfile(String username) {
        if (traineeRepository.existsByUsername(username)) {
            throw new NoSuchElementException("Trainee not found");
        }
        traineeRepository.deleteByUsername(username);
    }



    public Trainee getProfile(String username) {
        return traineeRepository.findByUsername(username).
                orElseThrow(()->new NoSuchElementException("Trainer not found"));
    }

    private String generateUsername(String firstName, String lastName) {
        return (firstName.charAt(0) + lastName + new Random().nextInt(1000)).toLowerCase();
    }

    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0,8);
    }
}
