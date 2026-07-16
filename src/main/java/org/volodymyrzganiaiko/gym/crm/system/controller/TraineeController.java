package org.volodymyrzganiaiko.gym.crm.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.dto.TraineeRegistrationDTO;
import org.volodymyrzganiaiko.gym.crm.system.dto.TraineeRegistrationRequest;
import org.volodymyrzganiaiko.gym.crm.system.facade.GymFacade;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/trainees")
public class TraineeController {
    @Autowired
    private GymFacade gymFacade;

    @PostMapping
    public ResponseEntity<TraineeRegistrationDTO> registerTrainee(@Valid @RequestBody TraineeRegistrationRequest  traineeRegistrationRequest) {
        Trainee trainee = new Trainee();
        trainee.setFirstName(traineeRegistrationRequest.firstName());
        trainee.setLastName(traineeRegistrationRequest.lastName());
        trainee.setDateOfBirth(traineeRegistrationRequest.dateOfBirth());
        trainee.setAddress(traineeRegistrationRequest.address());
        return new ResponseEntity<>(gymFacade.createTrainee(trainee), HttpStatus.OK);
    }
}
