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
import org.volodymyrzganiaiko.gym.crm.system.service.TraineeService;
import org.volodymyrzganiaiko.gym.crm.system.service.TrainerService;
import org.volodymyrzganiaiko.gym.crm.system.service.TrainingService;
import org.volodymyrzganiaiko.gym.crm.system.service.TrainingTypeService;

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

    @InjectMocks
    GymFacade gymFacade;

    @Test
    void createTrainee() {
        Trainee returnTrainee = new Trainee(null, null, null, null, null, null, null, null);
        returnTrainee.setId(1L);
        TraineeRegistrationDTO shouldReturnDTO = new TraineeRegistrationDTO(returnTrainee, "test");
        when(traineeService.create(any())).thenReturn(shouldReturnDTO);
        Trainee input = new Trainee();

        TraineeRegistrationDTO result = gymFacade.createTrainee(input);

        assertEquals(1L, result.trainee().getId());
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
        when(traineeService.findByUsername(new Credentials("John.Doe", "pass"))).thenReturn(Optional.of(new Trainee()));

        Optional<Trainee> result = gymFacade.findTraineeByUsername(new Credentials("John.Doe", "pass"));

        assertTrue(result.isPresent());
        verify(traineeService).findByUsername(new Credentials("John.Doe", "pass"));
    }

    @Test
    void changeTraineePassword() {
        gymFacade.changeTraineePassword(new Credentials("John.Doe", "oldPass"), "newPass");
        verify(traineeService).changePassword(new Credentials("John.Doe", "oldPass"), "newPass");
    }

    @Test
    void updateTrainee() {
        when(traineeService.update(any(Credentials.class), any())).thenReturn(new Trainee());

        Trainee result = gymFacade.updateTrainee(new Credentials("John.Doe", "pass"), new Trainee());

        assertNotNull(result);
        verify(traineeService).update(any(Credentials.class), any());
    }

    @Test
    void activateTrainee() {
        gymFacade.activateTrainee(new Credentials("John.Doe", "pass"));
        verify(traineeService).activate(new Credentials("John.Doe", "pass"));
    }

    @Test
    void deactivateTrainee() {
        gymFacade.deactivateTrainee(new Credentials("John.Doe", "pass"));
        verify(traineeService).deactivate(new Credentials("John.Doe", "pass"));
    }

    @Test
    void deleteTraineeByUsername() {
        when(traineeService.deleteByUsername(new Credentials("John.Doe", "pass"))).thenReturn(true);

        boolean result = gymFacade.deleteTraineeByUsername(new Credentials("John.Doe", "pass"));

        assertTrue(result);
        verify(traineeService).deleteByUsername(new Credentials("John.Doe", "pass"));
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
        when(traineeService.updateTrainerList(any(Credentials.class), any())).thenReturn(new ArrayList<>());

        List<Trainer> result = gymFacade.updateTrainerListForTrainee(new Credentials("John.Doe", "pass"), new ArrayList<>());

        assertNotNull(result);
        verify(traineeService).updateTrainerList(any(Credentials.class), any());
    }

    @Test
    void createTrainer() {
        when(trainerService.create(any())).thenReturn(new TrainerRegistrationDTO(new Trainer(), "test"));
        Trainer input = new Trainer();

        TrainerRegistrationDTO result = gymFacade.createTrainer(input);

        assertNotNull(result);
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
        when(trainerService.findByUsername(new Credentials("John.Doe", "pass"))).thenReturn(Optional.of(new Trainer()));

        Optional<Trainer> result = gymFacade.findTrainerByUsername(new Credentials("John.Doe", "pass"));

        assertTrue(result.isPresent());
        verify(trainerService).findByUsername(new Credentials("John.Doe", "pass"));
    }

    @Test
    void changeTrainerPassword() {
        gymFacade.changeTrainerPassword(new Credentials("John.Doe", "oldPass"), "newPass");
        verify(trainerService).changePassword(new Credentials("John.Doe", "oldPass"), "newPass");
    }

    @Test
    void updateTrainer() {
        when(trainerService.update(any(Credentials.class), any())).thenReturn(new Trainer());

        Trainer result = gymFacade.updateTrainer(new Credentials("John.Doe", "pass"), new Trainer());

        assertNotNull(result);
        verify(trainerService).update(any(Credentials.class), any());
    }

    @Test
    void activateTrainer() {
        gymFacade.activateTrainer(new Credentials("John.Doe", "pass"));
        verify(trainerService).activate(new Credentials("John.Doe", "pass"));
    }

    @Test
    void deactivateTrainer() {
        gymFacade.deactivateTrainer(new Credentials("John.Doe", "pass"));
        verify(trainerService).deactivate(new Credentials("John.Doe", "pass"));
    }

    @Test
    void getUnassignedTrainers() {
        when(trainerService.getUnassignedTrainers(new Credentials("John.Doe", "pass"))).thenReturn(new ArrayList<>());

        List<Trainer> result = gymFacade.getUnassignedTrainers(new Credentials("John.Doe", "pass"));

        assertNotNull(result);
        verify(trainerService).getUnassignedTrainers(new Credentials("John.Doe", "pass"));
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
        when(trainingService.addTraining(any(Credentials.class), any())).thenReturn(new Training());
        Training input = new Training();

        Training result = gymFacade.createTraining(new Credentials("John.Doe", "pass"), input);

        assertNotNull(result);
        verify(trainingService).addTraining(any(Credentials.class), eq(input));
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
        when(trainingService.getTraineeTrainings(new Credentials("John.Doe", "pass"), null, null, null, null)).thenReturn(new ArrayList<>());

        List<Training> result = gymFacade.getTraineeTrainings(new Credentials("John.Doe", "pass"), null, null, null, null);

        assertNotNull(result);
        verify(trainingService).getTraineeTrainings(new Credentials("John.Doe", "pass"), null, null, null, null);
    }

    @Test
    void getTrainerTrainings() {
        when(trainingService.getTrainerTrainings(new Credentials("John.Doe", "pass"), null, null, null)).thenReturn(new ArrayList<>());

        List<Training> result = gymFacade.getTrainerTrainings(new Credentials("John.Doe", "pass"), null, null, null);

        assertNotNull(result);
        verify(trainingService).getTrainerTrainings(new Credentials("John.Doe", "pass"), null, null, null);
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