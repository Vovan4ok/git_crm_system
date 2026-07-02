package org.volodymyrzganiaiko.gym.crm.system.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainingDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Training;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainingDAOImpl implements TrainingDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Training save(Training training) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(training);
        return training;
    }

    @Override
    public Optional<Training> findById(Long trainingId) {
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(Training.class, trainingId));
    }

    @Override
    public List<Training> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Training", Training.class).list();
    }

    @Override
    public List<Training> findTraineeTrainings(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerUsername, String trainingTypeName) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Training> cr =  cb.createQuery(Training.class);
        Root<Training> root = cr.from(Training.class);
        List<Predicate> predicates = new ArrayList<>();
        if (traineeUsername != null) {
            predicates.add(cb.equal(root.get("trainee").get("user").get("username"), traineeUsername));
        }
        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("trainingDate"), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("trainingDate"), toDate));
        }
        if (trainerUsername != null) {
            predicates.add(cb.equal(root.get("trainer").get("user").get("username"), trainerUsername));
        }
        if (trainingTypeName != null) {
            predicates.add(cb.equal(root.get("trainingType").get("trainingTypeName"), trainingTypeName));
        }
        cr.select(root).where(predicates.toArray(new Predicate[0]));
        return session.createQuery(cr).getResultList();
    }

    @Override
    public List<Training> findTrainerTrainings(String trainerUsername, LocalDate fromDate, LocalDate toDate, String traineeUsername) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Training> cr =  cb.createQuery(Training.class);
        Root<Training> root = cr.from(Training.class);
        List<Predicate> predicates = new ArrayList<>();
        if (traineeUsername != null) {
            predicates.add(cb.equal(root.get("trainee").get("user").get("username"), traineeUsername));
        }
        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("trainingDate"), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("trainingDate"), toDate));
        }
        if (trainerUsername != null) {
            predicates.add(cb.equal(root.get("trainer").get("user").get("username"), trainerUsername));
        }
        cr.select(root).where(predicates.toArray(new Predicate[0]));
        return session.createQuery(cr).getResultList();
    }
}
