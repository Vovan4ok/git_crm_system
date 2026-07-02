package org.volodymyrzganiaiko.gym.crm.system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.volodymyrzganiaiko.gym.crm.system.dao.TraineeDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.User;
import org.volodymyrzganiaiko.gym.crm.system.service.AuthenticationService;
import org.volodymyrzganiaiko.gym.crm.system.service.CredentialsService;
import org.volodymyrzganiaiko.gym.crm.system.service.TraineeService;

import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TraineeServiceImpl implements TraineeService {
    private TraineeDAO traineeDAO;
    private TrainerDAO trainerDAO;
    private CredentialsService credentialsService;
    private AuthenticationService authenticationService;

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

    @Override
    @Transactional
    public Trainee create(Trainee trainee) {
        User user = trainee.getUser();
        user.setIsActive(true);
        user.setUsername(credentialsService.generateUsername(user));
        user.setPassword(credentialsService.generatePassword());
        trainee.setUser(user);
        trainee = traineeDAO.save(trainee);
        log.info("Creating a trainee record with id {}", trainee.getId());
        return trainee;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainee> findById(Long id) {
        log.debug("Finding the trainee record with id {}", id);
        return traineeDAO.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainee> findByUsername(String username, String password) {
        authenticationService.check(username, password);
        return traineeDAO.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> findAll() {
        log.debug("Finding all trainee records");
        return traineeDAO.findAll();
    }

    @Override
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        authenticationService.check(username, oldPassword);
        Optional<Trainee> foundTraineeOpt = traineeDAO.findByUsername(username);
        if (foundTraineeOpt.isEmpty()) {
            throw new IllegalArgumentException("Trainee with username " + username + " was not found");
        }
        Trainee trainee = foundTraineeOpt.get();
        requireNotBlank(newPassword, "password");
        trainee.getUser().setPassword(newPassword);
        traineeDAO.update(trainee);
    }

    @Override
    @Transactional
    public Trainee update(String username, String password, Trainee trainee) {
        authenticationService.check(username, password);
        Optional<Trainee> foundTraineeOpt = traineeDAO.findByUsername(username);
        if (foundTraineeOpt.isEmpty()) {
            throw new IllegalArgumentException("Trainee with usernames " + username + " and " + trainee.getUser().getUsername() + " was not found");
        }
        Trainee foundTrainee = foundTraineeOpt.get();
        foundTrainee.setDateOfBirth(trainee.getDateOfBirth());
        foundTrainee.setAddress(trainee.getAddress());
        requireNotBlank(trainee.getUser().getFirstName(), "firstName");
        foundTrainee.getUser().setFirstName(trainee.getUser().getFirstName());
        requireNotBlank(trainee.getUser().getLastName(), "lastName");
        foundTrainee.getUser().setLastName(trainee.getUser().getLastName());
        return traineeDAO.update(foundTrainee);
    }

    @Override
    @Transactional
    public void activate(String username, String password) {
        authenticationService.check(username, password);
        Optional<Trainee> foundTraineeOpt = traineeDAO.findByUsername(username);
        if (foundTraineeOpt.isEmpty()) {
            throw new IllegalArgumentException("Trainee with the username " + username + " was not found");
        }
        Trainee foundTrainee = foundTraineeOpt.get();
        if (foundTrainee.getUser().getIsActive()) {
            throw new IllegalStateException("Trainee with the username " + username + " is already active");
        }
        foundTrainee.getUser().setIsActive(true);
        traineeDAO.update(foundTrainee);
    }

    @Override
    @Transactional
    public void deactivate(String username, String password) {
        authenticationService.check(username, password);
        Optional<Trainee> foundTraineeOpt = traineeDAO.findByUsername(username);
        if (foundTraineeOpt.isEmpty()) {
            throw new IllegalArgumentException("Trainee with the username" + username + " was not found");
        }
        Trainee foundTrainee = foundTraineeOpt.get();
        if (!foundTrainee.getUser().getIsActive()) {
            throw new IllegalStateException("Trainee with the username " + username + " is already inactive");
        }
        foundTrainee.getUser().setIsActive(false);
        traineeDAO.update(foundTrainee);
    }

    @Override
    @Transactional
    public boolean deleteByUsername(String username, String password) {
        authenticationService.check(username, password);
        return traineeDAO.deleteByUsername(username);
    }

    @Override
    @Transactional
    public List<Trainer> updateTrainerList(String username, String password, List<String> trainerUsernames) {
        authenticationService.check(username, password);
        Optional<Trainee> foundTraineeOpt = traineeDAO.findByUsername(username);
        if (foundTraineeOpt.isEmpty()) {
            throw new IllegalArgumentException("Trainee with the username " + username + " was not found");
        }
        Trainee trainee = foundTraineeOpt.get();
        Set<Trainer> updatedTrainers = new HashSet<>();
        for (String trainerUsername : trainerUsernames) {
            Optional<Trainer> trainer = trainerDAO.findByUsername(trainerUsername);
            if (trainer.isEmpty()) {
                throw new IllegalArgumentException("Trainer with username " + trainerUsername + " was not found");
            }
            updatedTrainers.add(trainer.get());
        }
        trainee.setTrainers(updatedTrainers);
        traineeDAO.update(trainee);
        return updatedTrainers.stream().toList();
    }

    private void requireNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
    }
}
