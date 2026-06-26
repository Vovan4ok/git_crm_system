package org.volodymyrzganiaiko.gym.crm.system;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;
import org.volodymyrzganiaiko.gym.crm.system.service.impl.TrainerServiceImpl;
import org.volodymyrzganiaiko.gym.crm.system.utils.CredentialsGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {
    @Mock
    CredentialsGenerator credentialsGenerator;
    
    @Mock
    TrainerDAO trainerDAO;
    
    @InjectMocks
    TrainerServiceImpl  trainerService;
    
    @Test
    public void testCreateTrainer() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setSpecialization(new TrainingType("Joga"));
        when(trainerDAO.save(trainer)).thenReturn(trainer);
        when(credentialsGenerator.generateUsername(trainer)).thenReturn("John.Doe");
        when(credentialsGenerator.generatePassword()).thenReturn(UUID.randomUUID().toString());
        trainer = trainerService.create(trainer);
        verify(trainerDAO).save(trainer);
        verify(credentialsGenerator).generateUsername(trainer);
        verify(credentialsGenerator).generatePassword();
        assertNotNull(trainer.getUserId());
        assertNotNull(trainer.getUsername());
        assertNotNull(trainer.getPassword());
        assertTrue(trainer.isActive());
    }
    
    @Test
    public void  testUpdateTrainer() {
        Trainer trainer = new Trainer("John", "Doe", "John.Doe", "random", true, new TrainingType("Joga"), UUID.randomUUID());
        when(trainerDAO.findById(trainer.getUserId())).thenReturn(Optional.of(trainer));
        when(trainerDAO.update(any())).thenReturn(true);
        trainer.setSpecialization(new TrainingType("Cross-fit"));
        assertTrue(trainerService.update(trainer));
        assertTrue(trainerService.findById(trainer.getUserId()).isPresent());
        assertEquals("Cross-fit", trainerService.findById(trainer.getUserId()).get().getSpecialization().getTrainingTypeName());
    }
    
    @Test
    public void testUpdateTrainerNotFound() {
        Trainer trainer = new Trainer("John", "Doe", "John.Doe", "random", true, new TrainingType("Joga"), UUID.randomUUID());
        assertFalse(trainerService.update(trainer));
        verify(trainerDAO).findById(trainer.getUserId());
        verify(trainerDAO, never()).update(any());
    }

    @Test
    public void testFindtrainer() {
        Trainer trainer = new Trainer("John", "Doe", "John.Doe", "random", true, new TrainingType("Joga"), UUID.randomUUID());
        when(trainerDAO.findById(trainer.getUserId())).thenReturn(Optional.of(trainer));
        Optional<Trainer> trainerFound = trainerService.findById(trainer.getUserId());
        assertTrue(trainerFound.isPresent());
        assertEquals("Joga", trainerFound.get().getSpecialization().getTrainingTypeName());
        verify(trainerDAO).findById(trainer.getUserId());
    }

    @Test
    public void testFindtrainerNotFound() {
        UUID id = UUID.randomUUID();
        assertFalse(trainerService.findById(id).isPresent());
        verify(trainerDAO).findById(id);
    }

    @Test
    public void testFindAllTrainer() {
        List<Trainer> trainers = new ArrayList<>();
        Trainer trainer = new Trainer("John", "Doe", "John.Doe", "random", true, new TrainingType("Joga"), UUID.randomUUID());
        Trainer trainer2 = new Trainer("John", "Doe", "John.Doe.1", "random", true, new TrainingType("Cross-fit"), UUID.randomUUID());
        trainers.add(trainer);
        trainers.add(trainer2);
        when(trainerDAO.findAll()).thenReturn(trainers);
        List<Trainer> result =  trainerService.findAll();
        assertFalse(result.isEmpty());
        assertEquals(2,  result.size());
        verify(trainerDAO).findAll();
    }
    
}
