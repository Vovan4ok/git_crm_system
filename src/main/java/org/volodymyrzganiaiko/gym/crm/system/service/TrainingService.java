package org.volodymyrzganiaiko.gym.crm.system.service;

import org.volodymyrzganiaiko.gym.crm.system.domain.Training;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrainingService {
    Training create(Training training);
    Optional<Training> findById(UUID trainingId);
    List<Training> findAll();
}
