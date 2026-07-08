package org.volodymyrzganiaiko.gym.crm.system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainingDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Training;
import org.volodymyrzganiaiko.gym.crm.system.dto.Credentials;
import org.volodymyrzganiaiko.gym.crm.system.service.AuthenticationService;
import org.volodymyrzganiaiko.gym.crm.system.service.TrainingService;

import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TrainingServiceImpl implements TrainingService {
    private TrainingDAO trainingDAO;
    private AuthenticationService authenticationService;
    private Validator validator;

    private static final Logger log = LoggerFactory.getLogger(TrainingServiceImpl.class);

    @Autowired
    public void setTrainingDAO(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    @Transactional
    public Training addTraining(Credentials credentials, Training training) {
        authenticationService.check(credentials);
        Set<ConstraintViolation<Training>> violations = validator.validate(training);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        training = trainingDAO.save(training);
        log.info("Creating the training record with id {}", training.getId());
        return training;
}

    @Override
    @Transactional(readOnly = true)
    public Optional<Training> findById(Long id) {
        log.debug("Finding the training record with id {}", id);
        return trainingDAO.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTraineeTrainings(Credentials credentials, LocalDate from, LocalDate to, String trainerUsername, String trainingTypeName) {
        authenticationService.check(credentials);
        log.debug("Finding the training records for trainee with username {}", credentials.username());
        return trainingDAO.findTraineeTrainings(credentials.username(), from, to, trainerUsername, trainingTypeName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainerTrainings(Credentials credentials, LocalDate from, LocalDate to, String traineeUsername) {
        authenticationService.check(credentials);
        log.debug("Finding the training records for trainer with username {}", credentials.username());
        return trainingDAO.findTrainerTrainings(credentials.username(), from, to, traineeUsername);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> findAll() {
        log.debug("Finding all training records");
        return trainingDAO.findAll();
    }
}
