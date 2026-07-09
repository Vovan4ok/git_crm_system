package org.volodymyrzganiaiko.gym.crm.system.dto;


public record TrainerRegistrationDTO(String username, String password) {
    @Override
    public String toString() {
        return "TrainerRegistrationDTO{" +
                "username=" + username +
                "}";
    }
}
