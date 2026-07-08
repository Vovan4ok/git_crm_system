package org.volodymyrzganiaiko.gym.crm.system.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainingTypeDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;
import org.volodymyrzganiaiko.gym.crm.system.domain.User;
import org.volodymyrzganiaiko.gym.crm.system.dto.Credentials;
import org.volodymyrzganiaiko.gym.crm.system.dto.TrainerRegistrationDTO;
import org.volodymyrzganiaiko.gym.crm.system.exceptions.AuthenticationException;
import org.volodymyrzganiaiko.gym.crm.system.service.impl.TrainerServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceImplTest {
    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private TrainingTypeDAO trainingTypeDAO;

    @Mock
    private CredentialsService credentialsService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    Trainer trainer;

    @BeforeEach
    public void setUp() {
        trainer = new Trainer(new TrainingType(1L, "Yoga"), "John", "Doe", "John.Doe", "random", true, new HashSet<>());
        trainer.setId(1L);
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
        when(trainerDAO.save(any(Trainer.class))).thenAnswer(inv -> inv.getArgument(0));
        when(trainingTypeDAO.findById(1L)).thenReturn(Optional.of(new TrainingType(1L, "Yoga")));

        TrainerRegistrationDTO result = trainerService.create(trainer);

        assertTrue(result.trainer().getIsActive());
        assertEquals("John.Doe", result.trainer().getUsername());
        assertEquals("random", result.password());
        verify(trainerDAO).save(trainer);
        verifyNoInteractions(authenticationService);
        verify(credentialsService).assignCredentials(trainer);
    }

    @Test
    public void create_throwsException() {
        trainer.setSpecialization(null);

        assertThrows(IllegalArgumentException.class, () -> trainerService.create(trainer));
        verify(trainerDAO, never()).save(any());
    }

    @Test
    public void findByUsername_positiveCase() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.ofNullable(trainer));

        Optional<Trainer> result = trainerService.findByUsername(new Credentials(trainer.getUsername(), trainer.getPassword()));

        assertTrue(result.isPresent());
        verify(authenticationService).check(new Credentials(trainer.getUsername(), trainer.getPassword()));
    }

    @Test
    public void findByUsername_negativeCase_throwsException() {
        doThrow(new AuthenticationException("Some message")).when(authenticationService).check(any(Credentials.class));

        assertThrows(AuthenticationException.class, () -> trainerService.findByUsername(new Credentials(trainer.getUsername(), trainer.getPassword())));
        verifyNoInteractions(trainerDAO);
    }

    @Test
    public void changePassword_success() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));
        when(passwordEncoder.encode("random1")).thenReturn("random1");

        trainerService.changePassword(new Credentials("John.Doe", "random"), "random1");

        assertEquals("random1", trainer.getPassword());
        verify(authenticationService).check(new Credentials("John.Doe", "random"));
        verify(trainerDAO).update(trainer);
    }

    @Test
    public void changePassword_throwsException() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));

        assertThrows(IllegalArgumentException.class, () -> trainerService.changePassword(new Credentials("John.Doe", "randomPass"), " "));
        verify(trainerDAO, never()).update(any());
    }

    @Test
    public void changePassword_trainerNotFound_throwsException() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> trainerService.changePassword(new Credentials("John.Doe", "random"), "randomPassword"));
        verify(trainerDAO, never()).update(any());
    }

    @Test
    public void update_updatesTrainer_success() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));
        when(trainerDAO.update(any(Trainer.class))).thenAnswer(inv -> inv.getArgument(0));
        when(trainingTypeDAO.findById(1L)).thenReturn(Optional.of(new TrainingType(1L, "Yoga")));

        trainer.setFirstName("Test");
        trainer.setLastName("Test");

        Trainer result = trainerService.update(new Credentials(trainer.getUsername(), trainer.getPassword()), trainer);

        assertTrue(result.getIsActive());
        assertEquals("Test", result.getFirstName());
        assertEquals("Test", result.getLastName());
        verify(trainerDAO).update(trainer);
        verify(authenticationService).check(new Credentials(trainer.getUsername(), trainer.getPassword()));
    }

    @Test
    public void update_throwsException() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));
        when(trainingTypeDAO.findById(99L)).thenReturn(Optional.empty());

        trainer.setSpecialization(new TrainingType(99L, "Cross-fit"));
        assertThrows(IllegalArgumentException.class, () -> trainerService.update(new Credentials(trainer.getUsername(), trainer.getPassword()), trainer));
        verify(trainerDAO, never()).update(trainer);
    }

    @Test
    public void update_blankFirstName_throwsException() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));

        trainer.setFirstName("");

        assertThrows(IllegalArgumentException.class, () -> trainerService.update(new Credentials(trainer.getUsername(), trainer.getPassword()), trainer));
        verify(trainerDAO, never()).update(any());
    }

    @Test
    public void update_trainerNotFound_throwsException() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.empty());

        trainer.setFirstName("Test");

        assertThrows(IllegalArgumentException.class, () -> trainerService.update(new Credentials(trainer.getUsername(), trainer.getPassword()), trainer));
        verify(trainerDAO, never()).update(any());
    }

    @Test
    public void activateTrainer_success() {
        trainer.setIsActive(false);
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));

        trainerService.activate(new Credentials(trainer.getUsername(), trainer.getPassword()));

        assertTrue(trainer.getIsActive());
        verify(trainerDAO).update(trainer);
    }

    @Test
    public void activateTrainer_throwsException() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));

        assertThrows(IllegalStateException.class, () -> trainerService.activate(new Credentials(trainer.getUsername(), trainer.getPassword())));
        verify(trainerDAO, never()).update(any());
    }

    @Test
    public void deactivateTrainer_success() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));

        trainerService.deactivate(new Credentials(trainer.getUsername(), trainer.getPassword()));

        assertFalse(trainer.getIsActive());
        verify(trainerDAO).update(trainer);
    }

    @Test
    public void deactivateTrainer_throwsException() {
        trainer.setIsActive(false);
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));

        assertThrows(IllegalStateException.class, () -> trainerService.deactivate(new Credentials(trainer.getUsername(), trainer.getPassword())));
        verify(trainerDAO, never()).update(any());
    }

    @Test
    public void findById_trainerFound() {
        when(trainerDAO.findById(any(Long.class))).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = trainerService.findById(trainer.getId());

        assertTrue(result.isPresent());
        verify(trainerDAO).findById(trainer.getId());
    }

    @Test
    public void findById_trainerNotFound() {
        when(trainerDAO.findById(any(Long.class))).thenReturn(Optional.empty());
        Long input = 2L;

        Optional<Trainer> result = trainerService.findById(input);

        assertFalse(result.isPresent());
        verify(trainerDAO).findById(input);
    }

    @Test
    public void findAll_success() {
        when(trainerDAO.findAll()).thenReturn(new ArrayList<>());

        List<Trainer> result = trainerService.findAll();

        assertTrue(result.isEmpty());
        verify(trainerDAO).findAll();
    }

    @Test
    public void getUnassignedTrainers() {
        when(trainerDAO.findUnassignedTrainers(any(String.class))).thenReturn(new ArrayList<>());

        List<Trainer> result = trainerService.getUnassignedTrainers(new Credentials("John.Doe", "randomPass"));

        assertTrue(result.isEmpty());
        verify(authenticationService).check(new Credentials("John.Doe", "randomPass"));
        verify(trainerDAO).findUnassignedTrainers("John.Doe");
    }
}
