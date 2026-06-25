package org.volodymyrzganiaiko.gym.crm.system;

import org.junit.jupiter.api.Test;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainingDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.impl.TrainingDAOImpl;
import org.volodymyrzganiaiko.gym.crm.system.domain.Training;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingDAOImplTest {
    @Test
    public void testSave() {
        TrainingDAO dao = prepareEmptyDAO();
        Training input = new Training(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), new TrainingType("Joga"), "Joga", LocalDate.parse("2026-06-26"), 90);
        Training result = dao.save(input);
        assertSame(input, result);
        assertSame(input, dao.findById(input.getTrainingId()).get());
    }

    @Test
    public void testFindByIdPresentTrainee() {
        TrainingDAO dao = prepareNonEmptyDAO();
        Training input = dao.findAll().get(0);
        Optional<Training> result = dao.findById(input.getTrainingId());
        assertTrue(result.isPresent());
        assertSame(input, result.get());
    }

    @Test
    public void testFindByIdNotFoundTrainee() {
        TrainingDAO dao = prepareEmptyDAO();
        Optional<Training> result = dao.findById(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindAll() {
        TrainingDAO dao = prepareNonEmptyDAO();
        List<Training> result = dao.findAll();
        assertEquals(2, result.size());
    }

    private TrainingDAO prepareEmptyDAO() {
        TrainingDAOImpl dao = new TrainingDAOImpl();
        dao.setStorage(new HashMap<>());
        return dao;
    }

    private TrainingDAO prepareNonEmptyDAO() {
        TrainingDAOImpl dao = new TrainingDAOImpl();
        Training training = new Training(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), new TrainingType("Joga"), "Joga", LocalDate.parse("2026-06-26"), 90);
        Training training1 = new Training(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), new TrainingType("Cross-fit"), "Cross-fit", LocalDate.parse("2026-06-27"), 60);
        Map<UUID, Training> storage = new HashMap<>();
        storage.put(training.getTrainingId(), training);
        storage.put(training1.getTrainingId(), training1);
        dao.setStorage(storage);
        return dao;
    }
}
