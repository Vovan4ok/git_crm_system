package org.volodymyrzganiaiko.gym.crm.system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainingTypeDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;
import org.volodymyrzganiaiko.gym.crm.system.dto.Credentials;
import org.volodymyrzganiaiko.gym.crm.system.dto.TrainerRegistrationDTO;
import org.volodymyrzganiaiko.gym.crm.system.service.AuthenticationService;
import org.volodymyrzganiaiko.gym.crm.system.service.CredentialsService;
import org.volodymyrzganiaiko.gym.crm.system.service.TrainerService;

import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.volodymyrzganiaiko.gym.crm.system.utils.ValueValidator.requireNotBlank;

@Service
public class TrainerServiceImpl implements TrainerService {
    private TrainerDAO trainerDAO;
    private TrainingTypeDAO trainingTypeDAO;
    private CredentialsService credentialsService;
    private AuthenticationService authenticationService;
    private PasswordEncoder passwordEncoder;
    private Validator validator;

    private static final Logger log = LoggerFactory.getLogger(TrainerServiceImpl.class);

    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Autowired
    public void setTrainingTypeDAO(TrainingTypeDAO trainingTypeDAO) {
        this.trainingTypeDAO = trainingTypeDAO;
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
    public TrainerRegistrationDTO create(Trainer trainer) {
        String password = credentialsService.assignCredentials(trainer);
        trainer.setSpecialization(resolveSpecialization(trainer.getSpecialization()));
        Set<ConstraintViolation<Trainer>> violations = validator.validate(trainer);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        trainer = trainerDAO.save(trainer);
        log.info("Creating the trainer record with id {}", trainer.getId());
        return new TrainerRegistrationDTO(trainer, password);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainer> findById(Long id) {
        log.debug("Finding the trainer record with id {}", id);
        return trainerDAO.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainer> findByUsername(Credentials credentials) {
        authenticationService.check(credentials);
        log.debug("Finding the trainer with username {}", credentials.username());
        return trainerDAO.findByUsername(credentials.username());
    }

    @Override
    @Transactional
    public void changePassword(Credentials credentials, String newPassword) {
        authenticationService.check(credentials);
        Trainer trainer = getByUsernameOrThrow(credentials.username());
        requireNotBlank(newPassword, "password");
        trainer.setPassword(passwordEncoder.encode(newPassword));
        log.info("Changing the password for the trainer with username {}", credentials.username());
        trainerDAO.update(trainer);
    }

    @Override
    @Transactional
    public Trainer update(Credentials credentials, Trainer trainer) {
        authenticationService.check(credentials);
        Trainer foundTrainer = getByUsernameOrThrow(credentials.username());
        foundTrainer.setFirstName(trainer.getFirstName());
        foundTrainer.setLastName(trainer.getLastName());
        foundTrainer.setSpecialization(resolveSpecialization(trainer.getSpecialization()));
        Set<ConstraintViolation<Trainer>> violations = validator.validate(foundTrainer);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        log.info("Updating the trainer with username {}", credentials.username());
        return trainerDAO.update(foundTrainer);
    }

    @Override
    @Transactional
    public void activate(Credentials credentials) {
        authenticationService.check(credentials);
        Trainer foundTrainer = getByUsernameOrThrow(credentials.username());
        if (foundTrainer.getIsActive()) {
            log.warn("Trainer with the username {} is already active", credentials.username());
            throw new IllegalStateException("Trainer with the username " + credentials.username() + " is already active");
        }
        foundTrainer.setIsActive(true);
        log.info("Activating the trainer with username {}", credentials.username());
        trainerDAO.update(foundTrainer);
    }

    @Override
    @Transactional
    public void deactivate(Credentials credentials) {
        authenticationService.check(credentials);
        Trainer foundTrainer = getByUsernameOrThrow(credentials.username());
        if (!foundTrainer.getIsActive()) {
            log.warn("Trainer with the username {} is already deactivated", credentials.username());
            throw new IllegalStateException("Trainer with the username " + credentials.username() + " is already inactive");
        }
        foundTrainer.setIsActive(false);
        log.info("Deactivating the trainer with username {}", credentials.username());
        trainerDAO.update(foundTrainer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getUnassignedTrainers(Credentials credentials) {
        authenticationService.check(credentials);
        log.debug("Finding unassigned trainers for the trainee with username {}", credentials.username());
        return trainerDAO.findUnassignedTrainers(credentials.username());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> findAll() {
        log.debug("Finding the trainers records");
        return trainerDAO.findAll();
    }

    private TrainingType resolveSpecialization(TrainingType specialization) {
        if (specialization == null || specialization.getId() == null) {
            throw new IllegalArgumentException("Specialization (training type) id is required");
        }
        return trainingTypeDAO.findById(specialization.getId()).orElseThrow(() -> new IllegalArgumentException("Training type with id " + specialization.getId() + " not found"));
    }

    private Trainer getByUsernameOrThrow(String username) {
        Optional<Trainer> foundTrainerOpt = trainerDAO.findByUsername(username);
        if (foundTrainerOpt.isEmpty()) {
            log.warn("Trainer with username {} wasn't found", username);
            throw new IllegalArgumentException("Trainer with the username " + username + " was not found");
        }
        return foundTrainerOpt.get();
    }
}
