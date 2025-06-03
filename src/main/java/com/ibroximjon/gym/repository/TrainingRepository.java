package com.ibroximjon.gym.repository;

import com.ibroximjon.gym.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    List<Training> findByTrainees_Username(String username);

    List<Training> findByTrainers_Username(java.lang.String username);
}
