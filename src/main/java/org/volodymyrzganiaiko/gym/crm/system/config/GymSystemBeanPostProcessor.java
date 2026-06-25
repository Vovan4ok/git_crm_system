package org.volodymyrzganiaiko.gym.crm.system.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.Training;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class GymSystemBeanPostProcessor implements BeanPostProcessor {
    @Value(value = "${trainees-storage-file}")
    private String traineeStoragePath;
    @Value(value = "${trainers-storage-file}")
    private String trainerStoragePath;
    @Value(value = "${trainings-storage-file}")
    private String trainingStoragePath;
    private static final Logger log = LoggerFactory.getLogger(GymSystemBeanPostProcessor.class);

    @Override
    @SuppressWarnings("unchecked")
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        switch (beanName) {
            case "traineeStorage": {
                try {
                    loadStorage((Map<UUID, Trainee>) bean, traineeStoragePath, "userId", this::parseTrainee);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case "trainerStorage": {
                try {
                    loadStorage((Map<UUID, Trainer>) bean, trainerStoragePath, "userId", this::parseTrainer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case "trainingStorage": {
                try {
                    loadStorage((Map<UUID, Training>) bean, trainingStoragePath, "trainingId", this::parseTraining);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            default: {
                log.debug("Bean {} is not a storage bean, skipping", beanName);
                break;
            }
        }
        return bean;
    }

    private Trainee parseTrainee(String[] fields) {
        Trainee trainee = new Trainee();
        trainee.setUserId(UUID.fromString(fields[0]));
        trainee.setFirstName(fields[1]);
        trainee.setLastName(fields[2]);
        trainee.setUsername(fields[3]);
        trainee.setPassword(fields[4]);
        trainee.setActive(Boolean.parseBoolean(fields[5]));
        trainee.setDateOfBirth(LocalDate.parse(fields[6]));
        trainee.setAddress(fields[7]);
        return trainee;
    }

    private Trainer parseTrainer(String[] fields) {
        Trainer trainer = new Trainer();
        trainer.setUserId(UUID.fromString(fields[0]));
        trainer.setFirstName(fields[1]);
        trainer.setLastName(fields[2]);
        trainer.setUsername(fields[3]);
        trainer.setPassword(fields[4]);
        trainer.setActive(Boolean.parseBoolean(fields[5]));
        trainer.setSpecialization(new TrainingType(fields[6]));
        return trainer;
    }

    private Training parseTraining(String[] fields) {
        Training training = new Training();
        training.setTrainingId(UUID.fromString(fields[0]));
        training.setTraineeId(UUID.fromString(fields[1]));
        training.setTrainerId(UUID.fromString(fields[2]));
        training.setTrainingType(new TrainingType(fields[3]));
        training.setTrainingName(fields[4]);
        training.setTrainingDate(LocalDate.parse(fields[5]));
        training.setTrainingDurationInMinutes(Integer.parseInt(fields[6]));
        return training;
    }

    private <V> void loadStorage(Map<UUID, V> storage, String path, String headerPrefix, Function<String[], V> rowParser) throws IOException {
        List<String> lines = readLines(path);
        for (String line : lines) {
            if (!line.startsWith(headerPrefix)) {
                try {
                    String[] fields = line.split(",");
                    V value = rowParser.apply(fields);
                    storage.put(UUID.fromString(fields[0]), value);
                } catch (Exception e) {
                    log.warn("Problem with line {}\n{}", line, e.getMessage());
                }
            }
        }
    }

    private List<String> readLines(String path) throws IOException {
        List<String> lines;
        var is = getClass().getClassLoader().getResourceAsStream(path);
        if (is == null) {
            throw new IllegalStateException("CSV not found for classpath: " + path);
        }
        try (var reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            lines = reader.lines().toList();
        }
        return lines;
    }
}
