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
    private TraineeDAO traineeDAO;
    private TrainerDAO trainerDAO;
    private CredentialsGenerator credentialsGenerator;

    @Autowired
    public void setTraineeDAO (TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    @Autowired
    public void setTrainerDAO (TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Autowired
    public void setCredentialsGenerator (CredentialsGenerator credentialsGenerator) {
        this.credentialsGenerator = credentialsGenerator;
    }

    public String generateUsername(User user) {
        Set<String> existingUsernames = Stream.concat(traineeDAO.findAll().stream().map(t -> t.getUser().getUsername()), trainerDAO.findAll().stream().map(t -> t.getUser().getUsername())).collect(Collectors.toSet());
        return credentialsGenerator.generateUsername(user, existingUsernames);
    }

    public String generatePassword() {
        return credentialsGenerator.generatePassword();
    }

    public void assignCredentials(User user) {
        user.setIsActive(true);
        user.setUsername(generateUsername(user));
        user.setPassword(generatePassword());
    }

}
