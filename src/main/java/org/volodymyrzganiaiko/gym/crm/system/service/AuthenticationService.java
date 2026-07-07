package org.volodymyrzganiaiko.gym.crm.system.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.volodymyrzganiaiko.gym.crm.system.dao.TraineeDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.UserDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.User;
import org.volodymyrzganiaiko.gym.crm.system.exceptions.AuthenticationException;
import org.volodymyrzganiaiko.gym.crm.system.service.impl.TraineeServiceImpl;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthenticationService {
    private UserDAO userDAO;

    private static final Logger log =  LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transactional(readOnly = true)
    public void check(String username, String password) {
        if(!matches(username, password)) {
            log.warn("The authentication for user with username {} wasn't successful", username);
            throw new AuthenticationException("Wrong username or password");
        }
    }

    private boolean matches(String username, String password) {
        Optional<User> foundUserOpt = userDAO.findByUsername(username);
        if (foundUserOpt.isPresent()) {
            User foundUser = foundUserOpt.get();
            return Objects.equals(foundUser.getPassword(), password);
        }
        return false;
    }
}
