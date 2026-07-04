package org.volodymyrzganiaiko.gym.crm.system.dao.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.volodymyrzganiaiko.gym.crm.system.dao.DaoTestConfig;
import org.volodymyrzganiaiko.gym.crm.system.dao.TraineeDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;
import org.volodymyrzganiaiko.gym.crm.system.domain.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoTestConfig.class)
@Transactional
public class TrainerDAOImplTest {
    @Autowired
    private TrainerDAO trainerDAO;

    @Autowired
    private TraineeDAO traineeDAO;

    @Autowired
    SessionFactory sessionFactory;

    Trainer trainer;

    @BeforeEach
    public void setUpTrainer() {
        User user = new User(null, "John", "Doe", "John.Doe", "random", true);
        trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(new TrainingType(1L, "Yoga"));
        trainer.setTrainees(new HashSet<>());
    }

    private void flushAndClear() {
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
    }

    @Test
    public void save_success() {
        trainer = trainerDAO.save(trainer);
        flushAndClear();

        Trainer reloaded = trainerDAO.findByUsername("John.Doe").orElseThrow();
        assertNotNull(reloaded.getUser().getId());
        assertEquals("John.Doe", reloaded.getUser().getUsername());
        assertEquals("Yoga", reloaded.getSpecialization().getTrainingTypeName());
    }

    @Test
    public void findByUsername_found() {
        Trainer savedTrainer = trainerDAO.save(trainer);
        flushAndClear();

        Optional<Trainer> foundTrainer = trainerDAO.findByUsername("John.Doe");
        assertTrue(foundTrainer.isPresent());
        assertEquals(foundTrainer.get().getId(), savedTrainer.getId());
    }

    @Test
    public void findByUsername_notFound() {
        Optional<Trainer> foundTrainer = trainerDAO.findByUsername("Test.Test");
        assertFalse(foundTrainer.isPresent());
    }

    @Test
    public void findUnassignedTrainers_Found() {
        Trainer trainer1 = new Trainer(null, new TrainingType(2L, "Cardio"), new User(null, "Bob", "Busy", "Bob.Busy", "random", true), new HashSet<>());
        Trainee trainee = new Trainee(null, LocalDate.parse("2011-08-11"), "Test address", new User(null, "Ann", "Free", "Ann.Free", "random", true), new HashSet<>());
        trainer = trainerDAO.save(trainer);
        trainerDAO.save(trainer1);
        trainee = traineeDAO.save(trainee);
        flushAndClear();

        trainee.setTrainers(Set.of(trainer));
        traineeDAO.update(trainee);
        flushAndClear();

        List<Trainer> result = trainerDAO.findUnassignedTrainers("Ann.Free");
        List<String> resultUsernames = result.stream().map(t -> t.getUser().getUsername()).toList();
        assertTrue(resultUsernames.contains("Bob.Busy"));
        assertFalse(resultUsernames.contains("John.Doe"));
    }

    @Test
    public void findUnassignedTrainers_NotFound() {
        Trainer trainer1 = new Trainer(null, new TrainingType(2L, "Cardio"), new User(null, "Bob", "Busy", "Bob.Busy", "random", true), new HashSet<>());
        Trainee trainee = new Trainee(null, LocalDate.parse("2011-08-11"), "Test address", new User(null, "Ann", "Free", "Ann.Free", "random", true), new HashSet<>());
        trainer = trainerDAO.save(trainer);
        trainer1 = trainerDAO.save(trainer1);
        trainee = traineeDAO.save(trainee);
        flushAndClear();

        trainee.setTrainers(Set.of(trainer, trainer1));
        traineeDAO.update(trainee);
        flushAndClear();

        List<Trainer> result = trainerDAO.findUnassignedTrainers("Ann.Free");
        List<String> resultUsernames = result.stream().map(t -> t.getUser().getUsername()).toList();
        assertTrue(resultUsernames.isEmpty());
    }
}
