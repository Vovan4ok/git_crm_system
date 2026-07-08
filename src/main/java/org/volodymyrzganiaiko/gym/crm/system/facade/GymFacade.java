package org.volodymyrzganiaiko.gym.crm.system.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.Training;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;
import org.volodymyrzganiaiko.gym.crm.system.dto.Credentials;
import org.volodymyrzganiaiko.gym.crm.system.dto.TraineeRegistrationDTO;
import org.volodymyrzganiaiko.gym.crm.system.dto.TrainerRegistrationDTO;
import org.volodymyrzganiaiko.gym.crm.system.service.TraineeService;
import org.volodymyrzganiaiko.gym.crm.system.service.TrainerService;
import org.volodymyrzganiaiko.gym.crm.system.service.TrainingService;
import org.volodymyrzganiaiko.gym.crm.system.service.TrainingTypeService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class GymFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService, TrainingTypeService trainingTypeService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.trainingTypeService = trainingTypeService;
    }

    public TraineeRegistrationDTO createTrainee(Trainee trainee) {
        return traineeService.create(trainee);
    }

    public Optional<Trainee> findTraineeById(Long id) {
        return traineeService.findById(id);
    }

    public Optional<Trainee> findTraineeByUsername(Credentials credentials) {
        return traineeService.findByUsername(credentials);
    }

    public void changeTraineePassword(Credentials credentials, String newPassword) {
        traineeService.changePassword(credentials, newPassword);
    }

    public Trainee updateTrainee(Credentials credentials, Trainee trainee) {
        return traineeService.update(credentials, trainee);
    }

    public void activateTrainee(Credentials credentials) {
        traineeService.activate(credentials);
    }

    public void deactivateTrainee(Credentials credentials) {
        traineeService.deactivate(credentials);
    }

    public boolean deleteTraineeByUsername(Credentials credentials) {
        return traineeService.deleteByUsername(credentials);
    }

    public List<Trainee> findAllTrainees() {
        return traineeService.findAll();
    }

    public List<Trainer> updateTrainerListForTrainee(Credentials credentials, List<String> trainerUsernames) {
        return traineeService.updateTrainerList(credentials, trainerUsernames);
    }

    public TrainerRegistrationDTO createTrainer(Trainer trainer) {
        return trainerService.create(trainer);
    }

    public Optional<Trainer> findTrainerById(Long id) {
        return trainerService.findById(id);
    }

    public Optional<Trainer> findTrainerByUsername(Credentials credentials) {
        return trainerService.findByUsername(credentials);
    }

    public void changeTrainerPassword(Credentials credentials, String newPassword) {
        trainerService.changePassword(credentials, newPassword);
    }

    public Trainer updateTrainer(Credentials credentials, Trainer trainer) {
        return trainerService.update(credentials, trainer);
    }

    public void activateTrainer(Credentials credentials) {
        trainerService.activate(credentials);
    }

    public void deactivateTrainer(Credentials credentials) {
        trainerService.deactivate(credentials);
    }

    public List<Trainer> getUnassignedTrainers(Credentials credentials) {
        return trainerService.getUnassignedTrainers(credentials);
    }

    public List<Trainer> findAllTrainers() {
        return trainerService.findAll();
    }

    public Training createTraining(Credentials credentials, Training training) {
        return trainingService.addTraining(credentials, training);
    }

    public Optional<Training> findTrainingById(Long id) {
        return trainingService.findById(id);
    }

    public List<Training> getTraineeTrainings(Credentials credentials, LocalDate from, LocalDate to, String trainerUsername, String trainingTypeName) {
        return trainingService.getTraineeTrainings(credentials, from, to, trainerUsername, trainingTypeName);
    }

    public List<Training> getTrainerTrainings(Credentials credentials, LocalDate from, LocalDate to, String traineeUsername) {
        return trainingService.getTrainerTrainings(credentials, from, to, traineeUsername);
    }

    public List<Training> findAllTrainings() {
        return trainingService.findAll();
    }

    public Optional<TrainingType> findTrainingTypeById(Long id) {
        return trainingTypeService.findById(id);
    }

    public List<TrainingType> findAllTrainingTypes() {
        return trainingTypeService.findAll();
    }
}
