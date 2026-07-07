package org.volodymyrzganiaiko.gym.crm.system.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.volodymyrzganiaiko.gym.crm.system.dao.UserDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.exceptions.AuthenticationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private UserDAO userDAO;

    @InjectMocks
    AuthenticationService authenticationService;

    @Test
    public void check_userFound_andPasswordMatches() {
        Trainee trainee = new Trainee(LocalDate.parse("2003-11-08"), "Test address", "John", "Doe", "John.Doe", "random", true, new HashSet<>());
        when(userDAO.findByUsername(any())).thenReturn(Optional.of(trainee));

        assertDoesNotThrow(() -> authenticationService.check(trainee.getUsername(), trainee.getPassword()));
    }

    @Test
    public void check_userFound_andPasswordDoesntMatch() {
        when(userDAO.findByUsername(any())).thenReturn(Optional.of(new Trainee(LocalDate.parse("2003-11-08"), "Test address", "Test", "Test", "Test.Test", "test", true, new HashSet<>())));

        assertThrows(AuthenticationException.class, () -> authenticationService.check("Test.Test", "password"));
    }

    @Test
    public void check_userNotFound() {
        when(userDAO.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> authenticationService.check("John.Doe", "random"));
    }
}
