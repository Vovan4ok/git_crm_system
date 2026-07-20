package org.volodymyrzganiaiko.gym.crm.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(description = "New state of a trainer profile. The specialization cannot be changed through this endpoint.")
public record UpdateTrainerRequest(
        @ApiModelProperty(value = "First name of the trainer", required = true, example = "Jane")
        @NotBlank String firstName,

        @ApiModelProperty(value = "Last name of the trainer", required = true, example = "Roe")
        @NotBlank String lastName,

        @ApiModelProperty(value = "Activation state of the profile", required = true, example = "true")
        @NotNull Boolean isActive) {
}