package org.volodymyrzganiaiko.gym.crm.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.dto.*;
import org.volodymyrzganiaiko.gym.crm.system.facade.GymFacade;

import javax.validation.Valid;
import java.time.LocalDate;

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
