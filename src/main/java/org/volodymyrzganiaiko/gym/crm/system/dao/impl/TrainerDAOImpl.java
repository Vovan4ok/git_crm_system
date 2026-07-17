package org.volodymyrzganiaiko.gym.crm.system.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainerDAOImpl implements TrainerDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Trainer save(Trainer trainer) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(trainer);
        return trainer;
    }

    @Override
    public Trainer update(Trainer trainer) {
        Session session = sessionFactory.getCurrentSession();
        return (Trainer) session.merge(trainer);
    }

    @Override
    public Optional<Trainer> findById(Long trainerId) {
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(Trainer.class, trainerId));
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Trainer t where t.username = :username", Trainer.class).setParameter("username", username).uniqueResultOptional();
    }

    @Override
    public List<Trainer> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Trainer", Trainer.class).list();
    }

    @Override
    public List<Trainer> findUnassignedTrainers(String traineeUsername) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(
                "select t from Trainer t where t.isActive = true and t.id not in " +
                        "(select tr.id from Trainee e join e.trainers tr where e.username = :username)",
                Trainer.class).setParameter("username", traineeUsername).list();
    }
}
