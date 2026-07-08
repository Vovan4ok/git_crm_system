package org.volodymyrzganiaiko.gym.crm.system.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name="trainings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "training_seq")
    @SequenceGenerator(name = "training_seq", sequenceName = "training_seq", allocationSize = 1)
    @Column(name = "training_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_type_id")
    private TrainingType trainingType;

    @NotBlank
    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @NotNull
    @Column(name = "training_date",  nullable = false)
    private LocalDate trainingDate;

    @NotNull
    @Column(name = "training_duration_in_minutes",  nullable = false)
    private Integer trainingDurationInMinutes;

    public Training(Trainee trainee, Trainer trainer,  TrainingType trainingType, String trainingName, LocalDate trainingDate, Integer trainingDurationInMinutes) {
        this.trainee = trainee;
        this.trainer = trainer;
        this.trainingType = trainingType;
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.trainingDurationInMinutes = trainingDurationInMinutes;
    }

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", trainingName='" + trainingName + '\'' +
                ", trainingDate=" + trainingDate +
                ", trainingDurationInMinutes=" + trainingDurationInMinutes +
                '}';
    }
}
