package org.volodymyrzganiaiko.gym.crm.system;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.volodymyrzganiaiko.gym.crm.system.config.AppConfig;
import org.volodymyrzganiaiko.gym.crm.system.domain.*;
import org.volodymyrzganiaiko.gym.crm.system.dto.Credentials;
import org.volodymyrzganiaiko.gym.crm.system.dto.TraineeRegistrationDTO;
import org.volodymyrzganiaiko.gym.crm.system.dto.TrainerRegistrationDTO;
import org.volodymyrzganiaiko.gym.crm.system.facade.GymFacade;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (var applicationContext = new AnnotationConfigApplicationContext(AppConfig.class)) {
            GymFacade gymFacade = applicationContext.getBean(GymFacade.class);


            /////Trainees
            User user =  new User();
            user.setFirstName("John");
            user.setLastName("Doe");
            Trainee trainee = new Trainee(LocalDate.parse("2003-11-08"), "Test address", user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword(), user.getIsActive(),  new HashSet<>());

            System.out.println(gymFacade.findAllTrainees().size());
            TraineeRegistrationDTO traineeRegistrationDTO = gymFacade.createTrainee(trainee);
            System.out.println(gymFacade.findAllTrainees().size());
            System.out.println(gymFacade.findTraineeById(trainee.getId()));
            trainee.setAddress("Test address 2");
            System.out.println(gymFacade.updateTrainee(new Credentials(trainee.getUsername(), trainee.getPassword()), trainee));
            System.out.println(gymFacade.findTraineeById(trainee.getId()));


            ////Trainers
            User user2 = new  User();
            user2.setFirstName("Volodymyr");
            user2.setLastName("Zganiaiko");
            Trainer trainer = new Trainer(new TrainingType(1L, "Cross-fit"), user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword(), user.getIsActive(), new HashSet<>());
            System.out.println(gymFacade.findAllTrainers().size());
            TrainerRegistrationDTO trainerRegistrationDTO = gymFacade.createTrainer(trainer);
            System.out.println(gymFacade.findAllTrainers().size());
            System.out.println(gymFacade.findTrainerById(trainer.getId()));
            trainer.setSpecialization(new TrainingType(2L, "Yoga"));
            System.out.println(gymFacade.updateTrainer(new Credentials(trainer.getUsername(), trainer.getPassword()), trainer));
            System.out.println(gymFacade.findTrainerById(trainer.getId()));


            //////Training
            Training training = new Training();
            training.setTrainee(trainee);
            training.setTrainer(trainer);
            training.setTrainingName("Yoga training");
            training.setTrainingDate(LocalDate.parse("2026-06-24"));
            training.setTrainingType(trainer.getSpecialization());
            training.setTrainingDurationInMinutes(90);

            System.out.println(gymFacade.findAllTrainings().size());
            training = gymFacade.createTraining(new Credentials(trainer.getUsername(), trainer.getPassword()), training);
            System.out.println(gymFacade.findAllTrainings().size());
            System.out.println(gymFacade.findTrainingById(training.getId()));

            List<Training> traineeTrainings = gymFacade.getTraineeTrainings(new Credentials(trainee.getUsername(), trainee.getPassword()), null, null, trainer.getUsername(), null);
            System.out.println(traineeTrainings);

            List<Training> trainerTrainings = gymFacade.getTrainerTrainings(new Credentials(trainer.getUsername(), trainer.getPassword()), null, null, trainee.getUsername());
            System.out.println(trainerTrainings);

            List<Trainer> unassignedTrainers = gymFacade.getUnassignedTrainers(new Credentials(trainee.getUsername(), trainee.getPassword()));
            System.out.println(unassignedTrainers);

            gymFacade.changeTraineePassword(new Credentials(trainee.getUsername(), trainee.getPassword()), "random");
            gymFacade.changeTrainerPassword(new Credentials(trainer.getUsername(), trainer.getPassword()), "random");

            trainee = gymFacade.findTraineeById(trainee.getId()).get();
            trainer = gymFacade.findTrainerById(trainer.getId()).get();

            gymFacade.deactivateTrainee(new Credentials(trainee.getUsername(), trainee.getPassword()));
            gymFacade.deactivateTrainer(new Credentials(trainer.getUsername(), trainer.getPassword()));

            gymFacade.deleteTraineeByUsername(new Credentials(trainee.getUsername(), trainee.getPassword()));
        }
    }
}