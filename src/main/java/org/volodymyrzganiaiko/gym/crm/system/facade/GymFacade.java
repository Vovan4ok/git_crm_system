package org.volodymyrzganiaiko.gym.crm.system.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.Training;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;
import org.volodymyrzganiaiko.gym.crm.system.dto.*;
import org.volodymyrzganiaiko.gym.crm.system.mapper.DtoMapper;
import org.volodymyrzganiaiko.gym.crm.system.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class GymFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;
    private final AuthenticationService authenticationService;
    private final CredentialsService credentialsService;
    private final UserService userService;
    private final DtoMapper mapper;

    @Autowired
    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService, TrainingTypeService trainingTypeService, AuthenticationService authenticationService, CredentialsService credentialsService, UserService userService, DtoMapper mapper) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.trainingTypeService = trainingTypeService;
        this.authenticationService = authenticationService;
        this.credentialsService = credentialsService;
        this.userService = userService;
        this.mapper = mapper;
    }

    public void login(Credentials credentials) {
        authenticationService.check(credentials);
    }

    @Transactional
    public void changeLogin(Credentials credentials, String newPassword) {
        authenticationService.check(credentials);
        userService.changePassword(credentials.username(), newPassword);
    }

    @Transactional(readOnly = true)
    public TraineeProfileResponse getTraineeProfile(Credentials credentials, String username) {
        authenticationService.check(credentials);
        Trainee trainee = traineeService.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Trainee with the username " + username + " was not found"));
        return mapper.mapTraineeToTraineeProfileResponse(trainee);
    }

    @Transactional
    public TraineeRegistrationDTO createTrainee(Trainee trainee) {
        String rawPassword = credentialsService.assignCredentials(trainee);
        Trainee saved = traineeService.create(trainee);
        return new TraineeRegistrationDTO(saved.getUsername(), rawPassword);
    }

    @Transactional
    public TraineeProfileResponse updateTraineeProfile(Credentials credentials, String username, Trainee data) {
        authenticationService.check(credentials);
        Trainee trainee = traineeService.update(username, data.getFirstName(), data.getLastName(), data.getIsActive(), data.getDateOfBirth(), data.getAddress());
        return mapper.mapTraineeToTraineeProfileResponse(trainee);
    }

    @Transactional
    public void deleteTraineeProfile(Credentials credentials, String username) {
        authenticationService.check(credentials);
        traineeService.deleteByUsername(username);
    }

    @Transactional
    public TrainerProfileResponse getTrainerProfile(Credentials credentials, String username) {
        authenticationService.check(credentials);
        Trainer trainer = trainerService.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Trainer with the username " + username + " was not found"));
        return mapper.mapTrainerToTrainerProfileResponse(trainer);
    }

    @Transactional
    public TrainerProfileResponse updateTrainerProfile(Credentials credentials, String username, Trainer data) {
        authenticationService.check(credentials);
        Trainer trainer = trainerService.update(username, data.getFirstName(), data.getLastName(), data.getIsActive());
        return mapper.mapTrainerToTrainerProfileResponse(trainer);
    }

    @Transactional
    public List<TrainerSummaryResponse> getUnassignedTrainers(Credentials credentials, String username) {
        authenticationService.check(credentials);
        return trainerService.getUnassignedTrainers(username).stream().map(mapper::mapTrainerToTrainerSummaryResponse).toList();
    }

    @Transactional
    public List<TrainerSummaryResponse> updateTrainers(Credentials credentials, String username, List<String> trainerUsernames) {
        authenticationService.check(credentials);
        return traineeService.updateTrainerList(username, trainerUsernames).stream().map(mapper::mapTrainerToTrainerSummaryResponse).toList();
    }

    @Transactional
    public void createTraining(Credentials credentials, AddTrainingRequest req) {
        authenticationService.check(credentials);
        trainingService.addTraining(req.traineeUsername(), req.trainerUsername(), req.trainingName(), req.trainingDate(), req.trainingDuration());
    }

    public Optional<Trainee> findTraineeById(Long id) {
        return traineeService.findById(id);
    }

    public Optional<Trainee> findTraineeByUsername(Credentials credentials) {
        authenticationService.check(credentials);
        return traineeService.findByUsername(credentials.username());
    }

    public void changeTraineePassword(Credentials credentials, String newPassword) {
        authenticationService.check(credentials);
        traineeService.changePassword(credentials.username(), newPassword);
    }

    public Trainee updateTrainee(Credentials credentials, Trainee trainee) {
        authenticationService.check(credentials);
        return traineeService.update(credentials.username(), trainee.getFirstName(), trainee.getLastName(), trainee.getIsActive(), trainee.getDateOfBirth(), trainee.getAddress());
    }

    public void activateTrainee(Credentials credentials) {
        authenticationService.check(credentials);
        traineeService.activate(credentials.username());
    }

    public void deactivateTrainee(Credentials credentials) {
        authenticationService.check(credentials);
        traineeService.deactivate(credentials.username());
    }

    public boolean deleteTraineeByUsername(Credentials credentials) {
        authenticationService.check(credentials);
        return traineeService.deleteByUsername(credentials.username());
    }

    public List<Trainee> findAllTrainees() {
        return traineeService.findAll();
    }

    public List<Trainer> updateTrainerListForTrainee(Credentials credentials, List<String> trainerUsernames) {
        authenticationService.check(credentials);
        return traineeService.updateTrainerList(credentials.username(), trainerUsernames);
    }

    @Transactional
    public TrainerRegistrationDTO createTrainer(Trainer trainer) {
        String rawPassword = credentialsService.assignCredentials(trainer);
        Trainer saved = trainerService.create(trainer);
        return new TrainerRegistrationDTO(saved.getUsername(), rawPassword);
    }

    public Optional<Trainer> findTrainerById(Long id) {
        return trainerService.findById(id);
    }

    public Optional<Trainer> findTrainerByUsername(Credentials credentials) {
        authenticationService.check(credentials);
        return trainerService.findByUsername(credentials.username());
    }

    public void changeTrainerPassword(Credentials credentials, String newPassword) {
        authenticationService.check(credentials);
        trainerService.changePassword(credentials.username(), newPassword);
    }

    public Trainer updateTrainer(Credentials credentials, Trainer trainer) {
        authenticationService.check(credentials);
        return trainerService.update(credentials.username(), trainer.getFirstName(), trainer.getLastName(), trainer.getIsActive());
    }

    public void activateTrainer(Credentials credentials) {
        authenticationService.check(credentials);
        trainerService.activate(credentials.username());
    }

    public void deactivateTrainer(Credentials credentials) {
        authenticationService.check(credentials);
        trainerService.deactivate(credentials.username());
    }

    public List<Trainer> getUnassignedTrainers(Credentials credentials) {
        authenticationService.check(credentials);
        return trainerService.getUnassignedTrainers(credentials.username());
    }

    public List<Trainer> findAllTrainers() {
        return trainerService.findAll();
    }

    public Training createTraining(Credentials credentials, Training training) {
        authenticationService.check(credentials);
        return trainingService.addTraining(training);
    }

    public Optional<Training> findTrainingById(Long id) {
        return trainingService.findById(id);
    }

    public List<Training> getTraineeTrainings(Credentials credentials, LocalDate from, LocalDate to, String trainerUsername, String trainingTypeName) {
        authenticationService.check(credentials);
        return trainingService.getTraineeTrainings(credentials.username(), from, to, trainerUsername, trainingTypeName);
    }

    public List<Training> getTrainerTrainings(Credentials credentials, LocalDate from, LocalDate to, String traineeUsername) {
        authenticationService.check(credentials);
        return trainingService.getTrainerTrainings(credentials.username(), from, to, traineeUsername);
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
