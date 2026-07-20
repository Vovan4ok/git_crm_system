package org.volodymyrzganiaiko.gym.crm.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@ApiModel(description = "Training session to be recorded. The training type is taken from the trainer's specialization.")
public record AddTrainingRequest(
        @ApiModelProperty(value = "Username of the trainee attending the session", required = true, example = "Tr.Ainee")
        @NotBlank String traineeUsername,

        @ApiModelProperty(value = "Username of the trainer conducting the session", required = true, example = "Tra.Iner")
        @NotBlank String trainerUsername,

        @ApiModelProperty(value = "Name of the training session", required = true, example = "Morning session")
        @NotBlank String trainingName,

        @ApiModelProperty(value = "Date the session takes place on", required = true, example = "2026-07-20")
        @NotNull LocalDate trainingDate,

        @ApiModelProperty(value = "Duration of the session in minutes. Must be positive.", required = true, example = "60")
        @NotNull @Positive Integer trainingDuration) {
}
