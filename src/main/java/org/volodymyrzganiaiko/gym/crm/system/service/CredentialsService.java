package org.volodymyrzganiaiko.gym.crm.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.volodymyrzganiaiko.gym.crm.system.dao.TraineeDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.User;
import org.volodymyrzganiaiko.gym.crm.system.utils.CredentialsGenerator;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CredentialsService {
    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;
    private final CredentialsGenerator credentialsGenerator;

    @Autowired
    public CredentialsService(TraineeDAO traineeDAO, TrainerDAO trainerDAO, CredentialsGenerator credentialsGenerator) {
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
        this.credentialsGenerator = credentialsGenerator;
    }

    public String generateUsername(User user) {
        Set<String> existingUsernames = Stream.concat(traineeDAO.findAll().stream(), trainerDAO.findAll().stream()).map(User::getUsername).collect(Collectors.toSet());
        return credentialsGenerator.generateUsername(user, existingUsernames);
    }

    public String generatePassword() {
        return credentialsGenerator.generatePassword();
    }

}
