package org.volodymyrzganiaiko.gym.crm.system.config;

import org.junit.jupiter.api.Test;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.Training;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GymSystemBeanPostProcessorTest {

    private void setField(Object target, String fieldName, String value) throws Exception {
        Field field = GymSystemBeanPostProcessor.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    public void loadsValidTraineesAndSkipsBadRow() throws Exception {
        GymSystemBeanPostProcessor bpp = new GymSystemBeanPostProcessor();
        setField(bpp, "traineeStoragePath", "test-trainees.csv");
        Map<UUID, Trainee> storage = new HashMap<>();

        bpp.postProcessAfterInitialization(storage, "traineeStorage");

        assertEquals(2, storage.size());
        assertTrue(storage.containsKey(UUID.fromString("11111111-1111-1111-1111-111111111111")));
        assertEquals("John", storage.get(UUID.fromString("11111111-1111-1111-1111-111111111111")).getFirstName());
    }

    @Test
    public void loadsValidTrainersAndSkipsBadRow() throws Exception {
        GymSystemBeanPostProcessor bpp = new GymSystemBeanPostProcessor();
        setField(bpp, "trainerStoragePath", "test-trainers.csv");
        Map<UUID, Trainer> storage = new HashMap<>();

        bpp.postProcessAfterInitialization(storage, "trainerStorage");

        assertEquals(2, storage.size());
        assertTrue(storage.containsKey(UUID.fromString("33333333-3333-3333-3333-333333333333")));
        assertEquals("Mike", storage.get(UUID.fromString("33333333-3333-3333-3333-333333333333")).getFirstName());
    }

    @Test
    public void loadsValidTrainingsAndSkipsBadRow() throws Exception {
        GymSystemBeanPostProcessor bpp = new GymSystemBeanPostProcessor();
        setField(bpp, "trainingStoragePath", "test-trainings.csv");
        Map<UUID, Training> storage = new HashMap<>();

        bpp.postProcessAfterInitialization(storage, "trainingStorage");

        assertEquals(2, storage.size());
        assertTrue(storage.containsKey(UUID.fromString("55555555-5555-5555-5555-555555555555")));
        assertEquals("Morning Cross-fit", storage.get(UUID.fromString("55555555-5555-5555-5555-555555555555")).getTrainingName());
    }

    @Test
    public void unknownBeanReturnsSameBeanUnchanged() {
        GymSystemBeanPostProcessor bpp = new GymSystemBeanPostProcessor();
        Map<String, Trainee> storage = new HashMap<>();

        Object result = bpp.postProcessAfterInitialization(storage, "unknownPath");

        assertSame(storage, result);
        assertTrue(storage.isEmpty());
    }
}