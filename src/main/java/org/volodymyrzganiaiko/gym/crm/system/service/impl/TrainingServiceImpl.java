package org.volodymyrzganiaiko.gym.crm.system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainingDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Training;
import org.volodymyrzganiaiko.gym.crm.system.service.AuthenticationService;
import org.volodymyrzganiaiko.gym.crm.system.service.TrainingService;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.volodymyrzganiaiko.gym.crm.system.utils.ValueValidator.requireNotBlank;
import static org.volodymyrzganiaiko.gym.crm.system.utils.ValueValidator.requireNotNull;

@Service
public class TrainingServiceImpl implements TrainingService {
    private TrainingDAO trainingDAO;
    private AuthenticationService authenticationService;

    private static final Logger log = LoggerFactory.getLogger(TrainingServiceImpl.class);

    @Autowired
    public void setTrainingDAO(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    @Transactional
    public Training addTraining(String username, String password, Training training) {
        authenticationService.check(username, password);
        requireNotNull(training.getTrainee(), "trainee");
        requireNotNull(training.getTrainee().getId(), "traineeId");
        requireNotNull(training.getTrainer(), "trainer");
        requireNotNull(training.getTrainer().getId(), "trainerId");
        requireNotNull(training.getTrainingType(), "trainingType");
        requireNotNull(training.getTrainingType().getId(), "trainingTypeId");
        requireNotBlank(training.getTrainingName(), "trainingName");
        requireNotNull(training.getTrainingDate(), "trainingDate");
        requireNotNull(training.getTrainingDurationInMinutes(), "trainingDurationInMinutes");
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
    public List<Training> getTraineeTrainings(String traineeUsername, String password, LocalDate from, LocalDate to, String trainerUsername, String trainingTypeName) {
        authenticationService.check(traineeUsername, password);
        log.debug("Finding the training records for trainee with username {}", traineeUsername);
        return trainingDAO.findTraineeTrainings(traineeUsername, from, to, trainerUsername, trainingTypeName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainerTrainings(String trainerUsername, String password, LocalDate from, LocalDate to, String traineeUsername) {
        authenticationService.check(trainerUsername, password);
        log.debug("Finding the training records for trainer with username {}", trainerUsername);
        return trainingDAO.findTrainerTrainings(trainerUsername, from, to, traineeUsername);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> findAll() {
        log.debug("Finding all training records");
        return trainingDAO.findAll();
    }
}
