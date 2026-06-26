package org.volodymyrzganiaiko.gym.crm.system.domain;

import java.time.LocalDate;
import java.util.UUID;

public class Trainee extends User {
    private LocalDate dateOfBirth;
    private String address;

    public Trainee() {}

    public Trainee(String firstName, String lastName, String username, String password, boolean isActive, LocalDate dateOfBirth, String address, UUID userId) {
        super(firstName, lastName, username, password, isActive, userId);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                super.toString() +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                '}';
    }
}
