package com.ibroximjon.gym.events;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TrainingEventRequest {
    private String trainerUsername;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private LocalDate trainingDate;
    private int trainingDuration;
    private String actionType; // "ADD" yoki "DELETE"
}
