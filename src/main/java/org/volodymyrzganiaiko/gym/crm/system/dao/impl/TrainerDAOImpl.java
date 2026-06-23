package org.volodymyrzganiaiko.gym.crm.system.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TrainerDAOImpl implements TrainerDAO {
    private Map<UUID, Trainer> storage;

    @Autowired
    @Qualifier("trainerStorage")
    public void setStorage(Map<UUID, Trainer> storage) {
        this.storage = storage;
    }

    @Override
    public boolean save(Trainer trainer) {
        storage.put(trainer.getUserId(), trainer);
        return true;
    }

    @Override
    public boolean update(Trainer trainer) {
        storage.put(trainer.getUserId(), trainer);
        return true;
    }

    @Override
    public Optional<Trainer> findById(UUID trainerId) {
        return Optional.ofNullable(storage.get(trainerId));
    }

    @Override
    public List<Trainer> findAll() {
        return storage.values().stream().toList();
    }
}
