package org.volodymyrzganiaiko.gym.crm.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "Full trainer profile, including the trainees currently assigned to them")
public record TrainerProfileResponse(
        @ApiModelProperty(value = "Generated username of the trainer", example = "Tra.Iner")
        String username,

        @ApiModelProperty(value = "First name of the trainer", example = "Jane")
        String firstName,

        @ApiModelProperty(value = "Last name of the trainer", example = "Roe")
        String lastName,

        @ApiModelProperty(value = "Training type the trainer specializes in")
        TrainingTypeResponse specialization,

        @ApiModelProperty(value = "Whether the profile is active", example = "true")
        Boolean isActive,

        @ApiModelProperty(value = "Trainees assigned to this trainer, empty when none are assigned")
        List<TraineeSummaryResponse> trainees) {
}