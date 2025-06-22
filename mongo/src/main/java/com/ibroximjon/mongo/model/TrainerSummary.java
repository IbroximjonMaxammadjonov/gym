package com.ibroximjon.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "trainer_summary")
@CompoundIndex(name = "name_idx", def = "{'firstName': 1, 'lastName': 1}")
public class TrainerSummary {

    @Id
    private String id;

    private String trainerUsername;
    private String firstName;
    private String lastName;
    private boolean isActive;

    // year → month → duration
    private Map<Integer, Map<Integer, Integer>> years = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Map<Integer, Map<Integer, Integer>> getYears() {
        return years;
    }

    public void setYears(Map<Integer, Map<Integer, Integer>> years) {
        this.years = years;
    }
}
