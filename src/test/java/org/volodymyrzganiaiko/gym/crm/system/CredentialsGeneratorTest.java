package org.volodymyrzganiaiko.gym.crm.system;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.volodymyrzganiaiko.gym.crm.system.dao.TraineeDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;
import org.volodymyrzganiaiko.gym.crm.system.domain.User;
import org.volodymyrzganiaiko.gym.crm.system.utils.CredentialsGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CredentialsGeneratorTest {
    @Mock
    TraineeDAO traineeDAO;

    @Mock
    TrainerDAO trainerDAO;

    @InjectMocks
    CredentialsGenerator credentialsGenerator;

    @Test
    public void testGenerateUsernameWithoutCollisions() {
        when(traineeDAO.findAll()).thenReturn(new ArrayList<>());
        when(trainerDAO.findAll()).thenReturn(new ArrayList<>());
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername(credentialsGenerator.generateUsername(user));
        verify(traineeDAO).findAll();
        verify(trainerDAO).findAll();
        assertEquals("John.Doe", user.getUsername());
    }

    @Test
    public void testGenerateUsernameWithCollisions() {
        List<Trainee> trainees = new ArrayList<>();
        trainees.add(new Trainee("John", "Doe", "John.Doe", "random", true, LocalDate.parse("2003-08-11"), "Test address", UUID.randomUUID()));
        when(traineeDAO.findAll()).thenReturn(trainees);
        when(trainerDAO.findAll()).thenReturn(new ArrayList<>());
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setUsername(credentialsGenerator.generateUsername(trainee));
        verify(traineeDAO).findAll();
        verify(trainerDAO).findAll();
        assertEquals("John.Doe.1", trainee.getUsername());
    }

    @Test
    public void testGenerateUsernameWithCrossCollisions() {
        List<Trainee> trainees = new ArrayList<>();
        trainees.add(new Trainee("John", "Doe", "John.Doe", "random", true, LocalDate.parse("2003-08-11"), "Test address", UUID.randomUUID()));
        when(traineeDAO.findAll()).thenReturn(trainees);
        when(trainerDAO.findAll()).thenReturn(new ArrayList<>());
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setUsername(credentialsGenerator.generateUsername(trainer));
        verify(traineeDAO).findAll();
        verify(trainerDAO).findAll();
        assertEquals("John.Doe.1", trainer.getUsername());
    }

    @Test
    public void testGenerateUsernameWithMultipleCollision() {
        List<Trainee> trainees = new ArrayList<>();
        trainees.add(new Trainee("John", "Doe", "John.Doe", "random", true, LocalDate.parse("2003-08-11"), "Test address", UUID.randomUUID()));
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(new Trainer("John", "Doe", "John.Doe.1", "random", true, new TrainingType("Joga"), UUID.randomUUID()));
        when(traineeDAO.findAll()).thenReturn(trainees);
        when(trainerDAO.findAll()).thenReturn(trainers);
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setUsername(credentialsGenerator.generateUsername(trainer));
        verify(traineeDAO).findAll();
        verify(trainerDAO).findAll();
        assertEquals("John.Doe.2", trainer.getUsername());
    }

    @Test
    public void testGeneratePasswordLengthEqualsTo10() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword(credentialsGenerator.generatePassword());
        assertEquals(10, user.getPassword().length());
    }

    @Test
    public void testPasswordForSymbols() {
        String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword(credentialsGenerator.generatePassword());
        for  (int i = 0; i < user.getPassword().length(); i++) {
            assertTrue(ALPHABET.contains(user.getPassword().substring(i, i + 1)));
        }
    }
}
