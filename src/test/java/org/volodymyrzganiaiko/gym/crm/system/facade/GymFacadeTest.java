package org.volodymyrzganiaiko.gym.crm.system.facade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.volodymyrzganiaiko.gym.crm.system.domain.*;
import org.volodymyrzganiaiko.gym.crm.system.dto.AddTrainingRequest;
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
}