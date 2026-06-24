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
        log.info("[TrainerService]: Creating the trainer record {}", trainer);
        trainer.setUserId(UUID.randomUUID());
        trainer.setActive(true);
        trainer.setUsername(credentialsGenerator.generateUsername(trainer));
        trainer.setPassword(credentialsGenerator.generatePassword());
        return trainerDAO.save(trainer);
    }

    @Override
    public boolean update(Trainer trainer) {
        log.info("[TrainerService]: Updating the trainer record with id {}", trainer.getId());
        Trainer trainerFromStorage = findById(trainer.getId()).orElse(null);
        if (trainerFromStorage == null) {
            log.warn("[TrainerService]: Trainer with id {} wasn't found",  trainer.getId());
            return false;
        }
        trainerFromStorage.setSpecialization(trainer.getSpecialization());
        trainerFromStorage.setActive(trainer.isActive());
        return trainerDAO.update(trainerFromStorage);
    }

    @Override
    public Optional<Trainer> findById(UUID trainerId) {
        log.debug("[TrainerService]: Finding the trainer record with id {}", trainerId);
        return trainerDAO.findById(trainerId);
    }

    @Override
    public List<Trainer> findAll() {
        log.debug("[TrainerService]: Finding the trainers records");
        return trainerDAO.findAll();
    }
}
