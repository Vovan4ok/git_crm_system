package org.volodymyrzganiaiko.gym.crm.system.service;

import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.dto.Credentials;
import org.volodymyrzganiaiko.gym.crm.system.dto.TrainerRegistrationDTO;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
    TrainerRegistrationDTO create(Trainer trainer);
    Optional<Trainer> findById(Long id);
    Optional<Trainer> findByUsername(Credentials credentials);
    void changePassword(Credentials credentials, String newPassword);
    Trainer update(Credentials credentials, Trainer trainer);
    void activate(Credentials credentials);
    void deactivate(Credentials credentials);
    List<Trainer> getUnassignedTrainers(Credentials credentials);
    List<Trainer> findAll();
}
