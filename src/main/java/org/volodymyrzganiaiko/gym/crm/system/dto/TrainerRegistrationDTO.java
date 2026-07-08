package org.volodymyrzganiaiko.gym.crm.system.dto;

import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;

public record TrainerRegistrationDTO(Trainer trainer, String password) {
    @Override
    public String toString() {
        return "TrainerRegistrationDTO{" +
                "user_id=" + trainer.getId() +
                "}";
    }
}
