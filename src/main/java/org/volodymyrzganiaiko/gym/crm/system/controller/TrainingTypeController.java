package org.volodymyrzganiaiko.gym.crm.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(tags = "Training types")
public class TrainingTypeController {
    @Autowired
    private GymFacade gymFacade;

    @GetMapping
    @ApiOperation(value = "List training types", notes = "Returns every training type available in the system.")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Wrong username or password")
    })
    public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes(@RequestHeader("X-Username") String authUser, @RequestHeader("X-Password") String authPass) {
        return ResponseEntity.ok(gymFacade.getTrainingTypes(new Credentials(authUser, authPass)));
    }
}
