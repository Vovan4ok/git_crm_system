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
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.dto.*;
import org.volodymyrzganiaiko.gym.crm.system.facade.GymFacade;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trainees")
@Api(tags = "Trainees")
public class TraineeController {
    @Autowired
    private GymFacade gymFacade;

    @PostMapping
    @ApiOperation(value = "Register a trainee", notes = "Creates a trainee and returns the generated username together with the generated password. No authentication is required.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Trainee was successfully created"),
            @ApiResponse(code = 400, message = "The request body failed validation")
    })
    public ResponseEntity<TraineeRegistrationDTO> createTrainee(@Valid @RequestBody TraineeRegistrationRequest  req) {
        Trainee trainee = mapTrainee(req.firstName(), req.lastName(), null, req.dateOfBirth(), req.address());
        return ResponseEntity.status(HttpStatus.CREATED).body(gymFacade.createTrainee(trainee));
    }

    @GetMapping("/{username}")
    @ApiOperation(value = "Get a trainee profile", notes = "Returns the profile of the trainee identified by the path variable, including the list of assigned trainers.")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Wrong username or password"),
            @ApiResponse(code = 404, message = "The trainee was not found")
    })
    public ResponseEntity<TraineeProfileResponse> getProfile(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass) {
        return ResponseEntity.ok(gymFacade.getTraineeProfile(new Credentials(authUser, authPass), username));
    }

    @PutMapping("/{username}")
    @ApiOperation(value = "Update a trainee profile", notes = "Overwrites the editable fields of the trainee and returns the updated profile.")
    @ApiResponses({
            @ApiResponse(code = 400, message = "The request body failed validation"),
            @ApiResponse(code = 401, message = "Wrong username or password"),
            @ApiResponse(code = 404, message = "The trainee was not found")
    })
    public ResponseEntity<TraineeProfileResponse> updateProfile(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass, @Valid @RequestBody UpdateTraineeRequest req) {
        Trainee trainee = mapTrainee(req.firstName(), req.lastName(), req.isActive(), req.dateOfBirth(), req.address());
        return ResponseEntity.ok(gymFacade.updateTraineeProfile(new Credentials(authUser, authPass), username, trainee));
    }

    @DeleteMapping("/{username}")
    @ApiOperation(value = "Delete a trainee profile", notes = "Removes the trainee together with all of their trainings.")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Wrong username or password"),
            @ApiResponse(code = 404, message = "The trainee was not found")
    })
    public ResponseEntity<Void> deleteProfile(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass) {
        gymFacade.deleteTraineeProfile(new Credentials(authUser, authPass), username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/unassigned-trainers")
    @ApiOperation(value = "List trainers not assigned to the trainee", notes = "Returns the active trainers that are not yet linked with this trainee.")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Wrong username or password"),
            @ApiResponse(code = 404, message = "The trainee was not found")
    })
    public ResponseEntity<List<TrainerSummaryResponse>> getUnassignedTrainers(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass) {
        return ResponseEntity.ok(gymFacade.getUnassignedTrainers(new Credentials(authUser, authPass), username));
    }

    @PutMapping("/{username}/trainers")
    @ApiOperation(value = "Replace the trainer list of a trainee", notes = "Replaces the whole set of trainers assigned to the trainee and returns the resulting list.")
    @ApiResponses({
            @ApiResponse(code = 400, message = "The request body failed validation"),
            @ApiResponse(code = 401, message = "Wrong username or password"),
            @ApiResponse(code = 404, message = "The trainee or one of the trainers was not found")
    })
    public ResponseEntity<List<TrainerSummaryResponse>> updateTrainers(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass, @Valid @RequestBody UpdateTrainerListRequest req) {
        return ResponseEntity.ok(gymFacade.updateTrainers(new Credentials(authUser, authPass), username, req.usernames()));
    }

    @PatchMapping("/{username}/status")
    @ApiOperation(value = "Activate or deactivate a trainee", notes = "Switches the active flag of the trainee. The operation is not idempotent: setting the flag to the value it already has is rejected.")
    @ApiResponses({
            @ApiResponse(code = 400, message = "The request body failed validation"),
            @ApiResponse(code = 401, message = "Wrong username or password"),
            @ApiResponse(code = 404, message = "The trainee was not found"),
            @ApiResponse(code = 409, message = "The trainee is already in the requested state")
    })
    public ResponseEntity<Void> changeStatus(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass, @Valid @RequestBody UpdateStatusRequest req) {
        gymFacade.changeTraineeStatus(new Credentials(authUser, authPass), username, req.isActive());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/trainings")
    @ApiOperation(value = "List the trainings of a trainee", notes = "Returns the trainings of the trainee. Every filter is optional; omitting all of them returns the full list.")
    @ApiResponses({
            @ApiResponse(code = 400, message = "A date filter is not in the yyyy-MM-dd format"),
            @ApiResponse(code = 401, message = "Wrong username or password"),
            @ApiResponse(code = 404, message = "The trainee was not found")
    })
    public ResponseEntity<List<TraineeTrainingResponse>> getTrainings(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass, @ApiParam(value = "Lower bound of the training date, inclusive", example = "2026-01-31") @RequestParam(required = false, value = "periodFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @ApiParam(value = "Upper bound of the training date, inclusive", example = "2026-12-31") @RequestParam(required = false, value = "periodTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to, @ApiParam(value = "Filter by the trainer's first name") @RequestParam(required = false) String trainerName, @ApiParam(value = "Filter by the training type name") @RequestParam(required = false) String trainingType) {
        return ResponseEntity.ok(gymFacade.getTraineeTrainings(new Credentials(authUser, authPass), username, from, to, trainerName, trainingType));
    }

    private Trainee mapTrainee(String firstName, String lastName, Boolean isActive, LocalDate dateOfBirth, String address) {
        Trainee trainee = new Trainee();
        trainee.setFirstName(firstName);
        trainee.setLastName(lastName);
        trainee.setIsActive(isActive);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress(address);
        return trainee;
    }
}
