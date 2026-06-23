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
                    Map<UUID, Trainee> storage = (Map<UUID, Trainee>) bean;
                    List<String> lines = readLines(traineeStoragePath);
                    for (String line : lines) {
                        if (!line.startsWith("userId")) {
                            try {
                                String[] fields = line.split(",");
                                Trainee trainee = new Trainee();
                                trainee.setUserId(UUID.fromString(fields[0]));
                                trainee.setFirstName(fields[1]);
                                trainee.setLastName(fields[2]);
                                trainee.setUsername(fields[3]);
                                trainee.setPassword(fields[4]);
                                trainee.setActive(Boolean.parseBoolean(fields[5]));
                                trainee.setDateOfBirth(LocalDate.parse(fields[6]));
                                trainee.setAddress(fields[7]);
                                storage.put(trainee.getUserId(), trainee);
                            } catch (RuntimeException e) {
                                log.warn("Problem with line {}\n{}", line, e.getMessage());
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case "trainerStorage": {
                try {
                    Map<UUID, Trainer> storage = (Map<UUID, Trainer>) bean;
                    List<String> lines = readLines(trainerStoragePath);
                    for (String line : lines) {
                        if (!line.startsWith("userId")) {
                            try {
                                String[] fields = line.split(",");
                                Trainer trainer = new Trainer();
                                trainer.setUserId(UUID.fromString(fields[0]));
                                trainer.setFirstName(fields[1]);
                                trainer.setLastName(fields[2]);
                                trainer.setUsername(fields[3]);
                                trainer.setPassword(fields[4]);
                                trainer.setActive(Boolean.parseBoolean(fields[5]));
                                trainer.setSpecialization(new TrainingType(fields[6]));
                                storage.put(trainer.getUserId(), trainer);
                            } catch (RuntimeException e) {
                                log.warn("Problem with line {}\n{}", line, e.getMessage());
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case "trainingStorage": {
                try {
                    Map<UUID, Training> storage = (Map<UUID, Training>) bean;
                    List<String> lines = readLines(trainingStoragePath);
                    for (String line : lines) {
                        if (!line.startsWith("trainingId")) {
                            try {
                                String[] fields = line.split(",");
                                Training training = new Training();
                                training.setTrainingId(UUID.fromString(fields[0]));
                                training.setTraineeId(UUID.fromString(fields[1]));
                                training.setTrainerId(UUID.fromString(fields[2]));
                                training.setTrainingType(new TrainingType(fields[3]));
                                training.setTrainingName(fields[4]);
                                training.setTrainingDate(LocalDate.parse(fields[5]));
                                training.setTrainingDurationInMinutes(Integer.parseInt(fields[6]));
                                storage.put(training.getTrainingId(), training);
                            } catch (RuntimeException e) {
                                log.warn("Problem with line {}\n{}", line, e.getMessage());
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            default: {
                log.warn("Unknown bean {}", beanName);
                break;
            }
        }
        return bean;
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
