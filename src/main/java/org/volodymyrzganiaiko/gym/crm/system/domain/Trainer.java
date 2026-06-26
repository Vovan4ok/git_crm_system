package org.volodymyrzganiaiko.gym.crm.system.domain;

import java.util.UUID;

public class Trainer extends User {
    private TrainingType specialization;

    public Trainer() {}

    public Trainer(String firstName, String lastName, String username, String password, boolean isActive, TrainingType specialization, UUID userId) {
        super(firstName, lastName, username, password, isActive, userId);
        this.specialization = specialization;
    }

    public TrainingType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                super.toString() +
                ", specialization=" + specialization +
                '}';
    }
}
