package org.volodymyrzganiaiko.gym.crm.system.dto;

import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;

public record TraineeRegistrationDTO(Trainee trainee, String password) {
    @Override
    public String toString() {
        return "TraineeRegistrationDTO{" +
                "user_id=" + trainee.getId() +
                "}";
    }
}
