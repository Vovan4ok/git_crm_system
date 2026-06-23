package org.volodymyrzganiaiko.gym.crm.system.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.volodymyrzganiaiko.gym.crm.system.dao.TraineeDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TraineeDAOImpl implements TraineeDAO {
    private Map<UUID, Trainee> storage;

    @Autowired
    @Qualifier("traineeStorage")
    public void setStorage(Map<UUID, Trainee> storage) {
        this.storage = storage;
    }


    @Override
    public boolean save(Trainee trainee) {
        storage.put(trainee.getUserId(), trainee);
        return true;
    }

    @Override
    public boolean update(Trainee trainee) {
        storage.put(trainee.getUserId(), trainee);
        return true;
    }

    @Override
    public boolean delete(UUID traineeId) {
        return storage.remove(traineeId) != null;
    }

    @Override
    public Optional<Trainee> findById(UUID traineeId) {
        return Optional.ofNullable(storage.get(traineeId));
    }

    @Override
    public List<Trainee> findAll() {
        return storage.values().stream().toList();
    }
}
