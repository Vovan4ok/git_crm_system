package org.volodymyrzganiaiko.gym.crm.system.dao.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TrainerDAOImplTest {
    TrainerDAOImpl dao;

    @BeforeEach
    public void initDAO() {
        dao = new TrainerDAOImpl();
        Trainer trainer = new Trainer("John", "Doe", "John.Doe", "random", true, new TrainingType("Joga"), UUID.randomUUID());
        Trainer trainer2 = new Trainer("John", "Doe", "John.Doe.1", "random", true, new TrainingType("Cross-fit"), UUID.randomUUID());
        Map<UUID, Trainer> storage = new HashMap<>();
        storage.put(trainer.getUserId(), trainer);
        storage.put(trainer2.getUserId(), trainer2);
        dao.setStorage(storage);
    }

    @Test
    public void testSave() {
        Trainer input = new Trainer("John", "Doe", "John.Doe", "random", true, new TrainingType("Joga"), UUID.randomUUID());
        Trainer result = dao.save(input);
        assertSame(input, result);
        assertSame(input, dao.findById(result.getUserId()).get());
    }

    @Test
    public void testUpdatePresentTrainer() {
        Trainer input = dao.findAll().get(0);
        input.setSpecialization(new TrainingType("Test"));
        boolean result = dao.update(input);
        assertTrue(result);
    }

    @Test
    public void testUpdateNotFoundTrainer() {
        Trainer input = new Trainer("Random", "Random", "Random.Random", "random", true, new TrainingType("Test"), UUID.randomUUID());
        boolean result = dao.update(input);
        assertFalse(result);
    }

    @Test
    public void testFindByIdPresentTrainer() {
        Trainer input = dao.findAll().get(0);
        Optional<Trainer> result = dao.findById(input.getUserId());
        assertTrue(result.isPresent());
        assertSame(input, result.get());
    }

    @Test
    public void testFindByIdNotFoundTrainer() {
        Optional<Trainer> result = dao.findById(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindAll() {
        List<Trainer> result = dao.findAll();
        assertEquals(2, result.size());
    }
}
