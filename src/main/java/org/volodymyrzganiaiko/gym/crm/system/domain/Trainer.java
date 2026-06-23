package org.volodymyrzganiaiko.gym.crm.system.domain;

import java.util.UUID;

public class Trainer extends User {
    private TrainingType specialization;
    private UUID userId;

    public Trainer() {}

    public Trainer(TrainingType specialization, UUID userId) {
        this.specialization = specialization;
        this.userId = userId;
    }

    public Trainer(String firstName, String lastName, String username, String password, boolean isActive, TrainingType specialization, UUID userId) {
        super(firstName, lastName, username, password, isActive);
        this.specialization = specialization;
        this.userId = userId;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "specialization=" + specialization +
                ", userId=" + userId +
                '}';
    }
}
