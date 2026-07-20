package org.volodymyrzganiaiko.gym.crm.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.volodymyrzganiaiko.gym.crm.system.dto.ChangePasswordRequest;
import org.volodymyrzganiaiko.gym.crm.system.dto.Credentials;
import org.volodymyrzganiaiko.gym.crm.system.facade.GymFacade;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/login")
@Api(tags = "User authorization")
public class AuthController {
    @Autowired
    private GymFacade gymFacade;

    @GetMapping
    @ApiOperation(value = "Log in", notes = "Verifies the credentials passed in the X-Username and X-Password headers.")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Wrong username or password")
    })
    public ResponseEntity<Void> login(@RequestHeader("X-Username") String username, @RequestHeader("X-Password") String password) {
        gymFacade.login(new Credentials(username, password));
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @ApiOperation(value = "Change password", notes = "Replaces the password of the authenticated user with the one supplied in the request body.")
    @ApiResponses({
            @ApiResponse(code = 400, message = "The new password is missing or does not satisfy the constraints"),
            @ApiResponse(code = 401, message = "Wrong username or password")
    })
    public ResponseEntity<Void> changePassword(@RequestHeader("X-Username") String username, @RequestHeader("X-Password") String password, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        gymFacade.changeLogin(new Credentials(username, password), changePasswordRequest.newPassword());
        return ResponseEntity.ok().build();
    }
}
