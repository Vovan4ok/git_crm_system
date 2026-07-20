package org.volodymyrzganiaiko.gym.crm.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Short trainee reference used inside a trainer profile")
public record TraineeSummaryResponse(
        @ApiModelProperty(value = "Generated username of the trainee", example = "Tr.Ainee")
        String username,

        @ApiModelProperty(value = "First name of the trainee", example = "John")
        String firstName,

        @ApiModelProperty(value = "Last name of the trainee", example = "Doe")
        String lastName) {
}