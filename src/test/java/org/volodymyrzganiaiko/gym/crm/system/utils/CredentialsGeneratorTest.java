package org.volodymyrzganiaiko.gym.crm.system.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.volodymyrzganiaiko.gym.crm.system.domain.User;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

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
