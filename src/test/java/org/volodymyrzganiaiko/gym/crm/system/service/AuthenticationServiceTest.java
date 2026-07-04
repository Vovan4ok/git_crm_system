package org.volodymyrzganiaiko.gym.crm.system.service;

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
import org.volodymyrzganiaiko.gym.crm.system.exceptions.AuthenticationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainerDAO trainerDAO;

    @InjectMocks
    AuthenticationService authenticationService;

    @Test
    public void check_traineeFound_andPasswordMatches() {
        Trainee trainee = new Trainee(1L, LocalDate.parse("2003-11-08"), "Test address", new User(1L, "John", "Doe", "John.Doe", "random", true), new HashSet<>());
        when(traineeDAO.findByUsername(any())).thenReturn(Optional.of(trainee));

        assertDoesNotThrow(() -> authenticationService.check(trainee.getUser().getUsername(), trainee.getUser().getPassword()));
    }

    @Test
    public void check_traineeFound_andPasswordDoesntMatch() {
        when(traineeDAO.findByUsername(any())).thenReturn(Optional.of(new Trainee(2L, LocalDate.parse("2003-11-08"), "Test address", new User(2L, "Test", "Test", "Test.Test", "test", true), new HashSet<>())));

        assertThrows(AuthenticationException.class, () -> authenticationService.check("Test.Test", "password"));
    }

    @Test
    public void check_traineeNotFound_trainerFound() {
        when(traineeDAO.findByUsername(any())).thenReturn(Optional.empty());
        when(trainerDAO.findByUsername(any())).thenReturn(Optional.of(new Trainer(1L, new TrainingType(1L, "Yoga"), new User(1L, "John", "Doe", "John.Doe", "random", true), Set.of(new Trainee()))));

        assertDoesNotThrow(() -> authenticationService.check("John.Doe", "random"));
    }

    @Test
    public void check_traineeNotFound_trainerFound_andPasswordDoesntMatch() {
        when(traineeDAO.findByUsername(any())).thenReturn(Optional.empty());
        when(trainerDAO.findByUsername(any())).thenReturn(Optional.of(new Trainer(1L, new TrainingType(1L, "Yoga"), new User(1L, "John", "Doe", "John.Doe", "random", true), Set.of(new Trainee()))));

        assertThrows(AuthenticationException.class, () -> authenticationService.check("John.Doe", "password"));
    }

    @Test
    public void check_nobodyFound() {
        when(traineeDAO.findByUsername(any())).thenReturn(Optional.empty());
        when(trainerDAO.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> authenticationService.check("John.Doe", "random"));
    }
}
