package bam.Polyclinic.API.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "doctors_schedule")
public class DoctorSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "workday_start")
    private LocalTime workdayStart;

    @Column(name = "workday_end")
    private LocalTime workdayEnd;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}
