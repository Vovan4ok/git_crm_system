package org.volodymyrzganiaiko.gym.crm.system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.service.CredentialsService;
import org.volodymyrzganiaiko.gym.crm.system.service.TrainerService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TrainerServiceImpl implements TrainerService {
    private TrainerDAO trainerDAO;
    private CredentialsService credentialsService;

    private static final Logger log = LoggerFactory.getLogger(TrainerServiceImpl.class);

    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Autowired
    public void setCredentialsService(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @Override
    public Trainer create(Trainer trainer) {
        trainer.setUserId(UUID.randomUUID());
        trainer.setActive(true);
        trainer.setUsername(credentialsService.generateUsername(trainer));
        trainer.setPassword(credentialsService.generatePassword());
        log.info("Creating the trainer record with id {}", trainer.getUserId());
        return trainerDAO.save(trainer);
    }

    @Override
    public boolean update(Trainer trainer) {
        log.info("Updating the trainer record with id {}", trainer.getUserId());
        Trainer trainerFromStorage = findById(trainer.getUserId()).orElse(null);
        if (trainerFromStorage == null) {
            log.warn("Trainer with id {} wasn't found",  trainer.getUserId());
            return false;
        }
        trainerFromStorage.setSpecialization(trainer.getSpecialization());
        trainerFromStorage.setActive(trainer.isActive());
        return trainerDAO.update(trainerFromStorage);
    }

    @Override
    public Optional<Trainer> findById(UUID trainerId) {
        log.debug("Finding the trainer record with id {}", trainerId);
        return trainerDAO.findById(trainerId);
    }

    @Override
    public List<Trainer> findAll() {
        log.debug("Finding the trainers records");
        return trainerDAO.findAll();
    }
}
