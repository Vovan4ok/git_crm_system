package org.volodymyrzganiaiko.gym.crm.system.service;

import org.volodymyrzganiaiko.gym.crm.system.domain.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainingService {
    Training addTraining(String username, String password, Training training);
    Optional<Training> findById(Long id);
    List<Training> getTraineeTrainings(String traineeUsername, String password, LocalDate from, LocalDate to, String trainerUsername, String trainingTypeName);
    List<Training> getTrainerTrainings(String trainerUsername, String password, LocalDate from, LocalDate to, String traineeUsername);
    List<Training> findAll();
}
