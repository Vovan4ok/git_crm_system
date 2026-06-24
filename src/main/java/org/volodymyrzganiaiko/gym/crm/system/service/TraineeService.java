package org.volodymyrzganiaiko.gym.crm.system.service;

import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TraineeService {
    Trainee create(Trainee trainee);
    boolean update(Trainee trainee);
    boolean delete(UUID traineeId);
    Optional<Trainee> findById(UUID traineeId);
    List<Trainee> findAll();
}
