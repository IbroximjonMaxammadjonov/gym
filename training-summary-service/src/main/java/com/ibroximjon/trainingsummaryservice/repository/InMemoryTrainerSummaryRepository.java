package com.ibroximjon.trainingsummaryservice.repository;
import com.ibroximjon.trainingsummaryservice.model.TrainerSummary;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryTrainerSummaryRepository {
    private final Map<String, TrainerSummary> data = new HashMap<>();

    public TrainerSummary getOrCreate(String username) {
        return data.computeIfAbsent(username, k -> new TrainerSummary());
    }

    public void save(TrainerSummary summary) {
        data.put(username, summary);
    }

    public Optional<TrainerSummary> find(String username) {
        return Optional.ofNullable(data.get(username));
    }

    public void deleteByTrainerUsername(String username) {
    }

    public Optional<Object> findByTrainerUsername(String username) {
    }
}
