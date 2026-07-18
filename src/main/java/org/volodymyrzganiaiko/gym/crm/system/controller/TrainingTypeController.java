package org.volodymyrzganiaiko.gym.crm.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.volodymyrzganiaiko.gym.crm.system.dto.Credentials;
import org.volodymyrzganiaiko.gym.crm.system.dto.TrainingTypeResponse;
import org.volodymyrzganiaiko.gym.crm.system.facade.GymFacade;

import java.util.List;

@RestController
@RequestMapping("/api/training-types")
public class TrainingTypeController {
    @Autowired
    private GymFacade gymFacade;

    @GetMapping
    public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes(@RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass) {
        return ResponseEntity.ok(gymFacade.getTrainingTypes(new Credentials(authUser, authPass)));
    }
}
