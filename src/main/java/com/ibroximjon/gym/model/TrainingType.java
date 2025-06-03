package com.ibroximjon.gym.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "training_type")
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String typeName;

    @OneToMany(mappedBy ="trainingType")
    private List<Training> trainings;

    public TrainingType() {}
    public TrainingType(int id, String typeName, List<Training> trainings) {
        this.id = id;
        this.typeName = typeName;
        this.trainings = trainings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }
}
