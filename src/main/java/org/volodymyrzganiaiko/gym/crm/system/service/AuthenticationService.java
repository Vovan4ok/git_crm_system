package org.volodymyrzganiaiko.gym.crm.system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.volodymyrzganiaiko.gym.crm.system.dao.TraineeDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.exceptions.AuthenticationException;
import org.volodymyrzganiaiko.gym.crm.system.service.impl.TraineeServiceImpl;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthenticationService {
    private TraineeDAO traineeDAO;

    private TrainerDAO trainerDAO;

    private static final Logger log =  LoggerFactory.getLogger(AuthenticationService.class);
    @Autowired
    public void setTraineeDAO(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Transactional(readOnly = true)
    public void check(String username, String password) {
        if(!matches(username, password)) {
            log.warn("The authentication for user with username {} wasn't successful", username);
            throw new AuthenticationException("Wrong username or password");
        }
    }

    private boolean matches(String username, String password) {
        Optional<Trainee> foundTraineeOpt = traineeDAO.findByUsername(username);
        if (foundTraineeOpt.isPresent()) {
            Trainee foundTrainee = foundTraineeOpt.get();
            return Objects.equals(foundTrainee.getUser().getPassword(), password);
        }
        Optional<Trainer> foundTrainerOpt = trainerDAO.findByUsername(username);
        if (foundTrainerOpt.isPresent()) {
            Trainer foundTrainer = foundTrainerOpt.get();
            return Objects.equals(foundTrainer.getUser().getPassword(), password);
        }
        return false;
    }
}
