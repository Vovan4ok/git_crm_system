package org.volodymyrzganiaiko.gym.crm.system.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.volodymyrzganiaiko.gym.crm.system.dao.TraineeDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.service.CredentialsService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {
    @Mock
    CredentialsService credentialsService;

    @Mock
    TraineeDAO traineeDAO;

    @InjectMocks
    TraineeServiceImpl traineeService;

    @Test
    public void testCreateTrainee() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setDateOfBirth(LocalDate.parse("2003-11-08"));
        trainee.setAddress("Test address");
        when(credentialsService.generateUsername(trainee)).thenReturn("John.Doe");
        when(credentialsService.generatePassword()).thenReturn(UUID.randomUUID().toString());
        when(traineeDAO.save(trainee)).thenAnswer(inv -> inv.getArgument(0));
        trainee = traineeService.create(trainee);
        verify(traineeDAO).save(trainee);
        verify(credentialsService).generateUsername(trainee);
        verify(credentialsService).generatePassword();
        assertNotNull(trainee.getUserId());
        assertNotNull(trainee.getUsername());
        assertNotNull(trainee.getPassword());
        assertTrue(trainee.isActive());
    }

    @Test
    public void testUpdateTrainee() {
        Trainee trainee = new Trainee("John", "Doe", "John.Doe", "random", true, LocalDate.parse("2011-09-09"), "Test address", UUID.randomUUID());
        when(traineeDAO.findById(trainee.getUserId())).thenReturn(Optional.of(trainee));
        when(traineeDAO.update(any())).thenReturn(true);
        trainee.setAddress("Test address 2");
        assertTrue(traineeService.update(trainee));
        assertTrue(traineeService.findById(trainee.getUserId()).isPresent());
        assertEquals("Test address 2", traineeService.findById(trainee.getUserId()).get().getAddress());
    }

    @Test
    public void testUpdateTraineeNotFound() {
        Trainee trainee = new Trainee("John", "Doe", "John.Doe", "random", true, LocalDate.parse("2011-09-09"), "Test address", UUID.randomUUID());
        assertFalse(traineeService.update(trainee));
        verify(traineeDAO).findById(trainee.getUserId());
        verify(traineeDAO, never()).update(any());
    }

    @Test
    public void testDeleteTrainee() {
        Trainee trainee = new Trainee("John", "Doe", "John.Doe", "random", true, LocalDate.parse("2011-09-09"), "Test address", UUID.randomUUID());
        when(traineeDAO.delete(trainee.getUserId())).thenReturn(true);
        assertTrue(traineeService.delete(trainee.getUserId()));
        verify(traineeDAO).delete(trainee.getUserId());
    }

    @Test
    public void testDeleteTraineeNotFound() {
        UUID id = UUID.randomUUID();
        when(traineeDAO.delete(any())).thenReturn(false);
        assertFalse(traineeService.delete(id));
        verify(traineeDAO).delete(id);
    }

    @Test
    public void testFindTrainee() {
        Trainee trainee = new Trainee("John", "Doe", "John.Doe", "random", true, LocalDate.parse("2011-09-09"), "Test address", UUID.randomUUID());
        when(traineeDAO.findById(trainee.getUserId())).thenReturn(Optional.of(trainee));
        Optional<Trainee> traineeFound = traineeService.findById(trainee.getUserId());
        assertTrue(traineeFound.isPresent());
        assertEquals("Test address", traineeFound.get().getAddress());
        verify(traineeDAO).findById(trainee.getUserId());
    }

    @Test
    public void testFindTraineeNotFound() {
        UUID id = UUID.randomUUID();
        assertFalse(traineeService.findById(id).isPresent());
        verify(traineeDAO).findById(id);
    }

    @Test
    public void testFindAllTrainee() {
        List<Trainee> trainees = new ArrayList<>();
        Trainee trainee = new Trainee("John", "Doe", "John.Doe", "random", true, LocalDate.parse("2011-09-09"), "Test address", UUID.randomUUID());
        Trainee trainee2 = new Trainee("John", "Doe", "John.Doe.1", "random", true, LocalDate.parse("2011-09-09"), "Test address", UUID.randomUUID());
        trainees.add(trainee);
        trainees.add(trainee2);
        when(traineeDAO.findAll()).thenReturn(trainees);
        List<Trainee> result =  traineeService.findAll();
        assertFalse(result.isEmpty());
        assertEquals(2,  result.size());
        verify(traineeDAO).findAll();
    }
}
