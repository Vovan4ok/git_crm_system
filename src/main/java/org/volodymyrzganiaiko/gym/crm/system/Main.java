package org.volodymyrzganiaiko.gym.crm.system;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.volodymyrzganiaiko.gym.crm.system.config.AppConfig;
import org.volodymyrzganiaiko.gym.crm.system.domain.*;
import org.volodymyrzganiaiko.gym.crm.system.facade.GymFacade;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (var applicationContext = new AnnotationConfigApplicationContext(AppConfig.class)) {
            GymFacade gymFacade = applicationContext.getBean(GymFacade.class);


            /////Trainees
            Trainee trainee = new Trainee();
            User user =  new User();
            user.setFirstName("John");
            user.setLastName("Doe");
            trainee.setUser(user);
            trainee.setDateOfBirth(LocalDate.parse("2003-11-08"));
            trainee.setAddress("Test address");

            System.out.println(gymFacade.findAllTrainees().size());
            trainee = gymFacade.createTrainee(trainee);
            System.out.println(gymFacade.findAllTrainees().size());
            System.out.println(gymFacade.findTraineeById(trainee.getId()));
            trainee.setAddress("Test address 2");
            System.out.println(gymFacade.updateTrainee(trainee.getUser().getUsername(), trainee.getUser().getPassword(), trainee));
            System.out.println(gymFacade.findTraineeById(trainee.getId()));


            ////Trainers
            Trainer trainer = new Trainer();
            User user2 = new  User();
            user2.setFirstName("Volodymyr");
            user2.setLastName("Zganiaiko");
            trainer.setUser(user2);
            trainer.setSpecialization(new TrainingType(1L, "Cross-fit"));
            System.out.println(gymFacade.findAllTrainers().size());
            trainer = gymFacade.createTrainer(trainer);
            System.out.println(gymFacade.findAllTrainers().size());
            System.out.println(gymFacade.findTrainerById(trainer.getId()));
            trainer.setSpecialization(new TrainingType(2L, "Yoga"));
            System.out.println(gymFacade.updateTrainer(trainer.getUser().getUsername(), trainer.getUser().getPassword(), trainer));
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
            training = gymFacade.createTraining(trainer.getUser().getUsername(), trainer.getUser().getPassword(), training);
            System.out.println(gymFacade.findAllTrainings().size());
            System.out.println(gymFacade.findTrainingById(training.getId()));

            List<Training> traineeTrainings = gymFacade.getTraineeTrainings(trainee.getUser().getUsername(), trainee.getUser().getPassword(), null, null, trainer.getUser().getUsername(), null);
            System.out.println(traineeTrainings);

            List<Training> trainerTrainings = gymFacade.getTrainerTrainings(trainer.getUser().getUsername(), trainer.getUser().getPassword(), null, null, trainee.getUser().getUsername());
            System.out.println(trainerTrainings);

            List<Trainer> unassignedTrainers = gymFacade.getUnassignedTrainers(trainee.getUser().getUsername(), trainee.getUser().getPassword());
            System.out.println(unassignedTrainers);

            gymFacade.changeTraineePassword(trainee.getUser().getUsername(), trainee.getUser().getPassword(), "random");
            gymFacade.changeTrainerPassword(trainer.getUser().getUsername(), trainer.getUser().getPassword(), "random");

            trainee = gymFacade.findTraineeById(trainee.getId()).get();
            trainer = gymFacade.findTrainerById(trainer.getId()).get();

            gymFacade.deactivateTrainee(trainee.getUser().getUsername(), trainee.getUser().getPassword());
            gymFacade.deactivateTrainer(trainer.getUser().getUsername(), trainer.getUser().getPassword());

            gymFacade.deleteTraineeByUsername(trainee.getUser().getUsername(), trainee.getUser().getPassword());
        }
    }
}