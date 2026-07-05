package org.volodymyrzganiaiko.gym.crm.system.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainingTypeDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;
import org.volodymyrzganiaiko.gym.crm.system.domain.User;
import org.volodymyrzganiaiko.gym.crm.system.exceptions.AuthenticationException;
import org.volodymyrzganiaiko.gym.crm.system.service.impl.TrainerServiceImpl;

import java.util.ArrayList;
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

    @InjectMocks
    private TrainerServiceImpl trainerService;

    Trainer trainer;

    @BeforeEach
    public void setUp() {
        User user = new User("John", "Doe", "John.Doe", "random", true);
        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUser(user);
        trainer.setSpecialization(new TrainingType(1L, "Yoga"));
    }

    @Test
    public void create_generatesCredentials_andSaves() {
        doAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setUsername("John.Doe");
            u.setPassword("random");
            u.setIsActive(true);
            return null;
        }).when(credentialsService).assignCredentials(any(User.class));
        when(trainerDAO.save(any(Trainer.class))).thenAnswer(inv -> inv.getArgument(0));
        when(trainingTypeDAO.findById(1L)).thenReturn(Optional.of(new TrainingType(1L, "Yoga")));

        Trainer result = trainerService.create(trainer);

        assertTrue(result.getUser().getIsActive());
        assertEquals("John.Doe", result.getUser().getUsername());
        assertEquals("random", result.getUser().getPassword());
        verify(trainerDAO).save(trainer);
        verifyNoInteractions(authenticationService);
        verify(credentialsService).assignCredentials(trainer.getUser());
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

        Optional<Trainer> result = trainerService.findByUsername(trainer.getUser().getUsername(), trainer.getUser().getPassword());

        assertTrue(result.isPresent());
        verify(authenticationService).check(trainer.getUser().getUsername(), trainer.getUser().getPassword());
    }

    @Test
    public void findByUsername_negativeCase_throwsException() {
        doThrow(new AuthenticationException("Some message")).when(authenticationService).check(any(String.class), any(String.class));

        assertThrows(AuthenticationException.class, () -> trainerService.findByUsername(trainer.getUser().getUsername(), trainer.getUser().getPassword()));
        verifyNoInteractions(trainerDAO);
    }

    @Test
    public void changePassword_success() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));

        trainerService.changePassword("John.Doe", "random", "random1");

        assertEquals("random1", trainer.getUser().getPassword());
        verify(authenticationService).check("John.Doe", "random");
        verify(trainerDAO).update(trainer);
    }

    @Test
    public void changePassword_throwsException() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));

        assertThrows(IllegalArgumentException.class, () -> trainerService.changePassword("John.Doe", "randomPass", " "));
        verify(trainerDAO, never()).update(any());
    }

    @Test
    public void changePassword_trainerNotFound_throwsException() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> trainerService.changePassword("John.Doe", "random", "randomPassword"));
        verify(trainerDAO, never()).update(any());
    }

    @Test
    public void update_updatesTrainer_success() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));
        when(trainerDAO.update(any(Trainer.class))).thenAnswer(inv -> inv.getArgument(0));
        when(trainingTypeDAO.findById(1L)).thenReturn(Optional.of(new TrainingType(1L, "Yoga")));

        trainer.getUser().setFirstName("Test");
        trainer.getUser().setLastName("Test");

        Trainer result = trainerService.update(trainer.getUser().getUsername(), trainer.getUser().getPassword(), trainer);

        assertTrue(result.getUser().getIsActive());
        assertEquals("Test", result.getUser().getFirstName());
        assertEquals("Test", result.getUser().getLastName());
        verify(trainerDAO).update(trainer);
        verify(authenticationService).check(trainer.getUser().getUsername(), trainer.getUser().getPassword());
    }

    @Test
    public void update_throwsException() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));
        when(trainingTypeDAO.findById(99L)).thenReturn(Optional.empty());

        trainer.setSpecialization(new TrainingType(99L, "Cross-fit"));
        assertThrows(IllegalArgumentException.class, () -> trainerService.update(trainer.getUser().getUsername(), trainer.getUser().getPassword(), trainer));
        verify(trainerDAO, never()).update(trainer);
    }

    @Test
    public void update_blankFirstName_throwsException() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));

        trainer.getUser().setFirstName("");

        assertThrows(IllegalArgumentException.class, () -> trainerService.update(trainer.getUser().getUsername(), trainer.getUser().getPassword(), trainer));
        verify(trainerDAO, never()).update(any());
    }

    @Test
    public void update_trainerNotFound_throwsException() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.empty());

        trainer.getUser().setFirstName("Test");

        assertThrows(IllegalArgumentException.class, () -> trainerService.update(trainer.getUser().getUsername(), trainer.getUser().getPassword(), trainer));
        verify(trainerDAO, never()).update(any());
    }

    @Test
    public void activateTrainer_success() {
        trainer.getUser().setIsActive(false);
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));

        trainerService.activate(trainer.getUser().getUsername(), trainer.getUser().getPassword());

        assertTrue(trainer.getUser().getIsActive());
        verify(trainerDAO).update(trainer);
    }

    @Test
    public void activateTrainer_throwsException() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));

        assertThrows(IllegalStateException.class, () -> trainerService.activate(trainer.getUser().getUsername(), trainer.getUser().getPassword()));
        verify(trainerDAO, never()).update(any());
    }

    @Test
    public void deactivateTrainer_success() {
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));

        trainerService.deactivate(trainer.getUser().getUsername(), trainer.getUser().getPassword());

        assertFalse(trainer.getUser().getIsActive());
        verify(trainerDAO).update(trainer);
    }

    @Test
    public void deactivateTrainer_throwsException() {
        trainer.getUser().setIsActive(false);
        when(trainerDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));

        assertThrows(IllegalStateException.class, () -> trainerService.deactivate(trainer.getUser().getUsername(), trainer.getUser().getPassword()));
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

        List<Trainer> result = trainerService.getUnassignedTrainers("John.Doe", "randomPass");

        assertTrue(result.isEmpty());
        verify(authenticationService).check("John.Doe", "randomPass");
        verify(trainerDAO).findUnassignedTrainers("John.Doe");
    }
}
