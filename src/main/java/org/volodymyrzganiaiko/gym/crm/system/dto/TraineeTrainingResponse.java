package org.volodymyrzganiaiko.gym.crm.system.dto;

import java.time.LocalDate;

public record TraineeTrainingResponse(String trainingName, LocalDate trainingDate, TrainingTypeResponse trainingType, Integer trainingDuration, String trainerName) {
}
