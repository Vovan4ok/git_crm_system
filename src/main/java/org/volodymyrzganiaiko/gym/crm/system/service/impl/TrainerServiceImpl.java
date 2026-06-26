package org.volodymyrzganiaiko.gym.crm.system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.service.TrainerService;
import org.volodymyrzganiaiko.gym.crm.system.utils.CredentialsGenerator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TrainerServiceImpl implements TrainerService {
    private final TrainerDAO trainerDAO;
    private final CredentialsGenerator credentialsGenerator;

    private static final Logger log = LoggerFactory.getLogger(TrainerServiceImpl.class);

    @Autowired
    public TrainerServiceImpl(TrainerDAO trainerDAO, CredentialsGenerator credentialsGenerator) {
        this.trainerDAO = trainerDAO;
        this.credentialsGenerator = credentialsGenerator;
    }

    @Override
    public Trainer create(Trainer trainer) {
        trainer.setUserId(UUID.randomUUID());
        trainer.setActive(true);
        trainer.setUsername(credentialsGenerator.generateUsername(trainer));
        trainer.setPassword(credentialsGenerator.generatePassword());
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
