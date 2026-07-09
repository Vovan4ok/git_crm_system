package org.volodymyrzganiaiko.gym.crm.system.dto;

public record TraineeRegistrationDTO(String username, String password) {
    @Override
    public String toString() {
        return "TraineeRegistrationDTO{" +
                "username=" + username +
                "}";
    }
}
