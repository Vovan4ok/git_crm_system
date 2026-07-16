package org.volodymyrzganiaiko.gym.crm.system.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record TrainerRegistrationRequest(@NotBlank String firstName, @NotBlank String lastName, @NotNull Long specializationId) {
}
