package org.volodymyrzganiaiko.gym.crm.system.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.volodymyrzganiaiko.gym.crm.system.dao.TraineeDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.User;
import org.volodymyrzganiaiko.gym.crm.system.dto.Credentials;
import org.volodymyrzganiaiko.gym.crm.system.dto.TraineeRegistrationDTO;
import org.volodymyrzganiaiko.gym.crm.system.exceptions.AuthenticationException;
import org.volodymyrzganiaiko.gym.crm.system.service.impl.TraineeServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
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

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Trainee trainee;

    @BeforeEach
    public void setUp() {
        trainee = new Trainee(null, null, "John", "Doe", "John.Doe", "random", true, new HashSet<>());
        trainee.setId(1L);
    }

    @Test
    public void create_generatesCredentials_andSaves() {
        doAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setUsername("John.Doe");
            u.setPassword("random");
            u.setIsActive(true);
            return "random";
        }).when(credentialsService).assignCredentials(any(User.class));
        when(traineeDAO.save(any(Trainee.class))).thenAnswer(inv -> inv.getArgument(0));

        TraineeRegistrationDTO result = traineeService.create(trainee);

        assertTrue(result.trainee().getIsActive());
        assertEquals("John.Doe", result.trainee().getUsername());
        assertEquals("random", result.password());
        verify(traineeDAO).save(trainee);
        verifyNoInteractions(authenticationService);
        verify(credentialsService).assignCredentials(trainee);
    }

    @Test
    public void findByUsername_positiveCase() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.ofNullable(trainee));

        Optional<Trainee> result = traineeService.findByUsername(new Credentials(trainee.getUsername(), trainee.getPassword()));

        assertTrue(result.isPresent());
        verify(authenticationService).check(new Credentials(trainee.getUsername(), trainee.getPassword()));
    }

    @Test
    public void findByUsername_negativeCase_throwsException() {
        doThrow(new AuthenticationException("Some message")).when(authenticationService).check(any(Credentials.class));

        assertThrows(AuthenticationException.class, () -> traineeService.findByUsername(new Credentials(trainee.getUsername(), trainee.getPassword())));
        verifyNoInteractions(traineeDAO);
    }

    @Test
    public void changePassword_success() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));
        when(passwordEncoder.encode("random1")).thenReturn("random1");

        traineeService.changePassword(new Credentials("John.Doe", "random"), "random1");

        assertEquals("random1", trainee.getPassword());
        verify(authenticationService).check(new Credentials("John.Doe", "random"));
        verify(traineeDAO).update(trainee);
    }

    @Test
    public void changePassword_throwsException() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));

        assertThrows(IllegalArgumentException.class, () -> traineeService.changePassword(new Credentials("John.Doe", "randomPass"), " "));
        verify(traineeDAO, never()).update(any());
    }

    @Test
    public void changePassword_traineeNotFound_throwsException() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> traineeService.changePassword(new Credentials("John.Doe", "random"), "randomPassword"));
        verify(traineeDAO, never()).update(any());
    }

    @Test
    public void update_updatesTrainee_success() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));
        when(traineeDAO.update(any(Trainee.class))).thenAnswer(inv -> inv.getArgument(0));

        trainee.setDateOfBirth(LocalDate.parse("2003-08-11"));
        trainee.setAddress("Test address");
        trainee.setFirstName("Test");
        trainee.setLastName("Test");

        Trainee result = traineeService.update(new Credentials(trainee.getUsername(), trainee.getPassword()), trainee);

        assertTrue(result.getIsActive());
        assertEquals("Test", result.getFirstName());
        assertEquals("Test", result.getLastName());
        assertEquals("Test address", result.getAddress());
        assertEquals(LocalDate.parse("2003-08-11"), result.getDateOfBirth());
        verify(traineeDAO).update(trainee);
        verify(authenticationService).check(new Credentials(trainee.getUsername(), trainee.getPassword()));
    }

    @Test
    public void update_blankFirstName_throwsException() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));

        trainee.setFirstName("");

        assertThrows(IllegalArgumentException.class, () -> traineeService.update(new Credentials(trainee.getUsername(), trainee.getPassword()), trainee));
        verify(traineeDAO, never()).update(any());
    }

    @Test
    public void update_traineeNotFound_throwsException() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.empty());

        trainee.setFirstName("Test");

        assertThrows(IllegalArgumentException.class, () -> traineeService.update(new Credentials(trainee.getUsername(), trainee.getPassword()), trainee));
        verify(traineeDAO, never()).update(any());
    }

    @Test
    public void activateTrainee_success() {
        trainee.setIsActive(false);
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));

        traineeService.activate(new Credentials(trainee.getUsername(), trainee.getPassword()));

        assertTrue(trainee.getIsActive());
        verify(traineeDAO).update(trainee);
    }

    @Test
    public void activateTrainee_throwsException() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));

        assertThrows(IllegalStateException.class, () -> traineeService.activate(new Credentials(trainee.getUsername(), trainee.getPassword())));
        verify(traineeDAO, never()).update(any());
    }

    @Test
    public void deactivateTrainee_success() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));

        traineeService.deactivate(new Credentials(trainee.getUsername(), trainee.getPassword()));

        assertFalse(trainee.getIsActive());
        verify(traineeDAO).update(trainee);
    }

    @Test
    public void deactivateTrainee_throwsException() {
        trainee.setIsActive(false);
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));

        assertThrows(IllegalStateException.class, () -> traineeService.deactivate(new Credentials(trainee.getUsername(), trainee.getPassword())));
        verify(traineeDAO, never()).update(any());
    }

    @Test
    public void deleteByUsername_success() {
        when(traineeDAO.deleteByUsername(any(String.class))).thenReturn(true);

        boolean result = traineeService.deleteByUsername(new Credentials(trainee.getUsername(), trainee.getPassword()));

        assertTrue(result);
        verify(traineeDAO).deleteByUsername(trainee.getUsername());
    }

    @Test
    public void updateTrainersList_success() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByUsername("trainer1")).thenReturn(Optional.of(new Trainer()));

        traineeService.updateTrainerList(new Credentials(trainee.getUsername(), trainee.getPassword()), List.of("trainer1"));

        assertEquals(1, trainee.getTrainers().size());
        verify(traineeDAO).update(trainee);
    }

    @Test
    public void updateTrainersList_trainerNotFound() {
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> traineeService.updateTrainerList(new Credentials(trainee.getUsername(), trainee.getPassword()), List.of("ghost")));verify(traineeDAO, never()).update(any());
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
