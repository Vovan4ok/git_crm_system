package org.volodymyrzganiaiko.gym.crm.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Short trainer reference used inside a trainee profile and in trainer lists")
public record TrainerSummaryResponse(
        @ApiModelProperty(value = "Generated username of the trainer", example = "Tra.Iner")
        String username,

        @ApiModelProperty(value = "First name of the trainer", example = "Jane")
        String firstName,

        @ApiModelProperty(value = "Last name of the trainer", example = "Roe")
        String lastName,

        @ApiModelProperty(value = "Training type the trainer specializes in")
        TrainingTypeResponse specialization) {
}