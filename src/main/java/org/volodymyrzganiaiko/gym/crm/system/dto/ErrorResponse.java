package org.volodymyrzganiaiko.gym.crm.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(description = "Body returned for every failed request")
public record ErrorResponse (
        @ApiModelProperty(value = "HTTP status code, repeated in the body for convenience", example = "404")
        int status,

        @ApiModelProperty(value = "Human readable reason for the failure", example = "Trainee with the username Tr.Ainee was not found")
        String message,

        @ApiModelProperty(value = "Server time the error was produced at", example = "2026-07-20T10:15:30")
        LocalDateTime timestamp) {
}