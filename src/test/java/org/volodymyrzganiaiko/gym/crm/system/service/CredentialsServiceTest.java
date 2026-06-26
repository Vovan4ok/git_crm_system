package org.volodymyrzganiaiko.gym.crm.system.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.volodymyrzganiaiko.gym.crm.system.dao.TraineeDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;
import org.volodymyrzganiaiko.gym.crm.system.utils.CredentialsGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CredentialsServiceTest {
    @Mock
    TraineeDAO traineeDAO;

    @Mock
    TrainerDAO trainerDAO;

    CredentialsService credentialsService;

    @BeforeEach
    public void initCredentialsService() {
        credentialsService = new CredentialsService();
        credentialsService.setTraineeDAO(traineeDAO);
        credentialsService.setTrainerDAO(trainerDAO);
        credentialsService.setCredentialsGenerator(new CredentialsGenerator());
    }

    @Test
    public void testGenerateUsernameWithoutCollision() {
        when(traineeDAO.findAll()).thenReturn(List.of());
        when(trainerDAO.findAll()).thenReturn(List.of());
        Trainer input = new Trainer();
        input.setFirstName("John");
        input.setLastName("Doe");
        String expected = "John.Doe";

        String result = credentialsService.generateUsername(input);

        assertEquals(expected, result);
    }

    @Test
    public void testGenerateUsernamesWithCrossCollisions() {
        when(traineeDAO.findAll()).thenReturn(List.of(new Trainee("John", "Doe", "John.Doe", "random", true, LocalDate.parse("2022-10-10"), "Test address", UUID.randomUUID())));
        when(trainerDAO.findAll()).thenReturn(List.of());
        Trainer input = new Trainer();
        input.setFirstName("John");
        input.setLastName("Doe");
        String expected = "John.Doe.1";

        String result = credentialsService.generateUsername(input);

        assertEquals(expected, result);
    }

    @Test
    public void testGenerateUsernameWithMultipleCollisions() {
        when(traineeDAO.findAll()).thenReturn(List.of(new Trainee("John", "Doe", "John.Doe", "random", true, LocalDate.parse("2022-10-10"), "Test address", UUID.randomUUID())));
        when(trainerDAO.findAll()).thenReturn(List.of(new Trainer("John", "Doe", "John.Doe.1", "random", true, new TrainingType("Joga"), UUID.randomUUID())));
        Trainer input = new Trainer();
        input.setFirstName("John");
        input.setLastName("Doe");
        String expected = "John.Doe.2";

        String result = credentialsService.generateUsername(input);

        assertEquals(expected, result);
    }
}
