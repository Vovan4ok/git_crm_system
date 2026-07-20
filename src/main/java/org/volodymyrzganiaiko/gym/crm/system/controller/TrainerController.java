package org.volodymyrzganiaiko.gym.crm.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;
import org.volodymyrzganiaiko.gym.crm.system.dto.*;
import org.volodymyrzganiaiko.gym.crm.system.facade.GymFacade;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trainers")
@Api(tags = "Trainers")
public class TrainerController {
    @Autowired
    private GymFacade gymFacade;

    @PostMapping
    @ApiOperation(value = "Register a trainer", notes = "Creates a trainer and returns the generated username together with the generated password. No authentication is required.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Trainer was successfully created"),
            @ApiResponse(code = 400, message = "The request body failed validation"),
            @ApiResponse(code = 404, message = "The specialization was not found")
    })
    public ResponseEntity<TrainerRegistrationDTO> createTrainer(@Valid @RequestBody TrainerRegistrationRequest req) {
        Trainer trainer = mapTrainer(req.firstName(), req.lastName(), null, new TrainingType(req.specializationId(), null));
        return ResponseEntity.status(HttpStatus.CREATED).body(gymFacade.createTrainer(trainer));
    }

    @GetMapping("/{username}")
    @ApiOperation(value = "Get a trainer profile", notes = "Returns the profile of the trainer identified by the path variable, including the list of assigned trainees.")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Wrong username or password"),
            @ApiResponse(code = 404, message = "The trainer was not found")
    })
    public ResponseEntity<TrainerProfileResponse> getProfile(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass) {
        return ResponseEntity.ok(gymFacade.getTrainerProfile(new Credentials(authUser, authPass), username));
    }

    @PutMapping("/{username}")
    @ApiOperation(value = "Update a trainer profile", notes = "Overwrites the editable fields of the trainer and returns the updated profile. The specialization cannot be changed.")
    @ApiResponses({
            @ApiResponse(code = 400, message = "The request body failed validation"),
            @ApiResponse(code = 401, message = "Wrong username or password"),
            @ApiResponse(code = 404, message = "The trainer was not found")
    })
    public ResponseEntity<TrainerProfileResponse> updateProfile(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass, @Valid @RequestBody UpdateTrainerRequest req) {
        Trainer trainer = mapTrainer(req.firstName(), req.lastName(), req.isActive(), null);
        return ResponseEntity.ok(gymFacade.updateTrainerProfile(new Credentials(authUser, authPass), username, trainer));
    }

    @PatchMapping("/{username}/status")
    @ApiOperation(value = "Activate or deactivate a trainer", notes = "Switches the active flag of the trainer. The operation is not idempotent: setting the flag to the value it already has is rejected.")
    @ApiResponses({
            @ApiResponse(code = 400, message = "The request body failed validation"),
            @ApiResponse(code = 401, message = "Wrong username or password"),
            @ApiResponse(code = 404, message = "The trainer was not found"),
            @ApiResponse(code = 409, message = "The trainer is already in the requested state")
    })
    public ResponseEntity<Void> changeStatus(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass, @Valid @RequestBody UpdateStatusRequest req) {
        gymFacade.changeTrainerStatus(new Credentials(authUser, authPass), username, req.isActive());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/trainings")
    @ApiOperation(value = "List the trainings of a trainer", notes = "Returns the trainings of the trainer. Every filter is optional; omitting all of them returns the full list.")
    @ApiResponses({
            @ApiResponse(code = 400, message = "A date filter is not in the yyyy-MM-dd format"),
            @ApiResponse(code = 401, message = "Wrong username or password"),
            @ApiResponse(code = 404, message = "The trainer was not found")
    })
    public ResponseEntity<List<TrainerTrainingResponse>> getTrainings(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass, @ApiParam(value = "Lower bound of the training date, inclusive", example = "2026-01-31") @RequestParam(required = false, value = "periodFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @ApiParam(value = "Upper bound of the training date, inclusive", example = "2026-12-31") @RequestParam(required = false, value = "periodTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to, @ApiParam(value = "Filter by the trainee's first name") @RequestParam(required = false) String traineeName) {
        return ResponseEntity.ok(gymFacade.getTrainerTrainings(new Credentials(authUser, authPass), username, from, to, traineeName));
    }

    private Trainer mapTrainer(String firstName, String lastName, Boolean isActive, TrainingType specialization) {
        Trainer trainer = new Trainer();
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setIsActive(isActive);
        trainer.setSpecialization(specialization);
        return trainer;
    }
}
