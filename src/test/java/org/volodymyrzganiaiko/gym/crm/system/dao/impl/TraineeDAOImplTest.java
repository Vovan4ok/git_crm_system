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
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoTestConfig.class)
@Transactional
public class TraineeDAOImplTest {
    @Autowired
    private TraineeDAO traineeDAO;

    @Autowired
    private SessionFactory sessionFactory;

    private Trainee trainee;

    @BeforeEach
    public void setUpTrainee() {
        User user = new User(null, "John", "Doe", "John.Doe", "random", true);
        trainee = new Trainee();
        trainee.setUser(user);
        trainee.setAddress("Test address");
        trainee.setDateOfBirth(LocalDate.parse("2003-11-08"));
    }

    private void flushAndClear() {
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
    }

    @Test
    public void save_success() {
        Trainee result = traineeDAO.save(trainee);
        flushAndClear();

        Trainee reloaded = traineeDAO.findById(result.getId()).orElseThrow();
        assertNotNull(reloaded.getUser().getId());
        assertEquals("John.Doe", reloaded.getUser().getUsername());
        assertEquals("Test address", reloaded.getAddress());
    }

    @Test
    public void findByUsername_found() {
        Trainee savedTrainee = traineeDAO.save(trainee);
        flushAndClear();

        Optional<Trainee> foundTrainee = traineeDAO.findByUsername("John.Doe");
        assertTrue(foundTrainee.isPresent());
        assertEquals(foundTrainee.get().getId(), savedTrainee.getId());
    }

    @Test
    public void findByUsername_notFound() {
        Optional<Trainee> foundTrainee = traineeDAO.findByUsername("Test.Test");
        assertFalse(foundTrainee.isPresent());
    }

    @Test
    public void update_success() {
        trainee = traineeDAO.save(trainee);
        flushAndClear();

        trainee.setAddress("Test address modified");
        trainee = traineeDAO.update(trainee);
        flushAndClear();

        Trainee reloaded = traineeDAO.findById(trainee.getId()).orElseThrow();

        assertNotNull(reloaded);
        assertEquals("Test address modified", reloaded.getAddress());
    }

    @Test
    public void deleteByUsername_success() {
        trainee = traineeDAO.save(trainee);
        flushAndClear();

        boolean result = traineeDAO.deleteByUsername("John.Doe");
        assertTrue(result);

        Optional<Trainee> foundTraineeOpt = traineeDAO.findByUsername("John.Doe");
        assertTrue(foundTraineeOpt.isEmpty());

        Long count = sessionFactory.getCurrentSession().createQuery("select count(u) from User u where u.username = :username", Long.class).setParameter("username", "John.Doe").uniqueResult();
        assertEquals(0L, count);
    }
}
