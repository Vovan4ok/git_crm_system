package org.volodymyrzganiaiko.gym.crm.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Training type available in the system")
public record TrainingTypeResponse(
        @ApiModelProperty(value = "Identifier of the training type", example = "2")
        Long id,

        @ApiModelProperty(value = "Display name of the training type", example = "Cardio")
        String name) {
}
