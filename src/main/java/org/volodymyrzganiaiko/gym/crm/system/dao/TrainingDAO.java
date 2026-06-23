package org.volodymyrzganiaiko.gym.crm.system.dao;

import org.volodymyrzganiaiko.gym.crm.system.domain.Training;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrainingDAO {
    boolean save(Training training);
    Optional<Training> findById(UUID trainingId);
    List<Training> findAll();
}
