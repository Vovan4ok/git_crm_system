package org.volodymyrzganiaiko.gym.crm.system.mapper;

import org.springframework.stereotype.Component;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;
import org.volodymyrzganiaiko.gym.crm.system.dto.TraineeProfileResponse;
import org.volodymyrzganiaiko.gym.crm.system.dto.TrainerSummaryResponse;
import org.volodymyrzganiaiko.gym.crm.system.dto.TrainingTypeResponse;

@Component
public class DtoMapper {

    public TrainingTypeResponse mapTrainingTypeToTrainingTypeResponse(TrainingType trainingType) {
        return new TrainingTypeResponse(trainingType.getId(), trainingType.getTrainingTypeName());
    }

    public TrainerSummaryResponse mapTrainerToTrainerSummaryResponse(Trainer trainer) {
        return new TrainerSummaryResponse(trainer.getUsername(), trainer.getFirstName(), trainer.getLastName(), mapTrainingTypeToTrainingTypeResponse(trainer.getSpecialization()));
    }

    public TraineeProfileResponse mapTraineeToTraineeProfileResponse(Trainee trainee) {
        return new TraineeProfileResponse(trainee.getUsername(), trainee.getFirstName(), trainee.getLastName(), trainee.getDateOfBirth(), trainee.getAddress(), trainee.getIsActive(), trainee.getTrainers().stream().map(this::mapTrainerToTrainerSummaryResponse).toList());
    }
}
