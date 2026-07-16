package org.volodymyrzganiaiko.gym.crm.system.dto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public record TraineeRegistrationRequest(@NotBlank String firstName, @NotBlank String lastName, LocalDate dateOfBirth, String address) {
}
