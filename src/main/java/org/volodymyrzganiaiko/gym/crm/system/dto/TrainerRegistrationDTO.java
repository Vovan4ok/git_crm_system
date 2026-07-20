package org.volodymyrzganiaiko.gym.crm.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Credentials issued for a newly registered trainer")
public record TrainerRegistrationDTO(
        @ApiModelProperty(value = "Username generated from the first and last name, with a suffix when that combination is already taken", example = "Jane.Roe")
        String username,

        @ApiModelProperty(value = "Generated password. Returned in this response only and stored encoded, so it cannot be retrieved again.")
        String password) {
    @Override
    public String toString() {
        return "TrainerRegistrationDTO{" +
                "username=" + username +
                "}";
    }
}