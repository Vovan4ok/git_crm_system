package org.volodymyrzganiaiko.gym.crm.system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.volodymyrzganiaiko.gym.crm.system.dao.TraineeDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.Training;
import org.volodymyrzganiaiko.gym.crm.system.dto.Credentials;
import org.volodymyrzganiaiko.gym.crm.system.dto.TraineeRegistrationDTO;
import org.volodymyrzganiaiko.gym.crm.system.service.AuthenticationService;
import org.volodymyrzganiaiko.gym.crm.system.service.CredentialsService;
import org.volodymyrzganiaiko.gym.crm.system.service.TraineeService;

import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.volodymyrzganiaiko.gym.crm.system.utils.ValueValidator.requireNotBlank;

@Service
public class TraineeServiceImpl implements TraineeService {
    private TraineeDAO traineeDAO;
    private TrainerDAO trainerDAO;
    private CredentialsService credentialsService;
    private AuthenticationService authenticationService;
    private PasswordEncoder passwordEncoder;
    private Validator validator;

    private static final Logger log =  LoggerFactory.getLogger(TraineeServiceImpl.class);

    @Autowired
    public void setTraineeDAO(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Autowired
    public void setCredentialsService(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    @Transactional
    public TraineeRegistrationDTO create(Trainee trainee) {
        String password = credentialsService.assignCredentials(trainee);
        Set<ConstraintViolation<Trainee>> violations = validator.validate(trainee);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        trainee = traineeDAO.save(trainee);
        log.info("Creating a trainee record with id {}", trainee.getId());
        return new TraineeRegistrationDTO(trainee, password);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainee> findById(Long id) {
        log.debug("Finding the trainee record with id {}", id);
        return traineeDAO.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainee> findByUsername(Credentials credentials) {
        authenticationService.check(credentials);
        log.debug("Finding the trainee with username {}", credentials.username());
        return traineeDAO.findByUsername(credentials.username());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> findAll() {
        log.debug("Finding all trainee records");
        return traineeDAO.findAll();
    }

    @Override
    @Transactional
    public void changePassword(Credentials credentials, String newPassword) {
        authenticationService.check(credentials);
        Trainee trainee = getByUsernameOrThrow(credentials.username());
        requireNotBlank(newPassword, "password");
        trainee.setPassword(passwordEncoder.encode(newPassword));
        log.info("Trainee with username {} is changing the password", credentials.username());
        traineeDAO.update(trainee);
    }

    @Override
    @Transactional
    public Trainee update(Credentials credentials, Trainee trainee) {
        authenticationService.check(credentials);
        Trainee foundTrainee = getByUsernameOrThrow(credentials.username());
        foundTrainee.setFirstName(trainee.getFirstName());
        foundTrainee.setLastName(trainee.getLastName());
        foundTrainee.setDateOfBirth(trainee.getDateOfBirth());
        foundTrainee.setAddress(trainee.getAddress());
        Set<ConstraintViolation<Trainee>> violations = validator.validate(foundTrainee);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        log.info("Updating the trainee with username {}", credentials.username());
        return traineeDAO.update(foundTrainee);
    }

    @Override
    @Transactional
    public void activate(Credentials credentials) {
        authenticationService.check(credentials);
        Trainee foundTrainee = getByUsernameOrThrow(credentials.username());
        if (foundTrainee.getIsActive()) {
            log.warn("Trainee with username {} is already active", credentials.username());
            throw new IllegalStateException("Trainee with the username " + credentials.username() + " is already active");
        }
        foundTrainee.setIsActive(true);
        log.info("Activating the trainee with username {}", credentials.username());

        traineeDAO.update(foundTrainee);
    }

    @Override
    @Transactional
    public void deactivate(Credentials credentials) {
        authenticationService.check(credentials);
        Trainee foundTrainee = getByUsernameOrThrow(credentials.username());
        if (!foundTrainee.getIsActive()) {
            log.warn("Trainee with username {} is already deactivated", credentials.username());
            throw new IllegalStateException("Trainee with the username " + credentials.username() + " is already inactive");
        }
        foundTrainee.setIsActive(false);
        log.info("Deactivating the trainee with username {}", credentials.username());
        traineeDAO.update(foundTrainee);
    }

    @Override
    @Transactional
    public boolean deleteByUsername(Credentials credentials) {
        authenticationService.check(credentials);
        log.info("Deleting the trainee with username {}", credentials.username());
        return traineeDAO.deleteByUsername(credentials.username());
    }

    @Override
    @Transactional
    public List<Trainer> updateTrainerList(Credentials credentials, List<String> trainerUsernames) {
        authenticationService.check(credentials);
        Trainee trainee = getByUsernameOrThrow(credentials.username());
        Set<Trainer> updatedTrainers = new HashSet<>();
        for (String trainerUsername : trainerUsernames) {
            Optional<Trainer> trainer = trainerDAO.findByUsername(trainerUsername);
            if (trainer.isEmpty()) {
                log.warn("Trainer with username {} was not found", trainerUsername);
                throw new IllegalArgumentException("Trainer with username " + trainerUsername + " was not found");
            }
            updatedTrainers.add(trainer.get());
        }
        trainee.setTrainers(updatedTrainers);
        log.info("Updating trainerList for the trainee with username {}", credentials.username());
        traineeDAO.update(trainee);
        return updatedTrainers.stream().toList();
    }

    private Trainee getByUsernameOrThrow(String username) {
        Optional<Trainee> foundTraineeOpt = traineeDAO.findByUsername(username);
        if (foundTraineeOpt.isEmpty()) {
            log.warn("Trainee with username {} was not found", username);
            throw new IllegalArgumentException("Trainee with the username " + username + " was not found");
        }
        return foundTraineeOpt.get();
    }
}
