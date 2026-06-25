package org.volodymyrzganiaiko.gym.crm.system;

import org.junit.jupiter.api.Test;
import org.volodymyrzganiaiko.gym.crm.system.dao.TraineeDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.impl.TraineeDAOImpl;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeDAOImplTest {
    @Test
    public void testSave() {
        TraineeDAO dao = prepareEmptyDAO();
        Trainee input = new Trainee("John", "Doe", "John.Doe", "random", true, LocalDate.parse("2006-09-05"), "Test address", UUID.randomUUID());
        Trainee result = dao.save(input);
        assertSame(input, result);
        assertSame(input, dao.findById(input.getUserId()).get());
    }

    @Test
    public void testUpdatePresentTrainee() {
        TraineeDAO dao = prepareNonEmptyDAO();
        Trainee input = dao.findAll().get(0);
        input.setAddress("Test address 2");
        boolean result = dao.update(input);
        assertTrue(result);
    }

    @Test
    public void testUpdateNotFoundTrainee() {
        TraineeDAO dao = prepareNonEmptyDAO();
        Trainee input = new Trainee("Random", "Random", "Random.Random", "random", true, LocalDate.parse("2004-09-09"), "Test address", UUID.randomUUID());
        boolean result = dao.update(input);
        assertNotEquals(true, result);
    }

    @Test
    public void testDeletePresentUser() {
        TraineeDAO dao = prepareNonEmptyDAO();
        Trainee trainee = dao.findAll().get(0);
        boolean result = dao.delete(trainee.getUserId());
        assertTrue(result);
    }

    @Test
    public void testDeleteNotFoundUser() {
        TraineeDAO dao = prepareNonEmptyDAO();
        boolean result = dao.delete(UUID.randomUUID());
        assertFalse(result);
    }

    @Test
    public void testFindByIdPresentTrainee() {
        TraineeDAO dao = prepareNonEmptyDAO();
        Trainee input = dao.findAll().get(0);
        Optional<Trainee> result = dao.findById(input.getUserId());
        assertTrue(result.isPresent());
        assertSame(input, result.get());
    }

    @Test
    public void testFindByIdNotFoundTrainee() {
        TraineeDAO dao = prepareEmptyDAO();
        Optional<Trainee> result = dao.findById(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindAll() {
        TraineeDAO dao = prepareNonEmptyDAO();
        List<Trainee> result = dao.findAll();
        assertEquals(2, result.size());
    }

    private TraineeDAO prepareEmptyDAO() {
        TraineeDAOImpl dao = new TraineeDAOImpl();
        dao.setStorage(new HashMap<>());
        return dao;
    }

    private TraineeDAO prepareNonEmptyDAO() {
        TraineeDAOImpl dao = new TraineeDAOImpl();
        Trainee trainee = new Trainee("John", "Doe", "John.Doe", "random", true, LocalDate.parse("2011-09-08"), "Test address", UUID.randomUUID());
        Trainee trainee2 = new Trainee("John", "Doe", "John.Doe.1", "random", true, LocalDate.parse("2009-01-01"), "Test address", UUID.randomUUID());
        Map<UUID, Trainee> storage = new HashMap<>();
        storage.put(trainee.getUserId(), trainee);
        storage.put(trainee2.getUserId(), trainee2);
        dao.setStorage(storage);
        return dao;
    }
}
