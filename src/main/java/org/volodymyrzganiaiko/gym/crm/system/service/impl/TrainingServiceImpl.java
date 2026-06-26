package org.volodymyrzganiaiko.gym.crm.system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainingDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Training;
import org.volodymyrzganiaiko.gym.crm.system.service.TrainingService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TrainingServiceImpl implements TrainingService {
    private TrainingDAO trainingDAO;

    private static final Logger log = LoggerFactory.getLogger(TrainingServiceImpl.class);

    @Autowired
    public void setTrainingDAO(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Override
    public Training create(Training training) {
        training.setTrainingId(UUID.randomUUID());
        log.info("Creating the training record with id {}", training.getTrainingId());
        return trainingDAO.save(training);
    }

    @Override
    public Optional<Training> findById(UUID trainingId) {
        log.debug("Finding the training record with id {}", trainingId);
        return trainingDAO.findById(trainingId);
    }

    @Override
    public List<Training> findAll() {
        log.debug("Finding all training records");
        return trainingDAO.findAll();
    }
}
