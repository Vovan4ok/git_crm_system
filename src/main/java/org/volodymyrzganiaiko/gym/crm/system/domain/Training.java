package org.volodymyrzganiaiko.gym.crm.system.domain;

import java.time.LocalDate;
import java.util.UUID;

public class Training {
    private UUID traineeId;
    private UUID trainerId;
    private TrainingType trainingType;
    private String trainingName;
    private LocalDate trainingDate;
    private Integer trainingDurationInMinutes;

    public Training() {}

    public Training(UUID traineeId, UUID trainerId, TrainingType trainingType, String trainingName, LocalDate trainingDate, Integer trainingDurationInMinutes) {
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingType = trainingType;
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.trainingDurationInMinutes = trainingDurationInMinutes;
    }

    public UUID getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(UUID traineeId) {
        this.traineeId = traineeId;
    }

    public UUID getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(UUID trainerId) {
        this.trainerId = trainerId;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public Integer getTrainingDurationInMinutes() {
        return trainingDurationInMinutes;
    }

    public void setTrainingDurationInMinutes(Integer trainingDurationInMinutes) {
        this.trainingDurationInMinutes = trainingDurationInMinutes;
    }

    @Override
    public String toString() {
        return "Training{" +
                "traineeId=" + traineeId +
                ", trainerId=" + trainerId +
                ", trainingType=" + trainingType +
                ", trainingName='" + trainingName + '\'' +
                ", trainingDate=" + trainingDate +
                ", trainingDurationInMinutes=" + trainingDurationInMinutes +
                '}';
    }
}
