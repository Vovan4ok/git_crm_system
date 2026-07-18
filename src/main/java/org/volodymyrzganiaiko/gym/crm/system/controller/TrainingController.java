package org.volodymyrzganiaiko.gym.crm.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.volodymyrzganiaiko.gym.crm.system.dto.AddTrainingRequest;
import org.volodymyrzganiaiko.gym.crm.system.dto.Credentials;
import org.volodymyrzganiaiko.gym.crm.system.facade.GymFacade;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/trainings")
@Api(tags = "Trainings")
public class TrainingController {
    @Autowired
    private GymFacade gymFacade;

    @PostMapping
    @ApiOperation(value = "Add a training", notes = "Creates a training and links the trainee with the trainer if they were not linked yet. The training type is taken from the trainer's specialization.")
    @ApiResponses({
            @ApiResponse(code = 400, message = "The request body failed validation"),
            @ApiResponse(code = 401, message = "Wrong username or password"),
            @ApiResponse(code = 404, message = "The trainee or the trainer was not found")
    })
    public ResponseEntity<Void> addTraining(@RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass, @Valid @RequestBody AddTrainingRequest req) {
        gymFacade.createTraining(new Credentials(authUser, authPass), req);
        return ResponseEntity.ok().build();
    }
}
