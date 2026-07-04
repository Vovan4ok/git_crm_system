package org.volodymyrzganiaiko.gym.crm.system.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.volodymyrzganiaiko.gym.crm.system.dao.TraineeDAO;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainerDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.User;
import org.volodymyrzganiaiko.gym.crm.system.utils.CredentialsGenerator;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CredentialsServiceTest {
    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private CredentialsGenerator credentialsGenerator;

    @InjectMocks
    private CredentialsService credentialsService;

    @Test
    void generateUsername_collectsUsernamesFromBothDaos_andDelegates() {
        when(traineeDAO.findAll()).thenReturn(List.of(new Trainee(null, null, null, new User(null, null, null, "A.One", null, true), null)));
        when(trainerDAO.findAll()).thenReturn(List.of(new Trainer(null, null, new User(null, null, null, "B.Two", null, true), null)));
        when(credentialsGenerator.generateUsername(any(), any())).thenReturn("A.One.1");

        String result = credentialsService.generateUsername(new User(1L,"A","One",null,null,true));

        assertEquals("A.One.1", result);
        ArgumentCaptor<Set<String>> captor = ArgumentCaptor.forClass(Set.class);
        verify(credentialsGenerator).generateUsername(any(User.class), captor.capture());
        assertTrue(captor.getValue().containsAll(Set.of("A.One", "B.Two")));
    }

    @Test
    void generatePassword_delegatesToGenerator() {
        when(credentialsGenerator.generatePassword()).thenReturn("secret1234");
        assertEquals("secret1234", credentialsService.generatePassword());
        verify(credentialsGenerator).generatePassword();
    }
}
