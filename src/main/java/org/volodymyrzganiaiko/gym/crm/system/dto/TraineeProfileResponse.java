package org.volodymyrzganiaiko.gym.crm.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.util.List;

@ApiModel(description = "Full trainee profile, including the trainers currently assigned to them")
public record TraineeProfileResponse(
        @ApiModelProperty(value = "Generated username of the trainee", example = "Tr.Ainee")
        String username,

        @ApiModelProperty(value = "First name of the trainee", example = "John")
        String firstName,

        @ApiModelProperty(value = "Last name of the trainee", example = "Doe")
        String lastName,

        @ApiModelProperty(value = "Date of birth, null when never provided", example = "1990-05-15")
        LocalDate dateOfBirth,

        @ApiModelProperty(value = "Postal address, null when never provided", example = "Main st. 1")
        String address,

        @ApiModelProperty(value = "Whether the profile is active", example = "true")
        Boolean isActive,

        @ApiModelProperty(value = "Trainers assigned to this trainee, empty when none are assigned")
        List<TrainerSummaryResponse> trainers) {
}