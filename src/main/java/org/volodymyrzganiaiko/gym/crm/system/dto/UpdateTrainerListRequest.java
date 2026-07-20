package org.volodymyrzganiaiko.gym.crm.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(description = "Replacement set of trainers for a trainee. The list is applied as a whole, not merged with the current one.")
public record UpdateTrainerListRequest(
        @ApiModelProperty(value = "Usernames of the trainers to assign. An empty list removes every assignment.", required = true)
        @NotNull List<String> usernames) {
}