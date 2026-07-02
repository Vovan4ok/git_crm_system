package org.volodymyrzganiaiko.gym.crm.system.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainingTypeDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainingTypeDAOImpl implements TrainingTypeDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<TrainingType> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(TrainingType.class, id));
    }

    @Override
    public List<TrainingType> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from TrainingType", TrainingType.class).list();
    }
}
