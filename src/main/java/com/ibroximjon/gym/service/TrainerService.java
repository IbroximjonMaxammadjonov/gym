package com.ibroximjon.gym.service;

import com.ibroximjon.gym.model.Trainee;
import com.ibroximjon.gym.model.Trainer;
import com.ibroximjon.gym.repository.TrainerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.UUID;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;

    TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;

    }

    public Trainer registerTrainer(Trainer trainer) {
        String username = generateUsername(trainer.getFirstName(), trainer.getLastName());
        String password =  generatePassword();

        trainer.setPassword(password);
        trainer.setUsername(username);
        trainer.setActive(true);

        return trainerRepository.save(trainer);
    }

    public Trainer updateTrainer(String username, Trainer trainer) {
        Trainer existing = getProfile(username);
        existing.setFirstName(trainer.getFirstName());
        existing.setLastName(trainer.getLastName());
        existing.setActive(trainer.getActive());

        return trainerRepository.save(existing);
    }

    public void setActiveStatus(String username, boolean isActive) {
        Trainer trainer = getProfile(username);
        trainer.setActive(isActive);
        trainerRepository.save(trainer);
    }

    public List<Trainee> getAllTrainers(String username) {
        Trainer trainer = getProfile(username);
        return trainer.getTrainees();
    }

    public Trainer getProfile(String username) {
        return trainerRepository.findByUsername(username).
                orElseThrow(()->new NoSuchElementException("Trainer not found"));
    }

    private String generateUsername(String firstName, String lastName) {
        return (firstName.charAt(0) + lastName + new Random().nextInt(1000)).toLowerCase();
    }

    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0,8);
    }


    public List<Trainee> getTrainees(String trainerUsername) {
        Trainer trainer = getProfile(trainerUsername);
        return trainer.getTrainees();
    }
}
