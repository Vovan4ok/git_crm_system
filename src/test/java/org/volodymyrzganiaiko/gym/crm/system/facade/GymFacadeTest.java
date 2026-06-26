package org.volodymyrzganiaiko.gym.crm.system.facade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.Training;
import org.volodymyrzganiaiko.gym.crm.system.service.TraineeService;
import org.volodymyrzganiaiko.gym.crm.system.service.TrainerService;
import org.volodymyrzganiaiko.gym.crm.system.service.TrainingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GymFacadeTest {
    @Mock
    TraineeService traineeService;

    @Mock
    TrainerService trainerService;

    @Mock
    TrainingService trainingService;

    @InjectMocks
    GymFacade gymFacade;

    @Test
    public void verifyTraineeCreate() {
        Trainee input = new Trainee();
        Trainee expected = new Trainee();
        when(traineeService.create(input)).thenReturn(expected);

        Trainee result = gymFacade.createTrainee(input);

        assertSame(expected, result);
        verify(traineeService).create(input);
    }

    @Test
    public void verifyUpdateTrainee() {
        Trainee input = new Trainee();
        when(traineeService.update(input)).thenReturn(true);

        boolean result = gymFacade.updateTrainee(input);

        assertTrue(result);
        verify(traineeService).update(input);
    }

    @Test
    public void verifyDeleteTrainee() {
        UUID input = UUID.randomUUID();
        when(traineeService.delete(input)).thenReturn(true);

        boolean result = gymFacade.deleteTrainee(input);

        assertTrue(result);
        verify(traineeService).delete(input);
    }

    @Test
    public void verifyFindByIdTrainee() {
        UUID input = UUID.randomUUID();
        Optional<Trainee> expected = Optional.of(new Trainee());
        when(traineeService.findById(input)).thenReturn(expected);

        Optional<Trainee> result = gymFacade.findTraineeById(input);

        assertSame(expected, result);
        verify(traineeService).findById(input);
    }

    @Test
    public void verifyFindAllTrainees() {
        List<Trainee> expected = new ArrayList<>();
        when(traineeService.findAll()).thenReturn(expected);

        List<Trainee> result = gymFacade.findAllTrainees();

        assertSame(expected, result);
        verify(traineeService).findAll();
    }

    @Test
    public void verifyCreateTrainer() {
        Trainer input = new Trainer();
        Trainer expected = new Trainer();
        when(trainerService.create(input)).thenReturn(expected);

        Trainer result = gymFacade.createTrainer(input);

        assertSame(expected, result);
        verify(trainerService).create(input);
    }

    @Test
    public void verifyUpdateTrainer() {
        Trainer input = new Trainer();
        when(trainerService.update(input)).thenReturn(true);

        boolean result = gymFacade.updateTrainer(input);

        assertTrue(result);
        verify(trainerService).update(any());
    }

    @Test
    public void verifyFindByIdTrainer() {
        UUID input = UUID.randomUUID();
        Optional<Trainer> expected = Optional.of(new Trainer());
        when(trainerService.findById(input)).thenReturn(expected);

        Optional<Trainer> result = gymFacade.findTrainerById(input);

        assertSame(expected, result);
        verify(trainerService).findById(any());
    }

    @Test
    public void verifyFindAllTrainers() {
        List<Trainer> expected = new ArrayList<>();
        when(trainerService.findAll()).thenReturn(expected);

        List<Trainer> result = gymFacade.findAllTrainers();

        assertSame(expected, result);
        verify(trainerService).findAll();
    }

    @Test
    public void verifyCreateTraining() {
        Training input = new Training();
        Training expected = new Training();
        when(trainingService.create(input)).thenReturn(expected);

        Training result = gymFacade.createTraining(input);

        assertSame(expected, result);
        verify(trainingService).create(input);
    }

    @Test
    public void findTrainingById() {
        UUID input = UUID.randomUUID();
        Optional<Training> expected = Optional.of(new Training());
        when(trainingService.findById(input)).thenReturn(expected);

        Optional<Training> result = gymFacade.findTrainingById(input);

        assertSame(expected, result);
        verify(trainingService).findById(input);
    }

    @Test
    public void findAllTrainings() {
        List<Training> expected = new ArrayList<>();
        when(trainingService.findAll()).thenReturn(expected);

        List<Training> result = gymFacade.findAllTrainings();

        assertSame(expected, result);
        verify(trainingService).findAll();
    }
}
