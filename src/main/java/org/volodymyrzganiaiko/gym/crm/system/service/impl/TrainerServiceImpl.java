package org.volodymyrzganiaiko.gym.crm.system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainingTypeDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;
import org.volodymyrzganiaiko.gym.crm.system.domain.User;
import org.volodymyrzganiaiko.gym.crm.system.service.AuthenticationService;
import org.volodymyrzganiaiko.gym.crm.system.service.CredentialsService;
import org.volodymyrzganiaiko.gym.crm.system.service.TrainerService;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

import static org.volodymyrzganiaiko.gym.crm.system.utils.ValueValidator.requireNotBlank;

@Service
public class TrainerServiceImpl implements TrainerService {
    private TrainerDAO trainerDAO;
    private TrainingTypeDAO trainingTypeDAO;
    private CredentialsService credentialsService;
    private AuthenticationService authenticationService;

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

    @Override
    @Transactional
    public Trainer create(Trainer trainer) {
        User user = trainer.getUser();
        credentialsService.assignCredentials(user);
        trainer.setUser(user);
        trainer.setSpecialization(resolveSpecialization(trainer.getSpecialization()));
        trainer = trainerDAO.save(trainer);
        log.info("Creating the trainer record with id {}", trainer.getId());
        return trainer;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainer> findById(Long id) {
        log.debug("Finding the trainer record with id {}", id);
        return trainerDAO.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainer> findByUsername(String username, String password) {
        authenticationService.check(username, password);
        log.debug("Finding the trainer with username {}", username);
        return trainerDAO.findByUsername(username);
    }

    @Override
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        authenticationService.check(username, oldPassword);
        Trainer trainer = getByUsernameOrThrow(username);
        requireNotBlank(newPassword, "password");
        trainer.getUser().setPassword(newPassword);
        log.info("Changing the password for the trainer with username {}", username);
        trainerDAO.update(trainer);
    }

    @Override
    @Transactional
    public Trainer update(String username, String password, Trainer trainer) {
        authenticationService.check(username, password);
        Trainer foundTrainer = getByUsernameOrThrow(username);
        requireNotBlank(trainer.getUser().getFirstName(), "firstName");
        foundTrainer.getUser().setFirstName(trainer.getUser().getFirstName());
        requireNotBlank(trainer.getUser().getLastName(), "lastName");
        foundTrainer.getUser().setLastName(trainer.getUser().getLastName());
        foundTrainer.setSpecialization(resolveSpecialization(trainer.getSpecialization()));
        log.info("Updating the trainer with username {}", username);
        return trainerDAO.update(foundTrainer);
    }

    @Override
    @Transactional
    public void activate(String username, String password) {
        authenticationService.check(username, password);
        Trainer foundTrainer = getByUsernameOrThrow(username);
        if (foundTrainer.getUser().getIsActive()) {
            log.warn("Trainer with the username {} is already active", username);
            throw new IllegalStateException("Trainer with the username " + username + " is already active");
        }
        foundTrainer.getUser().setIsActive(true);
        log.info("Activating the trainer with username {}", username);
        trainerDAO.update(foundTrainer);
    }

    @Override
    @Transactional
    public void deactivate(String username, String password) {
        authenticationService.check(username, password);
        Trainer foundTrainer = getByUsernameOrThrow(username);
        if (!foundTrainer.getUser().getIsActive()) {
            log.warn("Trainer with the username {} is already deactivated", username);
            throw new IllegalStateException("Trainer with the username " + username + " is already inactive");
        }
        foundTrainer.getUser().setIsActive(false);
        log.info("Deactivating the trainer with username {}", username);
        trainerDAO.update(foundTrainer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getUnassignedTrainers(String traineeUsername, String password) {
        authenticationService.check(traineeUsername, password);
        log.debug("Finding unassigned trainers for the trainee with username {}", traineeUsername);
        return trainerDAO.findUnassignedTrainers(traineeUsername);
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
