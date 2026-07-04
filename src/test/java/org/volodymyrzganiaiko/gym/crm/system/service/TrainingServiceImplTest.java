package org.volodymyrzganiaiko.gym.crm.system.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.volodymyrzganiaiko.gym.crm.system.dao.TrainingDAO;
import org.volodymyrzganiaiko.gym.crm.system.domain.*;
import org.volodymyrzganiaiko.gym.crm.system.service.impl.TrainingServiceImpl;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceImplTest {
    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Training training;

    @BeforeEach
    void setUp() {
        Trainee trainee = new Trainee(1L, LocalDate.parse("2003-08-11"), "Test address", new User(1L, "John", "Doe", "John.Doe", "random", true), new HashSet<>());
        Trainer trainer = new Trainer(1L, new TrainingType(1L, "Yoga"), new User(2L, "Test", "Test", "Test.Test", "random", true), new HashSet<>());
        training = new Training();
        training.setId(1L);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingDate(LocalDate.now());
        training.setTrainingType(new TrainingType(1L, "Yoga"));
        training.setTrainingName("Yoga");
        training.setTrainingDurationInMinutes(90);
    }

    @Test
    public void addTraining_success() {
        when(trainingDAO.save(any(Training.class))).thenReturn(training);

        trainingService.addTraining(training.getTrainee().getUser().getUsername(), training.getTrainee().getUser().getPassword(), training);

        verify(trainingDAO).save(training);
        verify(authenticationService).check(training.getTrainee().getUser().getUsername(), training.getTrainee().getUser().getPassword());
    }

    @ParameterizedTest(name = "invalid field: {0}")
    @MethodSource("invalidTrainingMutators")
    public void addTraining_throwsException(String caseName, Consumer<Training> corrupt) {
        corrupt.accept(training);

        assertThrows(IllegalArgumentException.class,
                () -> trainingService.addTraining("John.Doe", "random", training));
        verifyNoInteractions(trainingDAO);
    }

    @Test
    public void findById_success() {
        when(trainingDAO.findById(1L)).thenReturn(Optional.of((training)));

        Optional<Training> result = trainingService.findById(1L);

        assertTrue(result.isPresent());
        verify(trainingDAO).findById(1L);
    }

    @Test
    public void getTraineeTrainings() {
        when(trainingDAO.findTraineeTrainings(any(), any(), any(), any(), any())).thenReturn(List.of(training));

        List<Training> result = trainingService.getTraineeTrainings("John.Doe", "random", null, null, null, null);

        assertFalse(result.isEmpty());
        verify(authenticationService).check("John.Doe",  "random");
        verify(trainingDAO).findTraineeTrainings("John.Doe", null, null, null, null);
    }

    @Test
    public void getTrainerTrainings() {
        when(trainingDAO.findTrainerTrainings(any(), any(), any(), any())).thenReturn(List.of(training));

        List<Training> result = trainingService.getTrainerTrainings("John.Doe", "random", null, null, null);

        assertFalse(result.isEmpty());
        verify(authenticationService).check("John.Doe",  "random");
        verify(trainingDAO).findTrainerTrainings("John.Doe", null, null, null);
    }

    @Test
    public void findAll() {
        when(trainingDAO.findAll()).thenReturn(List.of(training));

        List<Training> result = trainingService.findAll();

        assertFalse(result.isEmpty());
        verify(trainingDAO).findAll();
    }

    static Stream<Arguments> invalidTrainingMutators() {
        return Stream.of(
                Arguments.of("null trainee",        (Consumer<Training>) t -> t.setTrainee(null)),
                Arguments.of("null trainer",        (Consumer<Training>) t -> t.setTrainer(null)),
                Arguments.of("null trainingType",   (Consumer<Training>) t -> t.setTrainingType(null)),
                Arguments.of("blank trainingName",  (Consumer<Training>) t -> t.setTrainingName("  ")),
                Arguments.of("null trainingDate",   (Consumer<Training>) t -> t.setTrainingDate(null)),
                Arguments.of("null duration",       (Consumer<Training>) t -> t.setTrainingDurationInMinutes(null))
        );
    }
}
