package org.volodymyrzganiaiko.gym.crm.system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.volodymyrzganiaiko.gym.crm.system.dao.TraineeDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.service.CredentialsService;
import org.volodymyrzganiaiko.gym.crm.system.service.TraineeService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TraineeServiceImpl implements TraineeService {
    private final TraineeDAO traineeDAO;
    private final CredentialsService credentialsService;

    private static final Logger log =  LoggerFactory.getLogger(TraineeServiceImpl.class);

    @Autowired
    public TraineeServiceImpl(TraineeDAO traineeDAO, CredentialsService credentialsService) {
        this.traineeDAO = traineeDAO;
        this.credentialsService = credentialsService;
    }

    @Override
    public Trainee create(Trainee trainee) {
        trainee.setUserId(UUID.randomUUID());
        trainee.setActive(true);
        trainee.setUsername(credentialsService.generateUsername(trainee));
        trainee.setPassword(credentialsService.generatePassword());
        log.info("Creating a trainee record with id {}", trainee.getUserId());
        return traineeDAO.save(trainee);
    }

    @Override
    public boolean update(Trainee trainee) {
        log.info("Updating the trainee record with id {}", trainee.getUserId());
        Trainee traineeFromStorage = findById(trainee.getUserId()).orElse(null);
        if (traineeFromStorage == null) {
            log.warn("Trainee with id {} wasn't found",  trainee.getUserId());
            return false;
        }
        traineeFromStorage.setDateOfBirth(trainee.getDateOfBirth());
        traineeFromStorage.setAddress(trainee.getAddress());
        traineeFromStorage.setActive(trainee.isActive());
        return traineeDAO.update(traineeFromStorage);
    }

    @Override
    public boolean delete(UUID traineeId) {
        log.info("Deleting the trainee record with id {}", traineeId);
        return traineeDAO.delete(traineeId);
    }

    @Override
    public Optional<Trainee> findById(UUID traineeId) {
        log.debug("Finding the trainee record with id {}", traineeId);
        return traineeDAO.findById(traineeId);
    }

    @Override
    public List<Trainee> findAll() {
        log.debug("Finding all trainee records");
        return traineeDAO.findAll();
    }
}
