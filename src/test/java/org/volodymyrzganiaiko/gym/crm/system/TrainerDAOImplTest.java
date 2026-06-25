package org.volodymyrzganiaiko.gym.crm.system;


import org.junit.jupiter.api.Test;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.impl.TrainerDAOImpl;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TrainerDAOImplTest {
    @Test
    public void testSave() {
        TrainerDAO dao = prepareEmptyDAO();
        Trainer input = new Trainer("John", "Doe", "John.Doe", "random", true, new TrainingType("Joga"), UUID.randomUUID());
        Trainer result = dao.save(input);
        assertSame(input, result);
        assertSame(input, dao.findById(result.getId()).get());
    }

    @Test
    public void testUpdatePresentTrainer() {
        TrainerDAO dao = prepareNonEmptyDAO();
        Trainer input = dao.findAll().get(0);
        input.setSpecialization(new TrainingType("Test"));
        boolean result = dao.update(input);
        assertTrue(result);
    }

    @Test
    public void testUpdateNotFoundTrainer() {
        TrainerDAO dao = prepareNonEmptyDAO();
        Trainer input = new Trainer("Random", "Random", "Random.Random", "random", true, new TrainingType("Test"), UUID.randomUUID());
        boolean result = dao.update(input);
        assertNotEquals(true, result);
    }

    @Test
    public void testFindByIdPresentTrainer() {
        TrainerDAO dao = prepareNonEmptyDAO();
        Trainer input = dao.findAll().get(0);
        Optional<Trainer> result = dao.findById(input.getUserId());
        assertTrue(result.isPresent());
        assertSame(input, result.get());
    }

    @Test
    public void testFindByIdNotFoundTrainer() {
        TrainerDAO dao = prepareEmptyDAO();
        Optional<Trainer> result = dao.findById(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindAll() {
        TrainerDAO dao = prepareNonEmptyDAO();
        List<Trainer> result = dao.findAll();
        assertEquals(2, result.size());
    }

    private TrainerDAO prepareEmptyDAO() {
        TrainerDAOImpl dao = new TrainerDAOImpl();
        dao.setStorage(new HashMap<>());
        return dao;
    }

    private TrainerDAO prepareNonEmptyDAO() {
        TrainerDAOImpl dao = new TrainerDAOImpl();
        Trainer trainer = new Trainer("John", "Doe", "John.Doe", "random", true, new TrainingType("Joga"), UUID.randomUUID());
        Trainer trainer2 = new Trainer("John", "Doe", "John.Doe.1", "random", true, new TrainingType("Cross-fit"), UUID.randomUUID());
        Map<UUID, Trainer> storage = new HashMap<>();
        storage.put(trainer.getUserId(), trainer);
        storage.put(trainer2.getUserId(), trainer2);
        dao.setStorage(storage);
        return dao;
    }
}
