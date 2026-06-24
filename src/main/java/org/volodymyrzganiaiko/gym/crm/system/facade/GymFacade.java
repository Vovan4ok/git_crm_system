package org.volodymyrzganiaiko.gym.crm.system.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.Training;
import org.volodymyrzganiaiko.gym.crm.system.service.TraineeService;
import org.volodymyrzganiaiko.gym.crm.system.service.TrainerService;
import org.volodymyrzganiaiko.gym.crm.system.service.TrainingService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class GymFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public Trainee createTrainee(Trainee trainee) {
        return traineeService.create(trainee);
    }

    public boolean updateTrainee(Trainee trainee) {
        return traineeService.update(trainee);
    }

    public Optional<Trainee> findTraineeById(UUID traineeId) {
        return traineeService.findById(traineeId);
    }

    public boolean deleteTrainee(UUID traineeId) {
        return traineeService.delete(traineeId);
    }

    public List<Trainee> findAllTrainees() {
        return traineeService.findAll();
    }

    public Trainer createTrainer(Trainer trainer) {
        return trainerService.create(trainer);
    }

    public boolean updateTrainer(Trainer trainer) {
        return trainerService.update(trainer);
    }

    public Optional<Trainer> findTrainerById(UUID trainerId) {
        return  trainerService.findById(trainerId);
    }

    public List<Trainer> findAllTrainers() {
        return trainerService.findAll();
    }

    public Training createTraining(Training training) {
        return trainingService.create(training);
    }

    public Optional<Training> findTrainingById(UUID trainingId) {
        return trainingService.findById(trainingId);
    }

    public List<Training> findAllTrainings() {
        return trainingService.findAll();
    }
}
