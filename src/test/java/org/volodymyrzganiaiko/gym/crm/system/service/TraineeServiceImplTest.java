package org.volodymyrzganiaiko.gym.crm.system.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.volodymyrzganiaiko.gym.crm.system.dao.TraineeDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.User;
import org.volodymyrzganiaiko.gym.crm.system.exceptions.AuthenticationException;
import org.volodymyrzganiaiko.gym.crm.system.service.impl.TraineeServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceImplTest {
    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private CredentialsService credentialsService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Trainee trainee;

    @BeforeEach
    public void setUp() {
        User user = new User("John", "Doe", "John.Doe", "random", true);
        trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUser(user);
    }

    @Test
    public void create_generatesCredentials_andSaves() {
        when(credentialsService.generateUsername(any(User.class))).thenReturn("John.Doe");
        when(credentialsService.generatePassword()).thenReturn("random");
        when(traineeDAO.save(any(Trainee.class))).thenAnswer(inv -> inv.getArgument(0));

        Trainee result = traineeService.create(trainee);

        assertTrue(result.getUser().getIsActive());
        assertEquals("John.Doe", result.getUser().getUsername());
        assertEquals("random", result.getUser().getPassword());
        verify(traineeDAO).save(trainee);
        verifyNoInteractions(authenticationService);
    }

    @Test
    public void findByUsername_positiveCase() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.ofNullable(trainee));

        Optional<Trainee> result = traineeService.findByUsername(trainee.getUser().getUsername(), trainee.getUser().getPassword());

        assertTrue(result.isPresent());
        verify(authenticationService).check(trainee.getUser().getUsername(), trainee.getUser().getPassword());
    }

    @Test
    public void findByUsername_negativeCase_throwsException() {
        doThrow(new AuthenticationException("Some message")).when(authenticationService).check(any(String.class), any(String.class));

        assertThrows(AuthenticationException.class, () -> traineeService.findByUsername(trainee.getUser().getUsername(), trainee.getUser().getPassword()));
        verifyNoInteractions(traineeDAO);
    }

    @Test
    public void changePassword_success() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));

        traineeService.changePassword("John.Doe", "random", "random1");

        assertEquals("random1", trainee.getUser().getPassword());
        verify(authenticationService).check("John.Doe", "random");
        verify(traineeDAO).update(trainee);
    }

    @Test
    public void changePassword_throwsException() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));

        assertThrows(IllegalArgumentException.class, () -> traineeService.changePassword("John.Doe", "randomPass", " "));
        verify(traineeDAO, never()).update(any());
    }

    @Test
    public void changePassword_traineeNotFound_throwsException() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> traineeService.changePassword("John.Doe", "random", "randomPassword"));
        verify(traineeDAO, never()).update(any());
    }

    @Test
    public void update_updatesTrainee_success() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));
        when(traineeDAO.update(any(Trainee.class))).thenAnswer(inv -> inv.getArgument(0));

        trainee.setDateOfBirth(LocalDate.parse("2003-08-11"));
        trainee.setAddress("Test address");
        trainee.getUser().setFirstName("Test");
        trainee.getUser().setLastName("Test");

        Trainee result = traineeService.update(trainee.getUser().getUsername(), trainee.getUser().getPassword(), trainee);

        assertTrue(result.getUser().getIsActive());
        assertEquals("Test", result.getUser().getFirstName());
        assertEquals("Test", result.getUser().getLastName());
        assertEquals("Test address", result.getAddress());
        assertEquals(LocalDate.parse("2003-08-11"), result.getDateOfBirth());
        verify(traineeDAO).update(trainee);
        verify(authenticationService).check(trainee.getUser().getUsername(), trainee.getUser().getPassword());
    }

    @Test
    public void update_blankFirstName_throwsException() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));

        trainee.getUser().setFirstName("");

        assertThrows(IllegalArgumentException.class, () -> traineeService.update(trainee.getUser().getUsername(), trainee.getUser().getPassword(), trainee));
        verify(traineeDAO, never()).update(any());
    }

    @Test
    public void update_traineeNotFound_throwsException() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.empty());

        trainee.getUser().setFirstName("Test");

        assertThrows(IllegalArgumentException.class, () -> traineeService.update(trainee.getUser().getUsername(), trainee.getUser().getPassword(), trainee));
        verify(traineeDAO, never()).update(any());
    }

    @Test
    public void activateTrainee_success() {
        trainee.getUser().setIsActive(false);
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));

        traineeService.activate(trainee.getUser().getUsername(), trainee.getUser().getPassword());

        assertTrue(trainee.getUser().getIsActive());
        verify(traineeDAO).update(trainee);
    }

    @Test
    public void activateTrainee_throwsException() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));

        assertThrows(IllegalStateException.class, () -> traineeService.activate(trainee.getUser().getUsername(), trainee.getUser().getPassword()));
        verify(traineeDAO, never()).update(any());
    }

    @Test
    public void deactivateTrainee_success() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));

        traineeService.deactivate(trainee.getUser().getUsername(), trainee.getUser().getPassword());

        assertFalse(trainee.getUser().getIsActive());
        verify(traineeDAO).update(trainee);
    }

    @Test
    public void deactivateTrainee_throwsException() {
        trainee.getUser().setIsActive(false);
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));

        assertThrows(IllegalStateException.class, () -> traineeService.deactivate(trainee.getUser().getUsername(), trainee.getUser().getPassword()));
        verify(traineeDAO, never()).update(any());
    }

    @Test
    public void deleteByUsername_success() {
        when(traineeDAO.deleteByUsername(any(String.class))).thenReturn(true);

        boolean result = traineeService.deleteByUsername(trainee.getUser().getUsername(), trainee.getUser().getPassword());

        assertTrue(result);
        verify(traineeDAO).deleteByUsername(trainee.getUser().getUsername());
    }

    @Test
    public void updateTrainersList_success() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByUsername("trainer1")).thenReturn(Optional.of(new Trainer()));

        traineeService.updateTrainerList(trainee.getUser().getUsername(), trainee.getUser().getPassword(), List.of("trainer1"));

        assertEquals(1, trainee.getTrainers().size());
        verify(traineeDAO).update(trainee);
    }

    @Test
    public void updateTrainersList_trainerNotFound() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> traineeService.updateTrainerList(trainee.getUser().getUsername(), trainee.getUser().getPassword(), List.of("ghost")));verify(traineeDAO, never()).update(any());
    }

    @Test
    public void findById_traineeFound() {
        when(traineeDAO.findById(any(Long.class))).thenReturn(Optional.of(trainee));

        Optional<Trainee> result = traineeService.findById(trainee.getId());

        assertTrue(result.isPresent());
        verify(traineeDAO).findById(trainee.getId());
    }

    @Test
    public void findById_traineeNotFound() {
        when(traineeDAO.findById(any(Long.class))).thenReturn(Optional.empty());
        Long input = 2L;

        Optional<Trainee> result = traineeService.findById(input);

        assertFalse(result.isPresent());
        verify(traineeDAO).findById(input);
    }

    @Test
    public void findAll_success() {
        when(traineeDAO.findAll()).thenReturn(new ArrayList<>());

        List<Trainee> result = traineeService.findAll();

        assertTrue(result.isEmpty());
        verify(traineeDAO).findAll();
    }
}
