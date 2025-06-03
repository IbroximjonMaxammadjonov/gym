package com.ibroximjon.gym.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "trainer")
public class Trainer extends User {

    @ManyToOne
    @JoinColumn(name = "training_type_id")
    private TrainingType trainingType;

    @ManyToMany(mappedBy = "trainers")
    private List<Trainee> trainees;

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public List<Trainee> getTrainees() {
        return trainees;
    }

    public void setTrainees(List<Trainee> trainees) {
        this.trainees = trainees;
    }
}
