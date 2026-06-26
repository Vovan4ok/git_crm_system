package org.volodymyrzganiaiko.gym.crm.system;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.volodymyrzganiaiko.gym.crm.system.dao.TraineeDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;
import org.volodymyrzganiaiko.gym.crm.system.domain.User;
import org.volodymyrzganiaiko.gym.crm.system.utils.CredentialsGenerator;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CredentialsGeneratorTest {
    CredentialsGenerator credentialsGenerator;

    @BeforeEach
    public void initCredentialsGenerator() {
        credentialsGenerator = new CredentialsGenerator();
    }

    @Test
    public void testGenerateUsernameWithoutCollisions() {
        User input = new User();
        input.setFirstName("John");
        input.setLastName("Doe");
        String expected = "John.Doe";
        Set<String> existingUsernames = new HashSet<>();

        String result = credentialsGenerator.generateUsername(input, existingUsernames);

        assertEquals(expected, result);
    }

    @Test
    public void testGenerateUsernameWithCollisions() {
        User input = new User();
        input.setFirstName("John");
        input.setLastName("Doe");
        String expected = "John.Doe.1";
        Set<String> existingUsernames = Set.of("John.Doe");

        String result = credentialsGenerator.generateUsername(input, existingUsernames);

        assertEquals(expected, result);
    }

    @Test
    public void testGenerateUsernameWithCrossCollisions() {
        User input = new User();
        input.setFirstName("John");
        input.setLastName("Doe");
        String expected = "John.Doe.2";
        Set<String> existingUsernames = Set.of("John.Doe", "John.Doe.1");

        String result = credentialsGenerator.generateUsername(input, existingUsernames);

        assertEquals(expected, result);
    }

    @Test
    public void testGeneratePasswordLengthEqualsTo10() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword(credentialsGenerator.generatePassword());
        assertEquals(10, user.getPassword().length());
    }

    @Test
    public void testPasswordForSymbols() {
        String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword(credentialsGenerator.generatePassword());
        for  (int i = 0; i < user.getPassword().length(); i++) {
            assertTrue(ALPHABET.contains(user.getPassword().substring(i, i + 1)));
        }
    }
}
