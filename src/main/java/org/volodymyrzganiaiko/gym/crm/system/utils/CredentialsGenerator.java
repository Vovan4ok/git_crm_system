package org.volodymyrzganiaiko.gym.crm.system.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.volodymyrzganiaiko.gym.crm.system.dao.TraineeDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CredentialsGenerator {
    private final SecureRandom secureRandom = new SecureRandom();
    private static final short PASSWORD_LENGTH = 10;
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Logger log = LoggerFactory.getLogger(CredentialsGenerator.class);
    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;

    @Autowired
    public CredentialsGenerator(TraineeDAO traineeDAO, TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
        this.traineeDAO = traineeDAO;
    }

    public String generateUsername(User user) {
        Set<String> usernames = Stream.concat(traineeDAO.findAll().stream(), trainerDAO.findAll().stream()).map(User::getUsername).collect(Collectors.toSet());
        log.debug("[CredentialsGenerator]: Generating username for user {}", user);
        String requiredUsername = user.getFirstName() + "." + user.getLastName();
        if (!usernames.contains(requiredUsername)) {
            return requiredUsername;
        } else {
            int count = 1;
            while (true) {
                String base = requiredUsername + "." + count;
                if (!usernames.contains(base)) {
                    return base;
                }
                count++;
            }
        }
    }

    public String generatePassword() {
        log.debug("[CredentialsGenerator]: Generating password");
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = secureRandom.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(index));
        }
        return sb.toString();
    }
}
