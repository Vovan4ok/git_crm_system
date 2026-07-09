package org.volodymyrzganiaiko.gym.crm.system.facade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.volodymyrzganiaiko.gym.crm.system.domain.*;
import org.volodymyrzganiaiko.gym.crm.system.dto.Credentials;
import org.volodymyrzganiaiko.gym.crm.system.dto.TraineeRegistrationDTO;
import org.volodymyrzganiaiko.gym.crm.system.dto.TrainerRegistrationDTO;
import org.volodymyrzganiaiko.gym.crm.system.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GymFacadeTest {
    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @Mock
    private TrainingTypeService trainingTypeService;
    
    @Mock
    private AuthenticationService authenticationService;
    
    @Mock
    private CredentialsService credentialsService;

    @InjectMocks
    GymFacade gymFacade;

    @Test
    void createTrainee() {
        Trainee saved = new Trainee();
        saved.setUsername("John.Doe");
        when(credentialsService.assignCredentials(any())).thenReturn("test");
        when(traineeService.create(any())).thenReturn(saved);
        Trainee input = new Trainee();

        TraineeRegistrationDTO result = gymFacade.createTrainee(input);

        assertEquals("John.Doe", result.username());
        assertEquals("test", result.password());
        verify(credentialsService).assignCredentials(input);
        verify(traineeService).create(input);
    }

    @Test
    void findTraineeById() {
        when(traineeService.findById(1L)).thenReturn(Optional.of(new Trainee()));

        Optional<Trainee> result = gymFacade.findTraineeById(1L);

        assertTrue(result.isPresent());
        verify(traineeService).findById(1L);
    }

    @Test
    void findTraineeByUsername() {
        when(traineeService.findByUsername("John.Doe")).thenReturn(Optional.of(new Trainee()));

        Optional<Trainee> result = gymFacade.findTraineeByUsername(new Credentials("John.Doe", "pass"));

        assertTrue(result.isPresent());
        verify(traineeService).findByUsername("John.Doe");
    }

    @Test
    void changeTraineePassword() {
        gymFacade.changeTraineePassword(new Credentials("John.Doe", "oldPass"), "newPass");
        verify(traineeService).changePassword("John.Doe", "newPass");
    }

    @Test
    void updateTrainee() {
        when(traineeService.update(any(), any(), any(), any(), any())).thenReturn(new Trainee());

        Trainee result = gymFacade.updateTrainee(new Credentials("John.Doe", "pass"), new Trainee());

        assertNotNull(result);
        verify(traineeService).update("John.Doe", null, null, null, null);
    }

    @Test
    void activateTrainee() {
        gymFacade.activateTrainee(new Credentials("John.Doe", "pass"));
        verify(traineeService).activate("John.Doe");
    }

    @Test
    void deactivateTrainee() {
        gymFacade.deactivateTrainee(new Credentials("John.Doe", "pass"));
        verify(traineeService).deactivate("John.Doe");
    }

    @Test
    void deleteTraineeByUsername() {
        when(traineeService.deleteByUsername("John.Doe")).thenReturn(true);

        boolean result = gymFacade.deleteTraineeByUsername(new Credentials("John.Doe", "pass"));

        assertTrue(result);
        verify(traineeService).deleteByUsername("John.Doe");
    }

    @Test
    void findAllTrainees() {
        when(traineeService.findAll()).thenReturn(List.of(new Trainee()));

        List<Trainee> result = gymFacade.findAllTrainees();

        assertNotNull(result);
        verify(traineeService).findAll();
    }

    @Test
    void updateTrainerListForTrainee() {
        when(traineeService.updateTrainerList(any(), any())).thenReturn(new ArrayList<>());

        List<Trainer> result = gymFacade.updateTrainerListForTrainee(new Credentials("John.Doe", "pass"), new ArrayList<>());

        assertNotNull(result);
        verify(traineeService).updateTrainerList(any(), any());
    }

    @Test
    void createTrainer() {
        Trainer saved = new Trainer();
        saved.setUsername("John.Doe");
        when(credentialsService.assignCredentials(any())).thenReturn("test");
        when(trainerService.create(any())).thenReturn(saved);
        Trainer input = new Trainer();

        TrainerRegistrationDTO result = gymFacade.createTrainer(input);

        assertEquals("John.Doe", result.username());
        assertEquals("test", result.password());
        verify(credentialsService).assignCredentials(input);
        verify(trainerService).create(input);
    }

    @Test
    void findTrainerById() {
        when(trainerService.findById(1L)).thenReturn(Optional.of(new Trainer()));

        Optional<Trainer> result = gymFacade.findTrainerById(1L);

        assertTrue(result.isPresent());
        verify(trainerService).findById(1L);
    }

    @Test
    void findTrainerByUsername() {
        when(trainerService.findByUsername("John.Doe")).thenReturn(Optional.of(new Trainer()));

        Optional<Trainer> result = gymFacade.findTrainerByUsername(new Credentials("John.Doe", "pass"));

        assertTrue(result.isPresent());
        verify(trainerService).findByUsername("John.Doe");
    }

    @Test
    void changeTrainerPassword() {
        gymFacade.changeTrainerPassword(new Credentials("John.Doe", "oldPass"), "newPass");
        verify(trainerService).changePassword("John.Doe", "newPass");
    }

    @Test
    void updateTrainer() {
        when(trainerService.update(any(), any(), any(), any())).thenReturn(new Trainer());

        Trainer result = gymFacade.updateTrainer(new Credentials("John.Doe", "pass"), new Trainer());

        assertNotNull(result);
        verify(trainerService).update("John.Doe", null, null, null);
    }

    @Test
    void activateTrainer() {
        gymFacade.activateTrainer(new Credentials("John.Doe", "pass"));
        verify(trainerService).activate("John.Doe");
    }

    @Test
    void deactivateTrainer() {
        gymFacade.deactivateTrainer(new Credentials("John.Doe", "pass"));
        verify(trainerService).deactivate("John.Doe");
    }

    @Test
    void getUnassignedTrainers() {
        when(trainerService.getUnassignedTrainers("John.Doe")).thenReturn(new ArrayList<>());

        List<Trainer> result = gymFacade.getUnassignedTrainers(new Credentials("John.Doe", "pass"));

        assertNotNull(result);
        verify(trainerService).getUnassignedTrainers("John.Doe");
    }

    @Test
    void findAllTrainers() {
        when(trainerService.findAll()).thenReturn(List.of(new Trainer()));

        List<Trainer> result = gymFacade.findAllTrainers();

        assertNotNull(result);
        verify(trainerService).findAll();
    }

    @Test
    void createTraining() {
        when(trainingService.addTraining(any())).thenReturn(new Training());
        Training input = new Training();

        Training result = gymFacade.createTraining(new Credentials("John.Doe", "pass"), input);

        assertNotNull(result);
        verify(trainingService).addTraining(eq(input));
    }

    @Test
    void findTrainingById() {
        when(trainingService.findById(1L)).thenReturn(Optional.of(new Training()));

        Optional<Training> result = gymFacade.findTrainingById(1L);

        assertTrue(result.isPresent());
        verify(trainingService).findById(1L);
    }

    @Test
    void getTraineeTrainings() {
        when(trainingService.getTraineeTrainings("John.Doe", null, null, null, null)).thenReturn(new ArrayList<>());

        List<Training> result = gymFacade.getTraineeTrainings(new Credentials("John.Doe", "pass"), null, null, null, null);

        assertNotNull(result);
        verify(trainingService).getTraineeTrainings("John.Doe", null, null, null, null);
    }

    @Test
    void getTrainerTrainings() {
        when(trainingService.getTrainerTrainings("John.Doe", null, null, null)).thenReturn(new ArrayList<>());

        List<Training> result = gymFacade.getTrainerTrainings(new Credentials("John.Doe", "pass"), null, null, null);

        assertNotNull(result);
        verify(trainingService).getTrainerTrainings("John.Doe", null, null, null);
    }

    @Test
    void findAllTrainings() {
        when(trainingService.findAll()).thenReturn(new ArrayList<>());

        List<Training> result = gymFacade.findAllTrainings();

        assertNotNull(result);
        verify(trainingService).findAll();
    }

    @Test
    void findTrainingTypeById() {
        when(trainingTypeService.findById(1L)).thenReturn(Optional.of(new TrainingType()));

        Optional<TrainingType> result = gymFacade.findTrainingTypeById(1L);

        assertTrue(result.isPresent());
        verify(trainingTypeService).findById(1L);
    }

    @Test
    void findAllTrainingTypes() {
        when(trainingTypeService.findAll()).thenReturn(new ArrayList<>());

        List<TrainingType> result = gymFacade.findAllTrainingTypes();

        assertNotNull(result);
        verify(trainingTypeService).findAll();
    }
}