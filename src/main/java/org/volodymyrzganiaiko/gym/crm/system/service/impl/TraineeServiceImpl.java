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
import org.volodymyrzganiaiko.gym.crm.system.dto.TraineeRegistrationDTO;
import org.volodymyrzganiaiko.gym.crm.system.service.AuthenticationService;
import org.volodymyrzganiaiko.gym.crm.system.service.CredentialsService;
import org.volodymyrzganiaiko.gym.crm.system.service.TraineeService;

import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public TraineeRegistrationDTO create(Trainee trainee) {
        String password = credentialsService.assignCredentials(trainee);
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
    public Optional<Trainee> findByUsername(String username, String password) {
        authenticationService.check(username, password);
        log.debug("Finding the trainee with username {}", username);
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
        Trainee trainee = getByUsernameOrThrow(username);
        requireNotBlank(newPassword, "password");
        trainee.setPassword(passwordEncoder.encode(newPassword));
        log.info("Trainee with username {} is changing the password", username);
        traineeDAO.update(trainee);
    }

    @Override
    @Transactional
    public Trainee update(String username, String password, Trainee trainee) {
        authenticationService.check(username, password);
        Trainee foundTrainee = getByUsernameOrThrow(username);
        requireNotBlank(trainee.getFirstName(), "firstName");
        foundTrainee.setFirstName(trainee.getFirstName());
        requireNotBlank(trainee.getLastName(), "lastName");
        foundTrainee.setLastName(trainee.getLastName());
        foundTrainee.setDateOfBirth(trainee.getDateOfBirth());
        foundTrainee.setAddress(trainee.getAddress());
        log.info("Updating the trainee with username {}", username);
        return traineeDAO.update(foundTrainee);
    }

    @Override
    @Transactional
    public void activate(String username, String password) {
        authenticationService.check(username, password);
        Trainee foundTrainee = getByUsernameOrThrow(username);
        if (foundTrainee.getIsActive()) {
            log.warn("Trainee with username {} is already active", username);
            throw new IllegalStateException("Trainee with the username " + username + " is already active");
        }
        foundTrainee.setIsActive(true);
        log.info("Activating the trainee with username {}", username);

        traineeDAO.update(foundTrainee);
    }

    @Override
    @Transactional
    public void deactivate(String username, String password) {
        authenticationService.check(username, password);
        Trainee foundTrainee = getByUsernameOrThrow(username);
        if (!foundTrainee.getIsActive()) {
            log.warn("Trainee with username {} is already deactivated", username);
            throw new IllegalStateException("Trainee with the username " + username + " is already inactive");
        }
        foundTrainee.setIsActive(false);
        log.info("Deactivating the trainee with username {}", username);
        traineeDAO.update(foundTrainee);
    }

    @Override
    @Transactional
    public boolean deleteByUsername(String username, String password) {
        authenticationService.check(username, password);
        log.info("Deleting the trainee with username {}", username);
        return traineeDAO.deleteByUsername(username);
    }

    @Override
    @Transactional
    public List<Trainer> updateTrainerList(String username, String password, List<String> trainerUsernames) {
        authenticationService.check(username, password);
        Trainee trainee = getByUsernameOrThrow(username);
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
        log.info("Updating trainerList for the trainee with username {}", username);
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
