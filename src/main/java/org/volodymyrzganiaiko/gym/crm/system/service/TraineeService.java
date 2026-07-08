package org.volodymyrzganiaiko.gym.crm.system.service;

import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.dto.TraineeRegistrationDTO;

import java.util.List;
import java.util.Optional;

public interface TraineeService {
    TraineeRegistrationDTO create(Trainee trainee);
    Optional<Trainee> findById(Long id);
    Optional<Trainee> findByUsername(String username, String password);
    List<Trainee> findAll();
    void changePassword(String username, String oldPassword, String newPassword);
    Trainee update(String username, String password, Trainee trainee);
    void activate(String username, String password);
    void deactivate(String username, String password);
    boolean deleteByUsername(String username, String password);
    List<Trainer> updateTrainerList(String username, String password, List<String> trainerUsernames);
}
