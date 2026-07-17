package org.volodymyrzganiaiko.gym.crm.system.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public record UpdateTraineeRequest (@NotBlank String firstName, @NotBlank String lastName, @NotNull Boolean isActive, LocalDate dateOfBirth, String address) {
}
