package org.volodymyrzganiaiko.gym.crm.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;

@ApiModel(description = "Training session as seen from the trainee side")
public record TraineeTrainingResponse(
        @ApiModelProperty(value = "Name of the training session", example = "Morning session")
        String trainingName,

        @ApiModelProperty(value = "Date the session took place on", example = "2026-07-20")
        LocalDate trainingDate,

        @ApiModelProperty(value = "Training type of the session")
        TrainingTypeResponse trainingType,

        @ApiModelProperty(value = "Duration of the session in minutes", example = "60")
        Integer trainingDuration,

        @ApiModelProperty(value = "Username of the trainer who conducted the session", example = "Tra.Iner")
        String trainerName) {
}