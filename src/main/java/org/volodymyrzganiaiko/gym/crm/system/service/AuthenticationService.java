package org.volodymyrzganiaiko.gym.crm.system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.volodymyrzganiaiko.gym.crm.system.dao.UserDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.User;
import org.volodymyrzganiaiko.gym.crm.system.dto.Credentials;
import org.volodymyrzganiaiko.gym.crm.system.exception.AuthenticationException;

import java.util.Optional;

@Service
public class AuthenticationService {
    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;

    private static final Logger log =  LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public void check(Credentials credentials) {
        if(!matches(credentials.username(), credentials.password())) {
            log.warn("The authentication for user with username {} wasn't successful", credentials.username());
            throw new AuthenticationException("Wrong username or password");
        }
    }

    private boolean matches(String username, String password) {
        Optional<User> foundUserOpt = userDAO.findByUsername(username);
        if (foundUserOpt.isPresent()) {
            User foundUser = foundUserOpt.get();
            return passwordEncoder.matches(password, foundUser.getPassword());
        }
        return false;
    }
}
