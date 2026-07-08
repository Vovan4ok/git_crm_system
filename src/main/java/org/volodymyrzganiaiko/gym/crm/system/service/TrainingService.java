package org.volodymyrzganiaiko.gym.crm.system.service;

import org.volodymyrzganiaiko.gym.crm.system.domain.Training;
import org.volodymyrzganiaiko.gym.crm.system.dto.Credentials;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainingService {
    Training addTraining(Credentials credentials, Training training);
    Optional<Training> findById(Long id);
    List<Training> getTraineeTrainings(Credentials credentials, LocalDate from, LocalDate to, String trainerUsername, String trainingTypeName);
    List<Training> getTrainerTrainings(Credentials credentials, LocalDate from, LocalDate to, String traineeUsername);
    List<Training> findAll();
}
