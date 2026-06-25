package org.volodymyrzganiaiko.gym.crm.system;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainingDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Training;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;
import org.volodymyrzganiaiko.gym.crm.system.service.impl.TrainingServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
    @Mock
    TrainingDAO trainingDAO;

    @InjectMocks
    TrainingServiceImpl trainingService;

    @Test
    public void createTraining() {
        Training training = new Training();
        training.setTraineeId(UUID.randomUUID());
        training.setTrainerId(UUID.randomUUID());
        training.setTrainingType(new TrainingType("Joga"));
        training.setTrainingDate(LocalDate.parse("2026-06-25"));
        training.setTrainingDurationInMinutes(90);
        training.setTrainingName("Joga");
        when(trainingDAO.save(any())).thenReturn(training);
        training = trainingService.create(training);
        assertNotNull(training);
        assertNotNull(training.getTrainingId());
    }

    @Test
    public void testFindTrainee() {
        Training training = new Training(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), new TrainingType("Joga"), "Joga", LocalDate.parse("2026-06-25"), 90);
        when(trainingDAO.findById(training.getId())).thenReturn(Optional.of(training));
        Optional<Training> trainingFound = trainingService.findById(training.getId());
        assertTrue(trainingFound.isPresent());
        assertEquals("Joga", trainingFound.get().getTrainingName());
        verify(trainingDAO).findById(training.getId());
    }

    @Test
    public void testFindTraineeNotFound() {
        UUID id = UUID.randomUUID();
        assertFalse(trainingService.findById(id).isPresent());
        verify(trainingDAO).findById(id);
    }

    @Test
    public void testFindAllTrainee() {
        List<Training> trainings = new ArrayList<>();
        Training training = new Training(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), new TrainingType("Joga"), "Joga", LocalDate.parse("2026-06-25"), 90);
        Training training1 = new Training(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), new TrainingType("Cross-fit"), "Cross-fit", LocalDate.parse("2026-06-25"), 90);
        trainings.add(training);
        trainings.add(training1);
        when(trainingDAO.findAll()).thenReturn(trainings);
        List<Training> result = trainingService.findAll();
        assertFalse(result.isEmpty());
        assertEquals(2,  result.size());
        verify(trainingDAO).findAll();
    }
}
