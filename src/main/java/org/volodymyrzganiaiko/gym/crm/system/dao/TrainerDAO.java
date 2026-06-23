package org.volodymyrzganiaiko.gym.crm.system.dao;

import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrainerDAO {
    Trainer save(Trainer trainer);
    boolean update(Trainer trainer);
    Optional<Trainer> findById(UUID trainerId);
    List<Trainer> findAll();
}
