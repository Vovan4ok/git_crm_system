package org.volodymyrzganiaiko.gym.crm.system.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

public record AddTrainingRequest(@NotBlank String traineeUsername, @NotBlank String trainerUsername, @NotBlank String trainingName, @NotNull
                                 LocalDate trainingDate, @NotNull @Positive Integer trainingDuration) {
}
