package org.volodymyrzganiaiko.gym.crm.system.dto;

import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record UpdateTrainerRequest(@NotBlank String firstName, @NotBlank String lastName, @NotNull Boolean isActive) {
}
