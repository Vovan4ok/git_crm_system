package org.volodymyrzganiaiko.gym.crm.system.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.volodymyrzganiaiko.gym.crm.system.dao.UserDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from User u where u.username = :username", User.class).setParameter("username", username).uniqueResultOptional();
    }

    @Override
    public List<User> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from User", User.class).list();
    }
}
