package org.volodymyrzganiaiko.gym.crm.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.Training;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class AppConfig {

    @Bean
    public Map<UUID, Trainee> traineeStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<UUID, Trainer> trainerStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<UUID, Training> trainingStorage() {
        return new HashMap<>();
    }
}
