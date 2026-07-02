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
        user.setIsActive(true);
        user.setUsername(credentialsService.generateUsername(user));
        user.setPassword(credentialsService.generatePassword());
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
        return trainerDAO.findByUsername(username);
    }

    @Override
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        authenticationService.check(username, oldPassword);
        Optional<Trainer> foundTraineeOpt = trainerDAO.findByUsername(username);
        if (foundTraineeOpt.isEmpty()) {
            throw new IllegalArgumentException("Trainer with username " + username + " was not found");
        }
        Trainer trainer = foundTraineeOpt.get();
        requireNotBlank(newPassword, "password");
        trainer.getUser().setPassword(newPassword);
        trainerDAO.update(trainer);
    }

    @Override
    @Transactional
    public Trainer update(String username, String password, Trainer trainer) {
        authenticationService.check(username, password);
        Optional<Trainer> foundTrainerOpt = trainerDAO.findByUsername(username);
        if (foundTrainerOpt.isEmpty()) {
            throw new IllegalArgumentException("Trainer with username " + username + " was not found");
        }
        Trainer foundTrainer = foundTrainerOpt.get();
        foundTrainer.setSpecialization(resolveSpecialization(trainer.getSpecialization()));
        requireNotBlank(trainer.getUser().getFirstName(), "firstName");
        foundTrainer.getUser().setFirstName(trainer.getUser().getFirstName());
        requireNotBlank(trainer.getUser().getLastName(), "lastName");
        foundTrainer.getUser().setLastName(trainer.getUser().getLastName());
        return trainerDAO.update(foundTrainer);
    }

    @Override
    @Transactional
    public void activate(String username, String password) {
        authenticationService.check(username, password);
        Optional<Trainer> foundTrainerOpt = trainerDAO.findByUsername(username);
        if (foundTrainerOpt.isEmpty()) {
            throw new IllegalArgumentException("Trainer with the username " + username + " was not found");
        }
        Trainer foundTrainer = foundTrainerOpt.get();
        if (foundTrainer.getUser().getIsActive()) {
            throw new IllegalStateException("Trainer with the username " + username + " is already active");
        }
        foundTrainer.getUser().setIsActive(true);
        trainerDAO.update(foundTrainer);
    }

    @Override
    @Transactional
    public void deactivate(String username, String password) {
        authenticationService.check(username, password);
        Optional<Trainer> foundTrainerOpt = trainerDAO.findByUsername(username);
        if (foundTrainerOpt.isEmpty()) {
            throw new IllegalArgumentException("Trainer with the username " + username + " was not found");
        }
        Trainer foundTrainer = foundTrainerOpt.get();
        if (!foundTrainer.getUser().getIsActive()) {
            throw new IllegalStateException("Trainer with the username " + username + " is already inactive");
        }
        foundTrainer.getUser().setIsActive(false);
        trainerDAO.update(foundTrainer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getUnassignedTrainers(String traineeUsername, String password) {
        authenticationService.check(traineeUsername, password);
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

    private void requireNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
    }
}
