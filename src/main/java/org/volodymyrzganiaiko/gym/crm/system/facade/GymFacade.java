package org.volodymyrzganiaiko.gym.crm.system.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.Training;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;
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

    public Optional<Trainee> findTraineeByUsername(String username, String password) {
        return traineeService.findByUsername(username, password);
    }

    public void changeTraineePassword(String username, String oldPassword, String newPassword) {
        traineeService.changePassword(username, oldPassword, newPassword);
    }

    public Trainee updateTrainee(String username, String password, Trainee trainee) {
        return traineeService.update(username, password, trainee);
    }

    public void activateTrainee(String username, String password) {
        traineeService.activate(username, password);
    }

    public void deactivateTrainee(String username, String password) {
        traineeService.deactivate(username, password);
    }

    public boolean deleteTraineeByUsername(String username, String password) {
        return traineeService.deleteByUsername(username, password);
    }

    public List<Trainee> findAllTrainees() {
        return traineeService.findAll();
    }

    public List<Trainer> updateTrainerListForTrainee(String username, String password, List<String> trainerUsernames) {
        return traineeService.updateTrainerList(username, password, trainerUsernames);
    }

    public TrainerRegistrationDTO createTrainer(Trainer trainer) {
        return trainerService.create(trainer);
    }

    public Optional<Trainer> findTrainerById(Long id) {
        return trainerService.findById(id);
    }

    public Optional<Trainer> findTrainerByUsername(String username, String password) {
        return trainerService.findByUsername(username, password);
    }

    public void changeTrainerPassword(String username, String oldPassword, String newPassword) {
        trainerService.changePassword(username, oldPassword, newPassword);
    }

    public Trainer updateTrainer(String username, String password, Trainer trainer) {
        return trainerService.update(username, password, trainer);
    }

    public void activateTrainer(String username, String password) {
        trainerService.activate(username, password);
    }

    public void deactivateTrainer(String username, String password) {
        trainerService.deactivate(username, password);
    }

    public List<Trainer> getUnassignedTrainers(String traineeUsername, String password) {
        return trainerService.getUnassignedTrainers(traineeUsername, password);
    }

    public List<Trainer> findAllTrainers() {
        return trainerService.findAll();
    }

    public Training createTraining(String username, String password, Training training) {
        return trainingService.addTraining(username, password, training);
    }

    public Optional<Training> findTrainingById(Long id) {
        return trainingService.findById(id);
    }

    public List<Training> getTraineeTrainings(String traineeUsername, String password, LocalDate from, LocalDate to, String trainerUsername, String trainingTypeName) {
        return trainingService.getTraineeTrainings(traineeUsername, password, from, to, trainerUsername, trainingTypeName);
    }

    public List<Training> getTrainerTrainings(String trainerUsername, String password, LocalDate from, LocalDate to, String traineeUsername) {
        return trainingService.getTrainerTrainings(trainerUsername, password, from, to, traineeUsername);
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
