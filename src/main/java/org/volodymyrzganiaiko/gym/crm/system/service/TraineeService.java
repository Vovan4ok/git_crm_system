package org.volodymyrzganiaiko.gym.crm.system.service;

import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.dto.Credentials;
import org.volodymyrzganiaiko.gym.crm.system.dto.TraineeRegistrationDTO;

import java.util.List;
import java.util.Optional;

public interface TraineeService {
    TraineeRegistrationDTO create(Trainee trainee);
    Optional<Trainee> findById(Long id);
    Optional<Trainee> findByUsername(Credentials credentials);
    List<Trainee> findAll();
    void changePassword(Credentials credentials, String newPassword);
    Trainee update(Credentials credentials, Trainee trainee);
    void activate(Credentials credentials);
    void deactivate(Credentials credentials);
    boolean deleteByUsername(Credentials credentials);
    List<Trainer> updateTrainerList(Credentials credentials, List<String> trainerUsernames);
}
