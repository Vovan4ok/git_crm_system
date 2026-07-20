package org.volodymyrzganiaiko.gym.crm.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "New password for the authenticated user")
public record ChangePasswordRequest(
        @ApiModelProperty(value = "Password that replaces the current one. Must not be blank.", required = true)
        @NotBlank String newPassword) {
}