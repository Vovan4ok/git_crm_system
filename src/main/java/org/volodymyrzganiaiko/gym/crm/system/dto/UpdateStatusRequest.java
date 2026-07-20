package org.volodymyrzganiaiko.gym.crm.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel(description = "Requested activation state. Setting the state the profile already has is rejected.")
public record UpdateStatusRequest (
        @ApiModelProperty(value = "True activates the profile, false deactivates it", required = true, example = "true")
        @NotNull Boolean isActive) {
}