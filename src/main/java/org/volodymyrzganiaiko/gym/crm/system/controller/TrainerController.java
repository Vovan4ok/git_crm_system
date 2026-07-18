package org.volodymyrzganiaiko.gym.crm.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;
import org.volodymyrzganiaiko.gym.crm.system.dto.*;
import org.volodymyrzganiaiko.gym.crm.system.facade.GymFacade;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {
    @Autowired
    private GymFacade gymFacade;

    @PostMapping
    public ResponseEntity<TrainerRegistrationDTO> createTrainer(@Valid @RequestBody TrainerRegistrationRequest req) {
        Trainer trainer = mapTrainer(req.firstName(), req.lastName(), null, new TrainingType(req.specializationId(), null));
        return new ResponseEntity<>(gymFacade.createTrainer(trainer),  HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TrainerProfileResponse> getProfile(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass) {
        return ResponseEntity.ok(gymFacade.getTrainerProfile(new Credentials(authUser, authPass), username));
    }

    @PutMapping("/{username}")
    public ResponseEntity<TrainerProfileResponse> updateProfile(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass, @Valid @RequestBody UpdateTrainerRequest req) {
        Trainer trainer = mapTrainer(req.firstName(), req.lastName(), req.isActive(), null);
        return ResponseEntity.ok(gymFacade.updateTrainerProfile(new Credentials(authUser, authPass), username, trainer));
    }

    @PatchMapping("/{username}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable String username, @RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass, @Valid @RequestBody UpdateStatusRequest req) {
        gymFacade.changeTrainerStatus(new Credentials(authUser, authPass), username, req.isActive());
        return ResponseEntity.ok().build();
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
