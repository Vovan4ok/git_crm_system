package org.volodymyrzganiaiko.gym.crm.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ApiModel(description = "New state of a trainee profile. Every field is overwritten, so omitting an optional field clears it.")
public record UpdateTraineeRequest (
        @ApiModelProperty(value = "First name of the trainee", required = true, example = "John")
        @NotBlank String firstName,

        @ApiModelProperty(value = "Last name of the trainee", required = true, example = "Doe")
        @NotBlank String lastName,

        @ApiModelProperty(value = "Activation state of the profile", required = true, example = "true")
        @NotNull Boolean isActive,

        @ApiModelProperty(value = "Date of birth, optional", example = "1990-05-15")
        LocalDate dateOfBirth,

        @ApiModelProperty(value = "Postal address, optional", example = "Main st. 1")
        String address) {
}