package org.volodymyrzganiaiko.gym.crm.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;
import org.volodymyrzganiaiko.gym.crm.system.dto.TrainerRegistrationDTO;
import org.volodymyrzganiaiko.gym.crm.system.dto.TrainerRegistrationRequest;
import org.volodymyrzganiaiko.gym.crm.system.facade.GymFacade;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {
    @Autowired
    private GymFacade gymFacade;

    @PostMapping
    public ResponseEntity<TrainerRegistrationDTO> createTrainer(@Valid @RequestBody TrainerRegistrationRequest trainerRegistrationRequest) {
        Trainer trainer = new Trainer();
        trainer.setFirstName(trainerRegistrationRequest.firstName());
        trainer.setLastName(trainerRegistrationRequest.lastName());
        trainer.setSpecialization(new TrainingType(trainerRegistrationRequest.specializationId(), null));
        return new ResponseEntity<>(gymFacade.createTrainer(trainer),  HttpStatus.OK);
    }
}
