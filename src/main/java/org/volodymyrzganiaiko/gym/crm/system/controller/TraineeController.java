package org.volodymyrzganiaiko.gym.crm.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
public class TraineeController {
    @Autowired
    private GymFacade gymFacade;

    @PostMapping
    public ResponseEntity<TraineeRegistrationDTO> registerTrainee(@Valid @RequestBody TraineeRegistrationRequest  req) {
        Trainee trainee = mapTrainee(req.firstName(), req.lastName(), null, req.dateOfBirth(), req.address());
        return ResponseEntity.ok(gymFacade.createTrainee(trainee));
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileResponse> getProfile(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass) {
        return ResponseEntity.ok(gymFacade.getTraineeProfile(new Credentials(authUser, authPass), username));
    }

    @PutMapping("/{username}")
    public ResponseEntity<TraineeProfileResponse> updateProfile(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass, @Valid @RequestBody UpdateTraineeRequest req) {
        Trainee trainee = mapTrainee(req.firstName(), req.lastName(), req.isActive(), req.dateOfBirth(), req.address());
        return ResponseEntity.ok(gymFacade.updateTraineeProfile(new Credentials(authUser, authPass), username, trainee));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteProfile(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass) {
        gymFacade.deleteTraineeProfile(new Credentials(authUser, authPass), username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/unassigned-trainers")
    public ResponseEntity<List<TrainerSummaryResponse>> getUnassignedTrainers(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass) {
        return ResponseEntity.ok(gymFacade.getUnassignedTrainers(new Credentials(authUser, authPass), username));
    }

    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<TrainerSummaryResponse>> updateTrainers(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass, @Valid @RequestBody UpdateTrainerListRequest req) {
        return ResponseEntity.ok(gymFacade.updateTrainers(new Credentials(authUser, authPass), username, req.usernames()));
    }

    @PatchMapping("/{username}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass, @Valid @RequestBody UpdateStatusRequest req) {
        gymFacade.changeTraineeStatus(new Credentials(authUser, authPass), username, req.isActive());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TraineeTrainingResponse>> getTrainings(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass, @RequestParam(required = false, value = "periodFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @RequestParam(required = false, value = "periodTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to, @RequestParam(required = false) String trainerName, @RequestParam(required = false) String trainingType) {
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
