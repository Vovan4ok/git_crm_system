package org.volodymyrzganiaiko.gym.crm.system.service;

import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
    Trainer create(Trainer trainer);
    Optional<Trainer> findById(Long id);
    Optional<Trainer> findByUsername(String username, String password);
    void changePassword(String username, String oldPassword, String newPassword);
    Trainer update(String username, String password, Trainer trainer);
    void activate(String username, String password);
    void deactivate(String username, String password);
    List<Trainer> getUnassignedTrainers(String traineeUsername, String password);
    List<Trainer> findAll();
}
