package com.ibroximjon.mongo.repository;

import com.ibroximjon.mongo.model.TrainerSummary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TrainerSummaryRepository extends MongoRepository<TrainerSummary, String> {

    Optional<TrainerSummary> findByTrainerUsername(String username);

    List<TrainerSummary> findByFirstNameAndLastName(String firstName, String lastName);
}
