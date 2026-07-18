package org.volodymyrzganiaiko.gym.crm.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.volodymyrzganiaiko.gym.crm.system.dto.AddTrainingRequest;
import org.volodymyrzganiaiko.gym.crm.system.dto.Credentials;
import org.volodymyrzganiaiko.gym.crm.system.facade.GymFacade;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/trainings")
public class TrainingController {
    @Autowired
    private GymFacade gymFacade;

    @PostMapping
    public ResponseEntity<Void> addTraining(@RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass, @Valid @RequestBody AddTrainingRequest req) {
        gymFacade.createTraining(new Credentials(authUser, authPass), req);
        return ResponseEntity.ok().build();
    }
}
