package org.volodymyrzganiaiko.gym.crm.system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.volodymyrzganiaiko.gym.crm.system.dao.TraineeDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.service.TraineeService;
import org.volodymyrzganiaiko.gym.crm.system.utils.CredentialsGenerator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TraineeServiceImpl implements TraineeService {
    private final TraineeDAO traineeDAO;
    private final CredentialsGenerator credentialsGenerator;

    private static final Logger log =  LoggerFactory.getLogger(TraineeServiceImpl.class);

    @Autowired
    public TraineeServiceImpl(TraineeDAO traineeDAO, CredentialsGenerator credentialsGenerator) {
        this.traineeDAO = traineeDAO;
        this.credentialsGenerator = credentialsGenerator;
    }

    @Override
    public Trainee create(Trainee trainee) {
        log.info("[TraineeService]: Creating a trainee record {}", trainee);
        trainee.setUserId(UUID.randomUUID());
        trainee.setActive(true);
        trainee.setUsername(credentialsGenerator.generateUsername(trainee));
        trainee.setPassword(credentialsGenerator.generatePassword());
        return traineeDAO.save(trainee);
    }

    @Override
    public boolean update(Trainee trainee) {
        log.info("[TraineeService]: Updating the trainee record {}", trainee);
        Trainee traineeFromStorage = findById(trainee.getId()).orElse(null);
        if (traineeFromStorage == null) {
            log.warn("[TraineeService]: Trainee with id {} wasn't found",  trainee.getId());
            return false;
        }
        traineeFromStorage.setDateOfBirth(trainee.getDateOfBirth());
        traineeFromStorage.setAddress(trainee.getAddress());
        traineeFromStorage.setActive(trainee.isActive());
        return traineeDAO.update(traineeFromStorage);
    }

    @Override
    public boolean delete(UUID traineeId) {
        log.info("[TraineeService]: Deleting the trainee record with id {}", traineeId);
        return traineeDAO.delete(traineeId);
    }

    @Override
    public Optional<Trainee> findById(UUID traineeId) {
        log.debug("[TraineeService]: Finding the trainee record with id {}", traineeId);
        return traineeDAO.findById(traineeId);
    }

    @Override
    public List<Trainee> findAll() {
        log.debug("[TraineeService]: Finding all trainee records");
        return traineeDAO.findAll();
    }
}
