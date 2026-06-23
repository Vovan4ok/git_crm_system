package org.volodymyrzganiaiko.gym.crm.system.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainingDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Training;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TrainingDAOImpl implements TrainingDAO {
    private Map<UUID, Training> storage;

    @Autowired
    @Qualifier("trainingStorage")
    public void setStorage(Map<UUID, Training> storage) {
        this.storage = storage;
    }

    @Override
    public Training save(Training training) {
        storage.put(training.getTrainingId(), training);
        return training;
    }

    @Override
    public Optional<Training> findById(UUID trainingId) {
        return Optional.ofNullable(storage.get(trainingId));
    }

    @Override
    public List<Training> findAll() {
        return storage.values().stream().toList();
    }
}
