package org.volodymyrzganiaiko.gym.crm.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.volodymyrzganiaiko.gym.crm.system.dto.ChangePasswordRequest;
import org.volodymyrzganiaiko.gym.crm.system.dto.Credentials;
import org.volodymyrzganiaiko.gym.crm.system.facade.GymFacade;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/login")
public class AuthController {
    @Autowired
    private GymFacade gymFacade;

    @GetMapping
    public ResponseEntity<Void> login(@RequestHeader("X-Username") String username, @RequestHeader("X-Password") String password) {
        gymFacade.login(new Credentials(username, password));
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> changePassword(@RequestHeader("X-Username") String username, @RequestHeader("X-Password") String password, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        gymFacade.changeLogin(new Credentials(username, password), changePasswordRequest.newPassword());
        return ResponseEntity.ok().build();
    }
}
