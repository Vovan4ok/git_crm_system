package org.volodymyrzganiaiko.gym.crm.system;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.volodymyrzganiaiko.gym.crm.system.config.AppConfig;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainee;
import org.volodymyrzganiaiko.gym.crm.system.domain.Trainer;
import org.volodymyrzganiaiko.gym.crm.system.domain.Training;
import org.volodymyrzganiaiko.gym.crm.system.domain.TrainingType;
import org.volodymyrzganiaiko.gym.crm.system.facade.GymFacade;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        try (var applicationContext = new AnnotationConfigApplicationContext(AppConfig.class)) {
            GymFacade gymFacade = applicationContext.getBean(GymFacade.class);


            /////Trainees
            Trainee trainee = new Trainee();
            trainee.setFirstName("Volodymyr");
            trainee.setLastName("Zganiaiko");
            trainee.setDateOfBirth(LocalDate.parse("2003-11-08"));
            trainee.setAddress("Test address");

            System.out.println(gymFacade.findAllTrainees().size());
            trainee = gymFacade.createTrainee(trainee);
            System.out.println(gymFacade.findAllTrainees().size());
            System.out.println(gymFacade.findTraineeById(trainee.getId()));
            trainee.setAddress("Test address 2");
            System.out.println(gymFacade.updateTrainee(trainee));
            System.out.println(gymFacade.findTraineeById(trainee.getId()));
            System.out.println(gymFacade.deleteTrainee(trainee.getId()));
            System.out.println(gymFacade.findAllTrainees().size());


            ////Trainers
            Trainer trainer = new Trainer();
            trainer.setFirstName("Volodymyr");
            trainer.setLastName("Zganiaiko");
            trainer.setSpecialization(new TrainingType("Cross-fit"));

            System.out.println(gymFacade.findAllTrainers().size());
            trainer = gymFacade.createTrainer(trainer);
            System.out.println(gymFacade.findAllTrainers().size());
            System.out.println(gymFacade.findTrainerById(trainer.getId()));
            trainer.setSpecialization(new TrainingType("Joga"));
            System.out.println(gymFacade.updateTrainer(trainer));
            System.out.println(gymFacade.findTrainerById(trainer.getId()));


            //////Trainings
            trainee = gymFacade.createTrainee(trainee);
            trainer = gymFacade.createTrainer(trainer);
            Training training = new Training();
            training.setTraineeId(trainee.getId());
            training.setTrainerId(trainer.getId());
            training.setTrainingName("Joga training");
            training.setTrainingDate(LocalDate.parse("2026-06-24"));
            training.setTrainingType(trainer.getSpecialization());
            training.setTrainingDurationInMinutes(90);
            System.out.println(gymFacade.findAllTrainings().size());
            training = gymFacade.createTraining(training);
            System.out.println(gymFacade.findAllTrainings().size());
            System.out.println(gymFacade.findTrainingById(training.getId()));
        }
    }
}